# Kotlin Multiplatform (KMP) MVVM Scaffold

![CI](https://github.com/defconradio/KMP-scaffold/actions/workflows/ci.yml/badge.svg)

A production-ready Kotlin Multiplatform scaffold designed for scalability, featuring **Clean Architecture**, **MVVM**, and modern KMP libraries.

## 🚀 Key Features

*   **Architecture:** Clean Architecture + MVVM (Model-View-ViewModel)
*   **Dependency Injection:** [Koin](https://insert-koin.io/) (v4.0.0)
*   **Networking:** [Ktor](https://ktor.io/) (v3.0.1) with ContentNegotiation & Serialization
*   **Logging:** [Napier](https://github.com/AAkira/Napier) (v2.7.1)
*   **Resources:** [JetBrains Compose Multiplatform Resources](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-resources.html) (Shared Strings, Images, Fonts)
*   **Persistence:** [AndroidX DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (Preferences)
*   **Navigation:** [JetBrains Compose Navigation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation.html)
*   **Targets:** Android, iOS, JVM (Desktop), Wasm (Web)

## 📂 Project Structure

The `shared` module is organized following Clean Architecture principles:

```text
shared/src/commonMain/kotlin/com/example/shared/
├── di/                 # Dependency Injection (Koin Modules)
├── domain/             # Business Logic (Entities, Use Cases, Repository Interfaces)
├── data/               # Data Layer (API Implementations, DTOs, Repository Impls)
└── presentation/       # UI State Holders (ViewModels, StateFlow)

shared/src/commonMain/composeResources/
├── drawable/           # Shared Images (SVG/XML)
├── values/             # Shared Strings & Colors
```

## 🛠️ Tech Stack & Configuration

*   **Kotlin:** 2.1.20
*   **Java:** JDK 21 (Toolchain enforced)
*   **Build System:** Gradle + Version Catalog (`libs.versions.toml`) + Default KMP Hierarchy Template

### Libraries Included:
1.  **Koin:** Pre-configured `initKoin`, `platformModule`, and `networkModule` for cross-platform DI.
2.  **Ktor Client:** Configured with JSON serialization (`kotlinx.serialization`) and Timeouts.
3.  **Napier:** Initialized via `initLogger()` on Android, iOS, and Wasm for cross-platform logging.
4.  **Compose Resources:** Type-safe access to resources via `com.example.shared.resources.Res`.

## 🏗️ How to Build

### 1. Build Shared Module
```bash
./gradlew :shared:build
```

### 2. Setup (Linux / Local Workstation)
- **Android Studio JDK:** `.idea/gradle.xml` and `.idea/misc.xml` are preconfigured to use `jbr-21` (JDK 21).
- **Local Network / Tor-routed Shells:** If `torsocks` is active in your shell, prefix local Gradle/server commands with `LD_PRELOAD=` or run `source torsocks off` for direct local IP access.
- **Chromium for Wasm Karma Tests:** Set `CHROME_BIN` so `wasmJsBrowserTest` can launch headless Chromium/Chrome:
```bash
export CHROME_BIN=$(which chromium || which chromium-browser || which google-chrome)
./gradlew kotlinUpgradeYarnLock --no-daemon && ./gradlew clean build --no-daemon
```
Or run only Wasm tests:
```bash
export CHROME_BIN=$(which chromium || which chromium-browser || which google-chrome)
./gradlew :shared:wasmJsBrowserTest
```
Or build without Wasm browser tasks:
```bash
./gradlew build -x wasmJsBrowserTest -x wasmJsBrowserDistribution
```

## 📱 Running the Apps

### 1. Run Android App
```bash
./gradlew :androidApp:installDebug
```
Tip: To connect an Android device/emulator to a local backend, forward ports with `adb reverse tcp:8081 tcp:8081`.

### 2. Generate Resources
If you add new images or strings, run this to regenerate the `Res` class:
```bash
./gradlew :shared:generateComposeResClass
```

### 3. Run Web App (Wasm)
Dev server with live reload & source debugging:
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```
Or build the distribution and serve locally with Cross-Origin Isolation headers (`COOP`/`COEP`) via `scripts/serve_wasm.py`:
```bash
# Development bundle
./gradlew :composeApp:wasmJsBrowserDevelopmentDistribution
python3 scripts/serve_wasm.py

# Production bundle
./gradlew :composeApp:wasmJsBrowserDistribution
python3 scripts/serve_wasm.py --prod
```

## 🍏 Building and Running the iOS App

To build the iOS XCFramework and prepare all required files for Xcode (requires macOS):

1. Open a terminal at the project root.
2. Make the iOS build script executable (only needed once):
   ```bash
   chmod +x iosApp/kmp-ios-build.sh
   ```
3. Run the script to build the shared KMP XCFramework (`:shared:assembleSharedReleaseXCFramework`):
   ```bash
   ./iosApp/kmp-ios-build.sh
   ```
   This builds `Shared.xcframework` into `shared/build/XCFrameworks/release` and copies it to `iosApp/Frameworks/Shared.xcframework`.

4. Open your Xcode project in `iosApp/`, link `iosApp/Frameworks/Shared.xcframework`, and build/run.

## 💡 Usage Examples

### Dependency Injection (Koin)
```kotlin
// In your Repository
class MyRepository(private val client: HttpClient) { ... }

// In Koin Module
val appModule = module {
    single { MyRepository(get()) }
}
```

### Shared Resources
```kotlin
// In Compose UI
Image(painter = painterResource(Res.drawable.my_icon), contentDescription = null)
Text(text = stringResource(Res.string.welcome_message))
```

### Logging (Napier)
```kotlin
Napier.d("Network request started", tag = "NetworkLayer")
Napier.e("Error parsing JSON", throwable = error)
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
