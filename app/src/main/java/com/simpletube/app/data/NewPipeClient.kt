package com.simpletube.app.data

import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request as NpRequest
import org.schabi.newpipe.extractor.downloader.Response
import org.schabi.newpipe.extractor.localization.Localization
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import org.schabi.newpipe.extractor.stream.StreamType
import org.schabi.newpipe.extractor.stream.VideoStream

object NewPipeClient {

    private val LOCK = Any()

    @Volatile
    private var initialized = false

    private class OkHttpDownloader : Downloader() {
        private val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

        override fun execute(request: NpRequest): Response {
            val data = request.dataToSend()
            val builder = Request.Builder().url(request.url())

            val headers = request.headers()
            if (headers != null) {
                for ((name, values) in headers) {
                    for (value in values) {
                        builder.addHeader(name, value)
                    }
                }
            }

            val body = data?.toRequestBody()
            val okRequest = builder.method(request.httpMethod(), body).build()

            client.newCall(okRequest).execute().use { response ->
                return Response(
                    response.code,
                    response.message,
                    response.headers.toMultimap(),
                    response.body?.string(),
                    response.request.url.toString()
                )
            }
        }
    }

    fun init() {
        if (!initialized) {
            synchronized(LOCK) {
                if (!initialized) {
                    NewPipe.init(OkHttpDownloader(), Localization("es", "ES"))
                    initialized = true
                }
            }
        }
    }

    suspend fun search(query: String): List<VideoItem> = withContext(Dispatchers.IO) {
        init()
        val service = NewPipe.getService(0)
        val extractor = service.getSearchExtractor(query)
        extractor.fetchPage()

        extractor.initialPage.items
            .filterIsInstance<StreamInfoItem>()
            .filter {
                it.streamType == StreamType.VIDEO_STREAM ||
                    it.streamType == StreamType.LIVE_STREAM
            }
            .map { it.toVideoItem() }
    }

    suspend fun resolve(videoUrl: String): PlaySource = withContext(Dispatchers.IO) {
        init()
        val service = NewPipe.getServiceByUrl(videoUrl)
        val extractor = service.getStreamExtractor(videoUrl)
        extractor.fetchPage()

        // 1) HLS (lo mas compatible)
        val hls = runCatching { extractor.hlsUrl }.getOrNull()
        if (!hls.isNullOrBlank()) {
            return@withContext PlaySource.Hls(hls)
        }

        // 2) Video-only + audio (máxima calidad)
        val audio = extractor.audioStreams
            .filter { it.isUrl }
            .maxByOrNull { it.averageBitrate }

        val video = extractor.videoOnlyStreams
            .filter { it.isUrl && it.height > 0 }
            .maxWithOrNull(compareBy<VideoStream> { it.height }.thenBy { it.bitrate })

        if (video != null && audio != null) {
            return@withContext PlaySource.Merged(video.url, audio.url)
        }

        // 3) Fallback: stream progresivo muxeado
        val muxed = extractor.videoStreams
            .filter { it.isUrl }
            .maxWithOrNull(compareBy<VideoStream> { it.height }.thenBy { it.bitrate })

        if (muxed != null) {
            return@withContext PlaySource.Progressive(muxed.url)
        }

        throw IllegalStateException("No hay streams reproducibles para este video")
    }
}

private fun StreamInfoItem.toVideoItem(): VideoItem = VideoItem(
    title = name,
    url = url,
    thumbnail = thumbnails.maxByOrNull { it.height }?.url,
    uploader = uploaderName,
    durationSeconds = duration,
    views = viewCount
)
