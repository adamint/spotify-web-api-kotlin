import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

plugins {
    `maven-publish`
    signing
    id("io.codearte.nexus-staging") version "0.21.2"
    kotlin("multiplatform") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"
    id("com.diffplug.gradle.spotless") version "4.4.0"
    id("com.moowork.node") version "1.3.1"
    id("org.jetbrains.dokka") version "0.10.1"
    id("com.android.library")
    id("kotlin-android-extensions")
}

repositories {
    jcenter()
    google()
    maven("https://kotlin.bintray.com/kotlinx")
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
    }
}

group = "com.adamratzman"
version = "3.2.02"

/*java {
    withSourcesJar()
    withJavadocJar()
}
*/

tasks.withType<Test> {
    this.testLogging {
        this.showStandardStreams = true
    }
}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.setSrcDirs(listOf("src/androidMain/kotlin"))
            res.setSrcDirs(listOf("src/androidMain/res"))
        }
        getByName("androidTest") {
            java.setSrcDirs(listOf("src/androidTest/kotlin"))
            res.setSrcDirs(listOf("src/androidTest/res"))
        }
    }
}

val dokkaJar by tasks.registering(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Docs"
    classifier = "javadoc"
    from(tasks.dokka)
}

kotlin {
    android {
        mavenPublication {
            artifactId = "spotify-api-kotlin-android"
            setupPom(artifactId)
        }

    }

    jvm {
        mavenPublication {
            artifactId = "spotify-api-kotlin"
            setupPom(artifactId)
        }
    }

    js {
        mavenPublication {
            artifactId = "spotify-api-kotlin-js"
            setupPom(artifactId)
        }

        browser {
            dceTask {
                keep("ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io")
            }

            webpackTask {
                output.libraryTarget = Target.UMD
            }

            testTask {
                enabled = false
            }
        }

        nodejs()
    }

    targets {
        sourceSets {
            val coroutineVersion = "1.3.7"
            val serializationVersion = "0.20.0"
            val spekVersion = "2.0.11"
            val ktorVersion = "1.3.2"

            val commonMain by getting {
                dependencies {
                    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutineVersion")
                    api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")
                    api("io.ktor:ktor-client-core:$ktorVersion")

                    implementation(kotlin("stdlib-common"))
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
                    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
                    api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
                    api("io.ktor:ktor-client-okhttp:$ktorVersion")
                    implementation(kotlin("stdlib-jdk8"))
                }
            }

            val jvmTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-junit"))
                    implementation("org.junit.jupiter:junit-jupiter:5.6.2")
                    implementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
                    runtimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
                    runtimeOnly(kotlin("reflect"))
                }
            }

            val jsMain by getting {
                dependencies {
                    api(npm("text-encoding", "0.7.0"))
                    api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutineVersion")
                    api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serializationVersion")
                    api("io.ktor:ktor-client-js:$ktorVersion")
                    api(npm("abort-controller", "3.0.0"))
                    api(npm("node-fetch", "2.6.0"))

                    compileOnly(kotlin("stdlib-js"))
                }
            }

            val jsTest by getting {
                dependencies {
                    implementation(kotlin("test-js"))
                    implementation("org.spekframework.spek2:spek-dsl-js:$spekVersion")
                }
            }

            val androidMain by getting {
                repositories {
                    mavenCentral()
                    jcenter()
                }

                dependencies {
                    api("net.sourceforge.streamsupport:android-retrofuture:1.7.2")
                    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
                    api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")
                    api("io.ktor:ktor-client-okhttp:$ktorVersion")
                    implementation(kotlin("stdlib-jdk8"))
                }
            }

            val androidTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-junit"))
                    implementation("org.junit.jupiter:junit-jupiter:5.6.2")
                    implementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
                    runtimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
                    runtimeOnly(kotlin("reflect"))
                }
            }

            all {
                languageSettings.useExperimentalAnnotation("kotlin.Experimental")
            }
        }
    }
}

publishing {
    publications {
        val kotlinMultiplatform by getting(MavenPublication::class) {
            artifactId = "spotify-api-kotlin-core"
            setupPom(artifactId)
        }

        val metadata by getting(MavenPublication::class) {
            artifactId = "spotify-api-kotlin-metadata"
            setupPom(artifactId)
        }
    }

    repositories {
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
    }
}

signing {
    sign(publishing.publications)
}

tasks {
    val dokka by getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"

        multiplatform {
            val js by creating {
                sourceLink {
                    path = "/src"
                    url = "https://github.com/adamint/com.adamratzman.spotify-web-api-kotlin/tree/master/"
                    lineSuffix = "#L"
                }
            }
            val jvm by creating {
                sourceLink {
                    path = "/src"
                    url = "https://github.com/adamint/com.adamratzman.spotify-web-api-kotlin/tree/master/"
                    lineSuffix = "#L"
                }
            }

            register("common") {
                sourceLink {
                    path = "/src"
                    url = "https://github.com/adamint/com.adamratzman.spotify-web-api-kotlin/tree/master/"
                    lineSuffix = "#L"
                }
            }

            register("global") {
                sourceLink {
                    path = "/src"
                    url = "https://github.com/adamint/com.adamratzman.spotify-web-api-kotlin/tree/master/"
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

    /* val javadocJar by getting(Jar::class) {
         dependsOn.add(javadoc)
         archiveClassifier.set("javadoc")
         from(javadoc)
     }*/

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


    getByName<Test>("jvmTest") {
        useJUnitPlatform()
    }


    val publishJvm by registering(Task::class) {
        dependsOn.add(check)
        dependsOn.add(dokka)
        dependsOn.add("publishJvmPublicationToNexusRepository")
    }

}


fun MavenPublication.setupPom(publicationName: String) {
    artifact(dokkaJar.get())

    pom {
        name.set(publicationName)
        description.set("A Kotlin wrapper for the Spotify Web API.")
        url.set("https://github.com/adamint/com.adamratzman.spotify-web-api-kotlin")
        inceptionYear.set("2018")
        scm {
            url.set("https://github.com/adamint/com.adamratzman.spotify-web-api-kotlin")
            connection.set("scm:https://github.com/adamint/com.adamratzman.spotify-web-api-kotlin.git")
            developerConnection.set("scm:git://github.com/adamint/com.adamratzman.spotify-web-api-kotlin.git")
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