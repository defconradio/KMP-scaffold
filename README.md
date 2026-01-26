# Kotlin Multiplatform Scaffold

This scaffold provides a starting Kotlin Multiplatform (KMP) project with targets for:
- common
- Android
- iOS (frameowrk for integration)
- JavaScript (IR)

Structure:
- `shared/` — KMP module
- `androidApp/` — minimal Android app consuming `shared`

How to build

- Build shared library:
  ./gradlew :shared:build

- Build Android app (requires Android SDK):
  ./gradlew :androidApp:assembleDebug

Notes

This is a minimal scaffold. Add your own business logic and platform-specific code as needed.
Key guidelines


common ( shared/src/commonMain ): domain models, use-cases, validation, business rules, repository/service interfaces, shared helpers, serialization, and coroutine-based logic. Also add multiplatform libraries (Ktor, kotlinx.serialization, SQLDelight common APIs, kotlinx.coroutines).
Platform specifics ( shared/src/androidMain, shared/src/iosMain ): actual implementations for filesystem, DB drivers, HTTP engine, platform APIs, or any code using Android/iOS SDKs.
App UI and platform integration belong in androidApp/ (Android activities/fragments/Compose) or the platform app modules.
Use expect/actual for small platform hooks (time, device info, secure storage, platform logging).
Keep tests in shared/src/commonTest for logic and platform tests in platform test source sets.
