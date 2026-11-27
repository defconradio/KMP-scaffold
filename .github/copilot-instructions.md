# Repository Instructions for KMP-scaffold

This repository is a production-ready Kotlin Multiplatform (KMP) scaffold with Clean Architecture + MVVM.

## Architecture & Code Standards
- **Multiplatform Structure:**
  - `shared/`: Cross-platform core with Clean Architecture layers:
    - `domain/`: Pure Kotlin business logic (entities, use cases, repository interfaces). No UI or platform framework dependencies.
    - `data/`: Network clients (Ktor 3.0.1), persistence (DataStore), DTOs, and repository implementations.
    - `presentation/`: UI state holders (ViewModels, `StateFlow`).
    - `di/`: Dependency injection modules using Koin 4.0.0 (`CommonModule.kt`, `platformModule`).
  - `composeApp/`: JetBrains Compose Multiplatform UI targeting Android, Desktop JVM, and Wasm (Web).
  - `androidApp/`: Android application launcher & configuration.
  - `desktopApp/`: Desktop JVM runner.
  - `serverApp/`: Optional backend service.
- **Tech Stack & Toolchain:**
  - Kotlin 2.1.20, Java JDK 21 (Toolchain enforced), Gradle wrapper.
  - Logging via Napier (`initLogger()`).
  - Resources via Compose Multiplatform Resources (`Res`).

## Agent Guardrails & Security Policies
- **Protected Secrets & Sensitive Files:**
  - Never commit signing keys, keystores (`*.jks`, `*.keystore`), `signing.properties`, `local.properties`, or `*.scp` scripts.
  - All credentials must be passed via environment variables or secret managers, never hardcoded.
- **Platform Portability:**
  - Never introduce Android-only (`android.*`) or JVM-only APIs into `shared/src/commonMain/`. Use `expect`/`actual` declarations when platform-specific APIs are required.
- **Pre-PR Local Verification:**
  - Run `./gradlew :shared:assemble :shared:jvmTest` to validate shared logic before proposing changes.
  - For UI or target changes, validate the affected target (e.g., `./gradlew :androidApp:assembleDebug` or `./gradlew :composeApp:wasmJsBrowserDistribution`).

## SDLC & Pull Request Workflow (GH-600 Domain 1)
- **Branching Policy:** Always work on a feature/fix branch (`feat/*`, `fix/*`). Direct pushes to `master` are strictly prohibited.
- **Durable Plans:** For architectural changes or multi-step tasks, create a plan artifact under `docs/plans/<feature>-plan.md` before implementation.
- **PR Submission:** Always open a Pull Request using `.github/pull_request_template.md`. Explicitly document multiplatform risks, breaking changes, and test evidence. Merges to `master` require CI check passage and human review.
