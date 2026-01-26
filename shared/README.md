Shared KMP module

Targets: android, ios (framework), js(IR), common

Build examples:

- Build shared library:
  ./gradlew :shared:build

- Build iOS framework (one of the architectures):
  ./gradlew :shared:linkDebugFrameworkIosX64

- Run JS tests:
  ./gradlew :shared:jsTest
