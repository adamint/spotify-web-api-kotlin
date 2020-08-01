pluginManagement {
    val mainKotlinVersion = "1.3.72"

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin-multiplatform") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$mainKotlinVersion")
            }
            if (requested.id.id == "org.jetbrains.kotlin.jvm") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$mainKotlinVersion")
            }
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:$mainKotlinVersion")
            } else if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:3.5.0")
            } else if (requested.id.id == "kotlin-android-extensions") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.71")
            }
        }
    }

    repositories {
        mavenCentral()
        jcenter()
        google()
        maven { url = java.net.URI("https://plugins.gradle.org/m2/") }
    }
}

enableFeaturePreview("GRADLE_METADATA")

rootProject.name = "com.adamratzman.spotify-web-api-kotlin"

