plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

kotlin {
    androidTarget()
    jvmToolchain(17)
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js(IR) {
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val iosMain by creating {
            dependsOn(commonMain)
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

        val jsMain by getting
        val jsTest by getting
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
        val result = project.exec { commandLine = cmd }
        if (result.exitValue != 0) {
            throw GradleException("xcodebuild failed with exit code ${result.exitValue}")
        }
    }
}
