@file:Suppress("UnstableApiUsage")

import com.fasterxml.jackson.databind.json.JsonMapper
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

plugins {
    kotlin("multiplatform")
    `maven-publish`
    signing
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.diffplug.spotless") version "6.21.0"
    id("com.moowork.node") version "1.3.1"
    id("org.jetbrains.dokka") version "1.9.0"
}

repositories {
    google()
    mavenCentral()
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:") // resolved in settings.gradle.kts
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:") // resolved in settings.gradle.kts
    }
}

// --- spotify-web-api-kotlin info ---
val libraryVersion: String = System.getenv("SPOTIFY_API_PUBLISH_VERSION") ?: "0.0.0.SNAPSHOT"

// Publishing credentials (environment variable)
val nexusUsername: String? = System.getenv("NEXUS_USERNAME")
val nexusPassword: String? = System.getenv("NEXUS_PASSWORD")

group = "com.adamratzman"
version = libraryVersion


android {
    namespace = "com.adamratzman.spotify"
    compileSdk = 30
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources.excludes.add("META-INF/*.md") // needed to prevent android compilation errors
    }
    defaultConfig {
        minSdk = 23
        setCompileSdkVersion(30)
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
    sourceSets["main"].setRoot("src/androidMain")
    sourceSets["test"].setRoot("src/androidUnitTest")
}

// invoked in kotlin closure, needs to be registered before
val dokkaJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "spotify-web-api-kotlin generated documentation"
    from(tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
}

kotlin {
    explicitApiWarning()
    jvmToolchain(17)

    androidTarget {
        compilations.all { kotlinOptions.jvmTarget = "17" }

        mavenPublication { setupPom(artifactId) }

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

        mavenPublication { setupPom(artifactId) }
    }

    js(KotlinJsCompilerType.IR) {
        mavenPublication { setupPom(artifactId) }

        browser {
            webpackTask {
                output.globalObject = "this"
                output.libraryTarget = Target.UMD
            }

            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport { isEnabled = true }
                }
            }
        }

        binaries.executable()
    }

    macosX64 {
        mavenPublication { setupPom(artifactId) }
    }

    linuxX64 {
        mavenPublication { setupPom(artifactId) }
    }

    mingwX64 {
        mavenPublication { setupPom(artifactId) }
    }

    iosX64 {
        binaries { framework { baseName = "spotify" } }
        mavenPublication { setupPom(artifactId) }
    }

    iosArm64 {
        binaries { framework { baseName = "spotify" } }
        mavenPublication { setupPom(artifactId) }
    }

    iosSimulatorArm64 {
        binaries { framework { baseName = "spotify" } }
        mavenPublication { setupPom(artifactId) }
    }

    // !! unable to include currently due to korlibs not being available !!
    /*
    tvos {
        binaries { framework { baseName = "spotify" } }

        mavenPublication { setupPom(artifactId) }
    }

    watchos {
        binaries { framework { baseName = "spotify" } }

        mavenPublication { setupPom(artifactId) }
    }*/

    applyDefaultHierarchyTemplate()

    sourceSets {
        val kotlinxDatetimeVersion: String by project
        val kotlinxSerializationVersion: String by project
        val kotlinxCoroutinesVersion: String by project
        val ktorVersion: String by project

        val sparkVersion: String by project
        val korlibsVersion: String by project

        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("com.soywiz.korlibs.krypto:krypto:$korlibsVersion")
                implementation("com.soywiz.korlibs.korim:korim:$korlibsVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
            }
        }

        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val commonJvmLikeMain by creating {
            dependsOn(commonMain.get())

            dependencies {
                implementation("net.sourceforge.streamsupport:android-retrofuture:1.7.3")
            }
        }

        val commonJvmLikeTest by creating {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("com.sparkjava:spark-core:$sparkVersion")
                runtimeOnly(kotlin("reflect"))
            }
        }

        val commonNonJvmTargetsTest by creating {
            dependsOn(commonTest.get())
        }

        jvmMain {
            dependsOn(commonJvmLikeMain)

            repositories {
                mavenCentral()
            }

            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }

        jvmTest.get().dependsOn(commonJvmLikeTest)

        jsMain {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation(kotlin("stdlib-js"))
            }
        }

        jsTest {
            dependsOn(commonNonJvmTargetsTest)

            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        androidMain {
            dependsOn(commonJvmLikeMain)

            repositories {
                mavenCentral()
            }

            dependencies {
                val androidSpotifyAuthVersion: String by project
                val androidCryptoVersion: String by project
                val androidxCompatVersion: String by project

                api("com.spotify.android:auth:$androidSpotifyAuthVersion")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("androidx.security:security-crypto:$androidCryptoVersion")
                implementation("androidx.appcompat:appcompat:$androidxCompatVersion")
            }
        }

        val androidUnitTest by getting {
            dependsOn(commonJvmLikeTest)
        }

        // desktop targets
        // as kotlin/native, they require special ktor versions
        val desktopMain by creating {
            dependsOn(commonMain.get())

            dependencies {
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
            }
        }

        linuxMain.get().dependsOn(desktopMain)
        mingwMain.get().dependsOn(desktopMain)
        macosMain.get().dependsOn(desktopMain)

        val desktopTest by creating { dependsOn(commonNonJvmTargetsTest) }
        linuxTest.get().dependsOn(desktopTest)
        mingwTest.get().dependsOn(desktopTest)
        macosTest.get().dependsOn(desktopTest)

        // darwin targets

        val nativeDarwinMain by creating {
            dependsOn(commonMain.get())

            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }

        val nativeDarwinTest by creating { dependsOn(commonNonJvmTargetsTest) }

        iosMain.get().dependsOn(nativeDarwinMain)
        iosTest.get().dependsOn(nativeDarwinTest)

        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
        }
    }

    publishing {
        registerPublishing()
    }
}

