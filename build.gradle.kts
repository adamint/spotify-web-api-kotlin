import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import java.net.URI

plugins {
    `maven-publish`
    signing
    `java-library`
    id("io.codearte.nexus-staging") version "0.21.2"
    kotlin("multiplatform") version "1.3.61"
    kotlin("plugin.serialization") version "1.3.61"
    id("com.diffplug.gradle.spotless") version "3.26.1"
    id("com.moowork.node") version "1.3.1"
    id("org.jetbrains.dokka") version "0.10.0"
}

group = "com.adamratzman"
version = "3.0.02"

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<Test> {
    this.testLogging {
        this.showStandardStreams = true
    }
}

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
            val coroutineVersion = "1.3.3"
            val serializationVersion = "0.14.0"
            val spekVersion = "2.0.9"
            val ktorVersion = "1.3.0-rc2"

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
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
                    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                    implementation(kotlin("stdlib-jdk8"))
                }
            }

            val jvmTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-junit"))
                    implementation("org.junit.jupiter:junit-jupiter:5.6.0-M1")
                    implementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
                    runtimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
                    runtimeOnly(kotlin("reflect"))
                }
            }

            val jsMain by getting {
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutineVersion")
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

            all {
                languageSettings.useExperimentalAnnotation("kotlin.Experimental")
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
    useJUnitPlatform()
}

spotless {
    kotlin {
        target("**/*.kt")
        licenseHeader("/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */")
        ktlint()
    }
}

nexusStaging {
    packageGroup = "com.adamratzman"
}


tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

publishing {
    publications {
        val jvm by getting(MavenPublication::class) {
            artifactId = "spotify-api-kotlin"
            artifact(tasks.getByName("javadocJar"))
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

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
        if (!project.hasProperty("publishToSpace")) {
            maven {
                name = "nexus"
                val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

                credentials {
                    val nexusUsername: String? by project.extra
                    val nexusPassword: String? by project.extra
                    username = nexusUsername
                    password = nexusPassword
                }
            }
        } else {
            if (project.extra.has("spaceUser") && project.extra.has("spacePassword")) {
                maven {
                    credentials {
                        username = project.extra["spaceUser"]?.toString()
                        password = project.extra["spacePassword"]?.toString()
                    }

                    url = URI.create("https://maven.jetbrains.space/adam/ratzman")
                }
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
        (has("signing.keyId") && has("signing.secretKeyRingFile") && has("signing.password"))
                || (has("signing.notNeeded") && get("signing.notNeeded") == "true")
    }
    if (!alreadyConfigured && allTasks.any { it is Sign }) {
        // Use Java's console to read from the console (no good for
        // a CI environment)
        val console = System.console()
        requireNotNull(console) { "Could not get signing config: please provide yours in the gradle.properties file." }
        console.printf(
                "\n\nWe have to sign some things in this build." +
                        "\n\nPlease enter your signing details.\n\n"
        )

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

tasks {
    val dokka by getting(DokkaTask::class) {
        outputDirectory = "docs/docs"
        outputFormat = "html"

        multiplatform {
            val js by creating {
                sourceLink {
                    path = "/src"
                    url = "https://github.com/adamint/spotify-web-api-kotlin/tree/master/"
                    lineSuffix = "#L"
                }
            }
            val jvm by creating {
                sourceLink {
                    path = "/src"
                    url = "https://github.com/adamint/spotify-web-api-kotlin/tree/master/"
                    lineSuffix = "#L"
                }
            }

            register("common") {
                sourceLink {
                    path = "/src"
                    url = "https://github.com/adamint/spotify-web-api-kotlin/tree/master/"
                    lineSuffix = "#L"
                }
            }

            register("global") {
                sourceLink {
                    path = "/src"
                    url = "https://github.com/adamint/spotify-web-api-kotlin/tree/master/"
                    lineSuffix = "#L"
                }

                sourceRoot {
                    path = kotlin.sourceSets.getByName("jvmMain").kotlin.srcDirs.first().toString()
                }
                sourceRoot {
                    path = kotlin.sourceSets.getByName("commonMain").kotlin.srcDirs.first().toString()
                }
            }
        }
    }

    val javadocJar by getting(Jar::class) {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
    }

    artifacts {
        archives(javadocJar)
    }

    val publishJvm by registering(Task::class) {
        dependsOn.add(check)
        dependsOn.add(dokka)
        dependsOn.add("publishJvmPublicationToNexusRepository")
    }
}