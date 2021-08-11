pluginManagement {
    val mainKotlinVersion = "1.5.0"

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
                useModule("com.android.tools.build:gradle:3.5.4")
            } else if (requested.id.id == "kotlin-android-extensions") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$mainKotlinVersion")
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

rootProject.name = "spotify-web-api-kotlin"
include("java-interop-basic-sample")
findProject(":java-interop-basic-sample")?.name = "java-interop-basic"
include("java-interop-sample")
