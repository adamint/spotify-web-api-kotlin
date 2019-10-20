plugins {
    kotlin("multiplatform") version "1.3.50"
    kotlin("plugin.serialization") version "1.3.50"
    id("com.diffplug.gradle.spotless") version "3.25.0"
}

group = "spotify-web-api-kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx")
}

kotlin {
    jvm()
    //js()

    sourceSets {
        val coroutineVersion = "1.3.2"
        val serializationVersion = "0.13.0"
        val spekVersion = "2.0.8"
        val ktorVersion = "1.2.5"

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutineVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("org.spekframework.spek2:spek-dsl-metadata:$spekVersion")
            }
        }

        jvm().compilations["main"].defaultSourceSet {
            repositories {
                mavenCentral()
                jcenter()
            }

            dependencies {
                val googleHttpClientVersion = "1.32.1"

                implementation("com.neovisionaries:nv-i18n:1.26")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
                implementation("io.ktor:ktor-client-apache:$ktorVersion")
                implementation(kotlin("stdlib-jdk8"))

            }
        }

        jvm().compilations["test"].defaultSourceSet {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("org.junit.jupiter:junit-jupiter:5.5.2")
                implementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
                runtimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
                runtimeOnly(kotlin("reflect"))
            }
        }
    }
}

tasks.named<Test>("jvmTest") {
    systemProperty("clientId", System.getProperty("clientId"))
    systemProperty("clientSecret", System.getProperty("clientSecret"))

    useJUnitPlatform()
}

spotless {
    kotlin {
        target("**/*.kt")
        licenseHeader("/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */")
        ktlint()
    }
}
