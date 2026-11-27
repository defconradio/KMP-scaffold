---
name: new-usecase
description: Scaffolds a new clean architecture UseCase in domain/, binds it in Koin di/, and adds unit tests.
---
Create a new UseCase for the specified feature:
1. Define a pure Kotlin interface/class under `shared/src/commonMain/kotlin/com/example/shared/domain/usecase/`.
2. Avoid any framework, Android, Ktor, or UI imports in the domain layer.
3. Register the UseCase factory/single in `shared/src/commonMain/kotlin/com/example/shared/di/AppModule.kt`.
4. Add unit test coverage in `shared/src/commonTest/` or `shared/src/jvmTest/`.
5. Run `./gradlew :shared:jvmTest` to verify.
