import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    `maven-publish`
    signing
    kotlin("multiplatform") version "1.3.50"
    kotlin("plugin.serialization") version "1.3.50"
    id("com.diffplug.gradle.spotless") version "3.25.0"
    id("com.moowork.node") version "1.3.1"
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
    js()

    targets {
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

            val jvmMain by getting {
                repositories {
                    mavenCentral()
                    jcenter()
                }

                dependencies {
                    implementation("com.neovisionaries:nv-i18n:1.26")
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
                    implementation("io.ktor:ktor-client-apache:$ktorVersion")
                    implementation(kotlin("stdlib-jdk8"))
                }
            }

            val jvmTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-junit"))
                    implementation("org.junit.jupiter:junit-jupiter:5.5.2")
                    implementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
                    runtimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
                    runtimeOnly(kotlin("reflect"))
                }
            }

            val jsMain by getting {
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.2")
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serializationVersion")
                    implementation("io.ktor:ktor-client-js:$ktorVersion")
                    compileOnly(kotlin("stdlib-js"))
                }
            }

            val jsTest by getting {
                dependencies {
                    implementation(kotlin("test-js"))
                    implementation("org.spekframework.spek2:spek-dsl-js:$spekVersion")
                }
            }

        }
    }
}

tasks.getByName<KotlinJsCompile>("compileKotlinJs") {
    kotlinOptions {
        moduleKind = "umd"
        noStdlib = false
        metaInfo = true
    }
}
tasks.named<Test>("jvmTest") {
    systemProperty("clientId", System.getProperty("clientId"))
    systemProperty("clientSecret", System.getProperty("clientSecret"))
    systemProperty("spotifyRedirectUri", System.getProperty("spotifyRedirectUri"))
    systemProperty("spotifyTokenString", System.getProperty("spotifyTokenString"))

    useJUnitPlatform()
}

spotless {
    kotlin {
        target("**/*.kt")
        licenseHeader("/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */")
        ktlint()
    }
}

publishing {
    publications {
        val jvm by getting(MavenPublication::class) {
            pom {
                name.set("spotify-api-kotlin")
                description.set("A Kotlin wrapper for the Spotify Web API.")
                url.set("https://github.com/adamint/spotify-web-api-kotlin")
                inceptionYear.set("2018")
                scm {
                    url.set("https://github.com/adamint/spotify-web-api-kotlin")
                    connection.set("scm:https://github.com/adamint/spotify-web-api-kotlin.git")
                    developerConnection.set("scm:git://github.com/adamint/spotify-web-api-kotlin.git")
                }
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("adamratzman")
                        name.set("Adam Ratzman")
                        email.set("adam@adamratzman.com")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            name = "nexus"
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

            credentials {
                val nexusUser: String? by project.extra
                val nexusPassword: String? by project.extra
                username = nexusUser
                password = nexusPassword
            }
        }
    }
}

signing {
    sign(publishing.publications["jvm"])
}

// get signing confs interactivly if needed
gradle.taskGraph.whenReady {
    val alreadyConfigured = with(project.extra) {
        (has("singing.keyId") && has("signing.secretKeyRingFile") && has("signing.password"))
                || (has("signing.notNeeded") && get("signing.notNeeded") == "true")
    }
    if (!alreadyConfigured && allTasks.any { it is Sign }) {
        // Use Java's console to read from the console (no good for
        // a CI environment)
        val console = System.console()
        requireNotNull(console) { "Could not get signing config: please provide yours in the gradle.properties file." }
        console.printf("\n\nWe have to sign some things in this build." +
                "\n\nPlease enter your signing details.\n\n")

        val id = console.readLine("PGP Key Id: ")
        val file = console.readLine("PGP Secret Key Ring File (absolute path): ")
        val password = console.readPassword("PGP Private Key Password: ")

        allprojects {
            project.extra["signing.keyId"] = id
            project.extra["signing.secretKeyRingFile"] = file
            project.extra["signing.password"] = password
        }

        console.printf("\nThanks.\n\n")
    }
}
