# ================= R8 / ProGuard agresivo =================
-optimizationpasses 5
-allowaccessmodification
-overloadaggressively
-repackageclasses ''
-optimizations !code/simple/box

# ---------- NewPipeExtractor ----------
-keep class org.schabi.newpipe.extractor.NewPipe { *; }
-keep class org.schabi.newpipe.extractor.downloader.** { *; }
-keep class org.schabi.newpipe.extractor.stream.** { *; }
-keep class org.schabi.newpipe.extractor.search.** { *; }
-keep class org.schabi.newpipe.extractor.services.** { *; }
-keep class org.schabi.newpipe.extractor.exceptions.** { *; }
-keep class org.schabi.newpipe.extractor.localization.** { *; }
-keep class org.schabi.newpipe.extractor.linkhandler.** { *; }
-keepclassmembers class * extends org.schabi.newpipe.extractor.downloader.Downloader { *; }

# ---------- OkHttp / Okio ----------
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn org.slf4j.**

# ---------- Media3 / ExoPlayer ----------
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# ---------- Kotlin / Coroutines ----------
-dontwarn kotlinx.coroutines.**
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }
-keepclassmembers class kotlin.coroutines.jvm.internal.DebugMetadataKt { *; }