tasks {
    dokkaHtml {
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
            licenseHeader("/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2023; Original author: Adam Ratzman */")
            ktlint()
        }
    }


    val publishAllPublicationsToNexusRepositoryWithTests by registering(Task::class) {
        dependsOn.add(check)
        dependsOn.add("publishAllPublicationsToNexusRepository")
        dependsOn.add(dokkaHtml)
    }

    withType<Test> {
        testLogging {
            showStandardStreams = true
        }
    }

    val packForXcode by creating(Sync::class) {
        group = "build"
        val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
        val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
        val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
        val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
        inputs.property("mode", mode)
        dependsOn(framework.linkTask)
        val targetDir = File(layout.buildDirectory.asFile.get(), "xcode-frameworks")
        from({ framework.outputDirectory })
        into(targetDir)
    }
    getByName("build").dependsOn(packForXcode)
}

val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}


fun MavenPublication.setupPom(publicationName: String) {
    artifactId = artifactId.replace("-web", "")
    artifact(dokkaJar.get()) // add javadocs to publication

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


// --- Publishing ---

fun PublishingExtension.registerPublishing() {
    publications {
        val kotlinMultiplatform by getting(MavenPublication::class) {
            artifactId = "spotify-api-kotlin-core"
            setupPom(artifactId)
        }
    }

    repositories {
        maven {
            name = "nexus"

            // Publishing locations
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"

            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
    }
}

// --- Signing ---
val signingKey = project.findProperty("SIGNING_KEY") as? String
val signingPassword = project.findProperty("SIGNING_PASSWORD") as? String

signing {
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(
            project.findProperty("SIGNING_KEY") as? String,
            project.findProperty("SIGNING_PASSWORD") as? String
        )
        sign(publishing.publications)
    }
}

// Test tasks
tasks.register("updateNonJvmTestFakes") {
    if (System.getenv("SPOTIFY_TOKEN_STRING") == null
        || System.getenv("SHOULD_RECACHE_RESPONSES")?.toBoolean() != true
    ) {
        return@register
    }

    dependsOn("jvmTest")
    val responseCacheDir =
        System.getenv("RESPONSE_CACHE_DIR")?.let { File(it) }
            ?: throw IllegalArgumentException("No response cache directory provided")
    val commonTestResourcesSource = projectDir.resolve("src/commonTest/resources")
    if (!commonTestResourcesSource.exists()) commonTestResourcesSource.mkdir()

    val commonTestResourceFileToSet = commonTestResourcesSource.resolve("cached_responses.json")

    if (commonTestResourceFileToSet.exists()) commonTestResourceFileToSet.delete()
    commonTestResourceFileToSet.createNewFile()

    val testToOrderedResponseMap: Map<String, List<String>> = responseCacheDir.walk()
        .filter { it.isFile && it.name.matches("http_request_\\d+.txt".toRegex()) }
        .groupBy { "${it.parentFile.parentFile.name}.${it.parentFile.name}" }
        .map { (key, group) -> key to group.sorted().map { it.readText() } }
        .toMap()

    val jsonLiteral = JsonMapper().writeValueAsString(testToOrderedResponseMap)
    commonTestResourceFileToSet.writeText(jsonLiteral)
    println(commonTestResourceFileToSet.absolutePath)
}