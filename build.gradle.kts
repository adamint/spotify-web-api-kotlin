plugins {
    kotlin("multiplatform") version "1.3.50"
}

group = "spotify-web-api-kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    //js()

    sourceSets {
        val coroutineVersion = "1.3.2"

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutineVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        jvm().compilations["main"].defaultSourceSet {
            repositories {
                mavenCentral()
                jcenter()
            }

            dependencies {
                val moshiVersion = "1.8.0"
                val googleHttpClientVersion = "1.32.1"

                implementation("com.neovisionaries:nv-i18n:1.26")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
                implementation("com.google.http-client:google-http-client:$googleHttpClientVersion")
                implementation("com.squareup.moshi:moshi:$moshiVersion")
                implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
                implementation(kotlin("stdlib-jdk8"))
            }
        }

        jvm().compilations["test"].defaultSourceSet {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}