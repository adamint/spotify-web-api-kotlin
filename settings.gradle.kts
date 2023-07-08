pluginManagement {
    val kotlinVersion: String by settings
    val androidBuildToolsVersion: String by settings

    plugins {
        id("org.jetbrains.kotlin.multiplatform").version(kotlinVersion)
        id("org.jetbrains.kotlin.plugin.serialization").version(kotlinVersion)
        id("org.jetbrains.dokka").version(kotlinVersion)
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin-multiplatform") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
            }
            if (requested.id.id == "org.jetbrains.kotlin.jvm") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
            }
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
            } else if (requested.id.id == "com.android.library") {
                useModule("com.android.tools.build:gradle:$androidBuildToolsVersion")
            } else if (requested.id.id == "kotlin-android-extensions") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
            }
        }
    }

    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
        maven { url = java.net.URI("https://plugins.gradle.org/m2/") }
    }
}

rootProject.name = "spotify-api-kotlin"