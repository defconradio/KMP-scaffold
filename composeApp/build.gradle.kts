plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        // Use outputModuleName with Provider API check
        // moduleName = "composeApp" // Deprecated
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val wasmJsMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.runtime)
                implementation(compose.ui)
            }
        }
    }
}

// Fix implicit dependency issue by enforcing task ordering
tasks.named("wasmJsBrowserProductionWebpack") {
    dependsOn(":shared:wasmJsProductionExecutableCompileSync")
}
tasks.named("wasmJsBrowserDevelopmentWebpack") {
    dependsOn(":shared:wasmJsDevelopmentExecutableCompileSync")
}
