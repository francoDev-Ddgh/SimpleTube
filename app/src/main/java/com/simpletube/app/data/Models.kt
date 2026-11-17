package com.simpletube.app.data

data class VideoItem(
    val title: String,
    val url: String,
    val thumbnail: String?,
    val uploader: String?,
    val durationSeconds: Long,
    val views: Long
) {
    val durationText: String
        get() = if (durationSeconds <= 0) {
            ""
        } else {
            val minutes = durationSeconds / 60
            val seconds = durationSeconds % 60
            String.format("%d:%02d", minutes, seconds)
        }
}

sealed class PlaySource {
    data class Hls(val url: String) : PlaySource()
    data class Progressive(val url: String) : PlaySource()
    data class Merged(val videoUrl: String, val audioUrl: String) : PlaySource()
}
