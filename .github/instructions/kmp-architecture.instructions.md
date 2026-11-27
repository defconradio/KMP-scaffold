---
applyTo: "{shared/**,composeApp/**}"
---
# KMP Clean Architecture & Multiplatform Guidelines

- **Domain Isolation:**
  - Files under `shared/src/commonMain/kotlin/.../domain/` must remain pure Kotlin.
  - Do not import Android framework, Ktor, Compose UI, or platform-specific libraries into `domain/`.
- **Data & Networking:**
  - Implement repository interfaces under `shared/src/commonMain/kotlin/.../data/`.
  - Use Ktor with `kotlinx.serialization` for network calls. Handle exceptions and map DTOs to Domain models.
- **Presentation & State:**
  - ViewModels in `presentation/` must expose immutable state flows (`StateFlow<UiState>`) and consume intent/action events.
  - Avoid launching unconfined coroutines; use the lifecycle-aware coroutine scope.
- **Dependency Injection:**
  - Register new services, repositories, and ViewModels in Koin modules under `shared/src/commonMain/kotlin/.../di/`.
- **Testing Requirements:**
  - Any new domain use case or repository logic must be accompanied by unit tests under `shared/src/commonTest/` or `shared/src/jvmTest/`.
