import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

plugins {
    `maven-publish`
    signing
    id("io.codearte.nexus-staging") version "0.22.0"
    id("com.android.library")
    kotlin("multiplatform") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.20"
    id("com.diffplug.gradle.spotless") version "4.4.0"
    id("com.moowork.node") version "1.3.1"
    id("org.jetbrains.dokka") version "1.4.20"
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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
    }
}

group = "com.adamratzman"
version = System.getenv("SPOTIFY_API_PUBLISH_VERSION") ?: "0.0.0.SNAPSHOT"

System.getenv("signing.keyId")?.let { project.ext["signing.keyId"] = it }
System.getenv("signing.password")?.let { project.ext["signing.password"] = it }
System.getenv("signing.secretKeyRingFile")?.let { project.ext["signing.secretKeyRingFile"] = it }


tasks.withType<Test> {
    this.testLogging {
        this.showStandardStreams = true
    }
}

android {
    compileSdkVersion(30)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packagingOptions {
        exclude("META-INF/*.md")
    }
    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(30)
        compileSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    testOptions {
        this.unitTests.isReturnDefaultValues = true
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.setSrcDirs(listOf("src/androidMain/kotlin"))
            res.setSrcDirs(listOf("src/androidMain/res"))
        }
        getByName("test") {
            java.setSrcDirs(listOf("src/androidTest/kotlin"))
            res.setSrcDirs(listOf("src/androidTest/res"))
        }
    }
}

val dokkaJar by tasks.registering(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Docs"
    classifier = "javadoc"
    from(tasks.dokkaHtml)
}

