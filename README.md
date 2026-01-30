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

*   **Kotlin:** 2.1.21
*   **Java:** JDK 21 (Toolchain enforced)
*   **Build System:** Gradle + Version Catalog (`libs.versions.toml`)

### Libraries Included:
1.  **Koin:** Pre-configured `initKoin` function and `networkModule` for HTTP client injection.
2.  **Ktor Client:** Configured with JSON serialization (Kotlinx.serialization) and Timeouts.
3.  **Napier:** initialized in `AndroidApp.kt` and `KoinModule.kt` for cross-platform logging.
4.  **Compose Resources:** Type-safe access to resources via `Res.string.*` and `Res.drawable.*`.

## 🏗️ How to Build

### 1. Build Shared Module
```bash
./gradlew :shared:build
```

### 2. Setup (Linux)
You need to set the `CHROME_BIN` environment variable for the `wasmJs` tests to find the Chromium binary.

Add this line to your `~/.bashrc` (or `~/.zshrc`) to make it permanent:
```bash
echo 'export CHROME_BIN=$(which chromium || which chromium-browser)' >> ~/.bashrc

./gradlew kotlinUpgradeYarnLock && ./gradlew clean build
```
otherwise build with 
`export CHROME_BIN=$(which chromium) && ./gradlew kotlinUpgradeYarnLock && ./gradlew clean build` 

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
./gradlew :shared:wasmJsBrowserDevelopmentRun
```

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
