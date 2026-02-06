plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose) // Add Kotlin Compose compiler plugin
    alias(libs.plugins.jetbrains.compose) // Apply plugin in Shared
}

kotlin {
    androidTarget()
    jvm() // Add JVM target
    jvmToolchain(21)
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            testTask {
                useKarma {
                     useChromeHeadless()
                }
            }
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.koin.core) // Add Koin Core to commonMain
                implementation(libs.napier) // Add Napier Logger
                implementation(compose.runtime) // Add Compose Runtime
                implementation(compose.foundation) // Add Compose Foundation
                implementation(compose.material3) // Add Compose Material 3
                implementation(compose.material) // Add Compose Material for Icons
                implementation(compose.materialIconsExtended) // Add Extended Icons
                implementation(compose.ui) // Add Compose UI
                implementation(compose.components.resources) // Add Resources component
                implementation(libs.androidx.navigation.compose) // Add Compose Navigation
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.koin.test) // Add Koin Test
            }
        }
        val androidMain by getting {
             dependencies {
                implementation(libs.ktor.client.cio)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.datastore.preferences) // Added DataStore to platform specific
                implementation(libs.androidx.core.ktx) // Add Core KTX for WindowCompat
                implementation(libs.androidx.activity.compose) // Ensure activity compose is available
             }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.datastore.preferences) // Added DataStore to platform specific
            }
        }
        val iosTest by creating {
            dependsOn(commonTest)
        }
        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
        val iosX64Test by getting { dependsOn(iosTest) }
        val iosArm64Test by getting { dependsOn(iosTest) }
        val iosSimulatorArm64Test by getting { dependsOn(iosTest) }

        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.datastore.preferences) // Added DataStore to platform specific
            }
        }
        val jvmTest by getting

        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
    }
}

android {
    namespace = "com.example.shared"
    //noinspection GradleDependency
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

// Publish as a framework for iOS
kotlin.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).configureEach {
    binaries.framework {
        baseName = "Shared"
    }
}

// XCFramework creation task (requires macOS to actually build native frameworks)
tasks.register("createXCFramework") {
    group = "distribution"
    description = "Assembles an XCFramework from iOS framework binaries (requires macOS)."

    doLast {
        val buildDir = layout.buildDirectory.asFile.get()
        val output = file("${buildDir}/xcframework/Shared.xcframework")
        output.parentFile.mkdirs()

        val frameworks = listOf(
            "${buildDir}/bin/iosX64/debugFramework/Shared.framework",
            "${buildDir}/bin/iosArm64/debugFramework/Shared.framework",
            "${buildDir}/bin/iosSimulatorArm64/debugFramework/Shared.framework"
        ).filter { file(it).exists() }

        if (frameworks.isEmpty()) {
            logger.warn("No iOS frameworks were found. Build the iOS frameworks first on macOS: e.g. \"./gradlew :shared:linkDebugFrameworkIosArm64\"")
            return@doLast
        }

        val cmd = mutableListOf("xcodebuild", "-create-xcframework")
        frameworks.forEach { cmd.addAll(listOf("-framework", it)) }
        cmd.addAll(listOf("-output", output.absolutePath))

        logger.lifecycle("Creating XCFramework: ${output.absolutePath}")
        val result = providers.exec { commandLine = cmd }.result.get()
        if (result.exitValue != 0) {
            throw GradleException("xcodebuild failed with exit code ${result.exitValue}")
        }
    }
}

// --- KMP iOS XCFramework export for Xcode integration ---
tasks.register("syncFramework") {
    group = "xcode"
    description = "Builds and copies the iOS XCFramework to shared/build/XCFrameworks/release for Xcode."
    dependsOn(
        "linkReleaseFrameworkIosArm64",
        "linkReleaseFrameworkIosX64",
        "linkReleaseFrameworkIosSimulatorArm64"
    )
    doLast {
        val buildDir = layout.buildDirectory.asFile.get()
        val xcDir = file("${buildDir}/XCFrameworks/release")
        xcDir.mkdirs()
        val frameworks = listOf(
            "${buildDir}/bin/iosArm64/releaseFramework/Shared.framework",
            "${buildDir}/bin/iosSimulatorArm64/releaseFramework/Shared.framework"
        )
        val cmd = mutableListOf("xcodebuild", "-create-xcframework")
        frameworks.forEach { cmd.addAll(listOf("-framework", it)) }
        cmd.addAll(listOf("-output", "${xcDir}/Shared.xcframework"))
        exec { commandLine = cmd }
    }
}

tasks.named("wasmJsBrowserTest") {
    dependsOn(":composeApp:wasmJsTestTestDevelopmentExecutableCompileSync")
}