kotlin {
    explicitApiWarning()

    android {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }

        mavenPublication {
            setupPom(artifactId)
        }

        publishLibraryVariants("debug", "release")

        publishLibraryVariantsGroupedByFlavor = true
    }

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }

        mavenPublication {
            setupPom(artifactId)
        }
    }

    js(KotlinJsCompilerType.LEGACY) {
        mavenPublication {
            setupPom(artifactId)
        }

        browser {
            webpackTask {
                output.libraryTarget = Target.UMD
            }

            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
                //   this.
            }
        }

        nodejs {
            testTask {
                useMocha {
                    timeout = "5000"
                }
            }
        }
    }

    val hostOs = System.getProperty("os.name")
    val isMainHost = hostOs.contains("mac", true)
    //val isMainPlatform =
    val isMingwX64 = hostOs.startsWith("Windows")

    macosX64 {
        mavenPublication {
            setupPom(artifactId)
        }
    }
    linuxX64 {
        mavenPublication {
            setupPom(artifactId)
        }
    }
    mingwX64 {
        mavenPublication {
            setupPom(artifactId)
        }
    }

    val publicationsFromMainHost =
        listOf(jvm(), js()).map { it.name } + "kotlinMultiplatform"

    publishing {
        publications {
            matching { it.name in publicationsFromMainHost }.all {
                val targetPublication = this@all
                tasks.withType<AbstractPublishToMaven>()
                    .matching { it.publication == targetPublication }
                    .configureEach { onlyIf { findProperty("isMainHost") == "true" } }
            }

            val kotlinMultiplatform by getting(MavenPublication::class) {
                artifactId = "spotify-api-kotlin-core"
                setupPom(artifactId)
            }

            /*val metadata by getting(MavenPublication::class) {
                artifactId = "spotify-api-kotlin-metadata"
                setupPom(artifactId)
            }*/
        }

        repositories {
            maven {
                name = "nexus"
                val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

                credentials {
                    val nexusUsername: String? = System.getenv("nexus.username") ?: project.extra["nexusUsername"] as? String
                    val nexusPassword: String? = System.getenv("nexus.password") ?: project.extra["nexusPassword"] as? String
                    username = nexusUsername
                    password = nexusPassword
                }
            }
        }
    }

    targets {
        sourceSets {
            val coroutineVersion = "1.4.2-native-mt"
            val serializationVersion = "1.0.1"
            val ktorVersion = "1.5.0"
            val klockVersion = "2.0.3"

            val commonMain by getting {
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                    implementation("io.ktor:ktor-client-core:$ktorVersion")
                }
            }

            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
                }
            }

            val jvmMain by getting {
                repositories {
                    mavenCentral()
                    jcenter()
                }

                dependencies {
                    implementation("io.ktor:ktor-client-cio:$ktorVersion")
                }
            }

            val jvmTest by getting {
                dependencies {
                    implementation(kotlin("test-junit"))
                    implementation("com.sparkjava:spark-core:2.9.3")
                    runtimeOnly(kotlin("reflect"))
                }
            }

            val jsMain by getting {
                dependencies {
                    implementation(npm("text-encoding", "0.7.0"))
                    implementation("io.ktor:ktor-client-js:$ktorVersion")
                    implementation(npm("abort-controller", "3.0.0"))
                    implementation(npm("node-fetch", "2.6.0"))
                    implementation(kotlin("stdlib-js"))

                }
            }

            val jsTest by getting {
                dependencies {
                    implementation(kotlin("test-js"))
                }
            }

            val androidMain by getting {
                repositories {
                    mavenCentral()
                    jcenter()
                }

                dependencies {
                    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                }
            }

            val androidTest by getting {
                dependencies {
                    implementation(kotlin("test-junit"))
                    implementation("com.sparkjava:spark-core:2.9.3")
                    runtimeOnly(kotlin("reflect"))
                }
            }

            val desktopMain by creating {
                dependsOn(commonMain)

                dependencies {
                    implementation("com.soywiz.korlibs.klock:klock:$klockVersion")
                    implementation("io.ktor:ktor-client-curl:$ktorVersion")
                }
            }

            val desktopTest by creating {
                dependsOn(commonTest)
            }

            val linuxX64Main by getting {
                dependsOn(desktopMain)

                dependencies {
                    implementation("com.soywiz.korlibs.klock:klock-linuxx64:$klockVersion")
                }
            }

            val linuxX64Test by getting {
                dependsOn(desktopTest)
            }

            val mingwX64Main by getting {
                dependsOn(desktopMain)

                dependencies {
                    implementation("com.soywiz.korlibs.klock:klock-mingwx64:$klockVersion")
                }
            }

            val mingwX64Test by getting {
                dependsOn(desktopTest)
            }

            val macosX64Main by getting {
                dependsOn(desktopMain)

                dependencies {
                    implementation("com.soywiz.korlibs.klock:klock-macosx64:$klockVersion")
                }
            }

            val macosX64Test by getting {
                dependsOn(desktopTest)
            }

            all {
                languageSettings.useExperimentalAnnotation("kotlin.Experimental")
            }
        }
    }
}

signing {
    if (project.hasProperty("signing.keyId")
        && project.hasProperty("signing.password")
        && project.hasProperty("signing.secretKeyRingFile")
    ) {
        sign(publishing.publications)
    }
}

tasks {
    val dokkaHtml by getting(DokkaTask::class) {
        outputDirectory.set(projectDir.resolve("docs"))

        dokkaSourceSets {
            configureEach {
                skipDeprecated.set(true)

                sourceLink {
                    localDirectory.set(file("src"))
                    remoteUrl.set(uri("https://github.com/adamint/spotify-web-api-kotlin/tree/master/src").toURL())
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }

    spotless {
        kotlin {
            target("**/*.kt")
            licenseHeader("/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */")
            ktlint()
        }
    }

    nexusStaging {
        packageGroup = "com.adamratzman"
    }


    val publishAllPublicationsToNexusRepositoryWithTests by registering(Task::class) {
        dependsOn.add(check)
        dependsOn.add("publishAllPublicationsToNexusRepository")
        dependsOn.add(dokkaHtml)
    }
}


fun MavenPublication.setupPom(publicationName: String) {
    artifactId = artifactId.replace("-web", "")
    artifact(dokkaJar.get())

    pom {
        name.set(publicationName)
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
                name.set("MIT License")
                url.set("https://github.com/adamint/spotify-web-api-kotlin/blob/master/LICENSE")
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

