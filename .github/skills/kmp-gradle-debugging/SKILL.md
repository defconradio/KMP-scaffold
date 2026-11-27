---
name: kmp-gradle-debugging
description: Diagnoses Kotlin Multiplatform compilation, dependency, and expect/actual mismatches.
---
1. Run `./gradlew :shared:jvmTest --stacktrace` to isolate the failing source set.
2. Check `gradle/libs.versions.toml` for Ktor, Koin, or Kotlin version conflicts.
3. Verify every `expect` declaration in `commonMain` has matching `actual` implementations in `androidMain`, `jvmMain`, and `wasmJsMain`.
