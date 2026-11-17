# SimpleTube

Cliente alternativo de YouTube simple, en **Kotlin + Jetpack Compose**, que usa
**[NewPipeExtractor](https://github.com/TeamNewPipe/NewPipeExtractor)** para buscar videos y
resolver los streams de reproducción.

- Buscador de videos de YouTube con miniaturas.
- Reproductor con **Media3 / ExoPlayer** (HLS, progresivo o video+audio fusionado).
- Interfaz Material 3 oscura, estilo YouTube.
- Compilación en la nube con **GitHub Actions** y APK **release** optimizado con **R8**.
- Sin keystore: el APK se genera **sin firmar**.

## Estructura

| Ruta | Contenido |
|---|---|
| `app/src/main/java/com/simpletube/app/data/` | Cliente de NewPipeExtractor (Downloader OkHttp, búsqueda, resolución de streams) |
| `app/src/main/java/com/simpletube/app/ui/` | Pantallas Compose: búsqueda, lista de resultados, reproductor |
| `app/src/main/java/com/simpletube/app/ui/theme/` | Tema Material 3 |
| `.github/workflows/build.yml` | CI: compila el APK release y lo publica en Releases |

## Build en la nube (recomendado)

Cada `push` a `main` (o ejecución manual desde la pestaña **Actions**) compila y publica:

1. `Actions` → `Build & Release APK` → `Run workflow`.
2. Al terminar, el APK queda en **Releases** como `SimpleTube-release.apk`
   (también disponible como artifact).

## Build local

```bash
gradle wrapper --gradle-version 8.9
./gradlew assembleRelease
# salida: app/build/outputs/apk/release/app-release-unsigned.apk
```

## Instalar el APK sin firmar

```bash
apksigner sign --ks debug.keystore app-release-unsigned.apk
adb install app-release-unsigned.apk
```

## Stack

- AGP 8.7.3, Kotlin 2.0.21, Gradle 8.9
- compileSdk 35 / minSdk 24 / targetSdk 35
- Compose BOM 2024.12.01, Material 3
- Media3 1.5.1 (ExoPlayer + HLS)
- NewPipeExtractor v0.27.1 (JitPack)
- OkHttp 4.12.0, Coil 2.7.0

## Notas

- El APK release se genera **unsigned** a propósito (no hay keystore en el repo).
- Para publicar en Google Play habría que añadir un keystore y un `signingConfig`.
