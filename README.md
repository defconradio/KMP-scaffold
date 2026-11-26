# Kotlin Multiplatform (KMP) MVVM Scaffold

![CI](https://github.com/defconradio/KMP-scaffold/actions/workflows/ci.yml/badge.svg)

A production-ready Kotlin Multiplatform scaffold designed for scalability, featuring **Clean Architecture**, **MVVM**, and modern KMP libraries.

## 🚀 Key Features

*   **Architecture:** Clean Architecture + MVVM (Model-View-ViewModel)
*   **Dependency Injection:** [Koin](https://insert-koin.io/) (v4.0.0)
*   **Networking:** [Ktor](https://ktor.io/) (v3.0.0-rc-1) with ContentNegotiation & Serialization
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

### 2. Setup (Linux)
You need to set the `CHROME_BIN` environment variable for the `wasmJs` tests to find the Chromium binary.
install: 
`sudo apt update && sudo apt install -y chromium-browser`

Add to your `~/.bashrc` (or `~/.zshrc`) to make it permanent:
```bash
echo 'export CHROME_BIN=$(which chromium-browser)' >> ~/.bashrc
source ~/.bashrc
./gradlew kotlinUpgradeYarnLock && ./gradlew clean build
```
otherwise build with 
`export CHROME_BIN=$(which chromium-browser) && ./gradlew kotlinUpgradeYarnLock && ./gradlew clean build` 

or build without wasm
`./gradlew build -x wasmJsBrowserTest -x wasmJsBrowserDistribution`

## 📱 Running the Apps

### 1. Run Android App
```bash
./gradlew :androidApp:installDebug
```

### 2. Generate Resources
If you add new images or strings, run this to regenerate the `Res` class:
```bash
./gradlew :shared:generateComposeResClass
```

### 3. Run Web App (Wasm)
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
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
