./gradlew kotlinUpgradeYarnLock
./gradlew :shared:wasmJsBrowserTest

./gradlew :composeApp:wasmJsBrowserDevelopmentRun
./gradlew :composeApp:wasmJsBrowserDevelopmentRun --dry-run

./gradlew clean build


./gradlew :shared:wasmJsBrowserTest --no-daemon
