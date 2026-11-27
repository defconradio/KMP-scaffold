## Summary
<!-- Concise description of changes, context, and motivation -->

## Scope & Autonomy Level
- **Autonomy Level:** Low / Medium / High
- **Task Scoping:** Bounded to repository / branch / issue

## Risk Notes
- **Multiplatform Targets Affected:** (Android / iOS / Desktop JVM / Wasm Web / None)
- **Shared API / Contract Changes:** (Yes/No — explain any interface or DTO modifications)
- **Dependency & Build Changes:** (Note any updates to `gradle/libs.versions.toml` or Gradle scripts)
- **Workflow / CI Touched?** (Yes/No — note any `.github/workflows/` modifications)
- **Secrets & Keystores Check:** (Confirm no keys, credentials, `local.properties`, or `*.scp` files are committed)

## Validation Evidence
- [ ] `./gradlew :shared:assemble` executed successfully
- [ ] `./gradlew :shared:jvmTest` executed and all unit tests passed
- [ ] Target builds verified (e.g., `:androidApp:assembleDebug` or `:composeApp:wasmJsBrowserDistribution` if touched)
- [ ] Code formatted and clean of unused dependencies/imports
