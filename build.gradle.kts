import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

plugins {
    id("lt.petuska.npm.publish") version "1.1.2"
    kotlin("multiplatform") version "1.5.31"
    `maven-publish`
    signing
    id("io.codearte.nexus-staging") version "0.30.0"
    id("com.android.library")
    kotlin("plugin.serialization") version "1.5.31"
    id("com.diffplug.spotless") version "5.14.2"
    id("com.moowork.node") version "1.3.1"
    id("org.jetbrains.dokka") version "1.5.0"
    id("kotlin-android-extensions")
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
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
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

    val irOnlyJs = project.hasProperty("irOnly")
    js(if (irOnlyJs) KotlinJsCompilerType.IR else KotlinJsCompilerType.BOTH) {

        mavenPublication {
            setupPom(artifactId)
        }

        browser {
            webpackTask {
                output.globalObject = "this"
                output.libraryTarget = Target.UMD
            }

            testTask {
                useKarma {
                    useChromeHeadless()
                    //useChrome()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }

        /*nodejs {
            testTask {
                useMocha {
                    timeout = "15000"
                }
            }
        }*/

        if (irOnlyJs) binaries.executable()
    }

    // val hostOs = System.getProperty("os.name")
    // val isMainHost = hostOs.contains("mac", true)
    // val isMingwX64 = hostOs.startsWith("Windows")

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
    ios {
        binaries {
            framework {
                baseName = "spotify"
            }
        }

        mavenPublication {
            setupPom(artifactId)
        }
    }
    tvos {
        binaries {
            framework {
                baseName = "spotify"
            }
        }

        mavenPublication {
            setupPom(artifactId)
        }
    }

    // disabled due to lack of coroutine/serialization library support (yet)
    /*watchos {
     binaries {
            framework {
                baseName = "spotify"
            }
        }

        mavenPublication {
            setupPom(artifactId)
        }
    }*/

    publishing {
        if ("local" !in (version as String)) registerPublishing()
    }

    targets {
        val kotlinxDatetimeVersion = "0.3.1"

        sourceSets {
            val serializationVersion = "1.3.0"
            val ktorVersion = "1.6.5"
            val korlibsVersion = "2.2.0"
            val sparkVersion = "2.9.3"
            val androidSpotifyAuthVersion = "1.2.3"
            val androidCryptoVersion = "1.0.0"
            val coroutineMTVersion = "1.5.2-native-mt"

            val commonMain by getting {
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                    implementation("io.ktor:ktor-client-core:$ktorVersion")
                    implementation("com.soywiz.korlibs.krypto:krypto:$korlibsVersion")
                    implementation("com.soywiz.korlibs.korim:korim:$korlibsVersion")

                }
            }

            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
                }
            }

            val commonJvmLikeMain by creating {
                dependsOn(commonMain)

                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
                    implementation("net.sourceforge.streamsupport:android-retrofuture:1.7.3")
                }
            }

            val jvmMain by getting {
                dependsOn(commonJvmLikeMain)
                repositories {
                    mavenCentral()
                }

                dependencies {
                    implementation("io.ktor:ktor-client-cio:$ktorVersion")
                }
            }

            val jvmTest by getting {
                dependencies {
                    implementation(kotlin("test-junit"))
                    implementation("com.sparkjava:spark-core:$sparkVersion")
                    runtimeOnly(kotlin("reflect"))
                }
            }

            val jsMain by getting {
                dependencies {
                    implementation(npm("text-encoding", "0.7.0"))
                    implementation("io.ktor:ktor-client-js:$ktorVersion")
                    implementation(npm("abort-controller", "3.0.0"))
                    implementation(npm("node-fetch", "2.6.1"))
                    implementation(kotlin("stdlib-js"))
                }
            }

            val jsTest by getting {
                dependencies {
                    implementation(kotlin("test-js"))
                }
            }

            val androidMain by getting {
                dependsOn(commonJvmLikeMain)

                repositories {
                    mavenCentral()
                }

                dependencies {
                    api("com.spotify.android:auth:$androidSpotifyAuthVersion")
                    implementation("com.pnikosis:materialish-progress:1.7")
                    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                    implementation("androidx.security:security-crypto:$androidCryptoVersion")
                    implementation("androidx.appcompat:appcompat:1.3.1")
                }
            }

            val androidTest by getting {
                dependencies {
                    implementation(kotlin("test-junit"))
                    implementation("com.sparkjava:spark-core:$sparkVersion")
                    runtimeOnly(kotlin("reflect"))
                }
            }

            val desktopMain by creating {
                dependsOn(commonMain)

                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineMTVersion") {
                        version {
                            strictly(coroutineMTVersion)
                        }
                    }
                    implementation("io.ktor:ktor-client-curl:$ktorVersion")
                    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
                }
            }

            val desktopTest by creating {
                dependsOn(commonTest)
            }

            val linuxX64Main by getting {
                dependsOn(desktopMain)
            }

            val linuxX64Test by getting {
                dependsOn(desktopTest)
            }

            val mingwX64Main by getting {
                dependsOn(desktopMain)
            }

            val mingwX64Test by getting {
                dependsOn(desktopTest)
            }

            val macosX64Main by getting {
                dependsOn(desktopMain)
            }

            val macosX64Test by getting {
                dependsOn(desktopTest)
            }

            val nativeDarwinMain by creating {
                dependsOn(commonMain)

                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineMTVersion") {
                        version {
                            strictly(coroutineMTVersion)
                        }
                    }
                    implementation("io.ktor:ktor-client-ios:$ktorVersion")
                    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
                }
            }

            val nativeDarwinTest by creating {
                dependsOn(commonTest)
            }

            val iosMain by getting {
                dependsOn(nativeDarwinMain)
            }

            val iosTest by getting {
                dependsOn(nativeDarwinTest)
            }

            val tvosMain by getting {
                dependsOn(nativeDarwinMain)
            }

            val tvosTest by getting {
                dependsOn(nativeDarwinTest)
            }

            /* val watchosMain by getting {
            dependsOn(nativeDarwinMain)
        }

        val watchosTest by getting {
            dependsOn(nativeDarwinTest)
        }*/

            all {
                languageSettings.useExperimentalAnnotation("kotlin.Experimental")
            }
        }
    }
}

publishing {
    if ("local" in (version as String)) registerPublishing()
}

signing {
    if (project.hasProperty("SIGNING_KEY")
        && project.hasProperty("SIGNING_PASSWORD")
    ) {
        useInMemoryPgpKeys(
            project.findProperty("SIGNING_KEY") as? String,
            project.findProperty("SIGNING_PASSWORD") as? String
        )
        sign(publishing.publications)
    }
}

npmPublishing {
    repositories {
        repository("npmjs") {
            registry = uri("https://registry.npmjs.org")
            (project.properties["npmauthtoken"] as? String)?.let { authToken = it }
        }
    }
}

tasks {
    /*npmPublishing {
        readme = file("README.MD")

        repositories {
            repository("npmjs") {
                registry = uri("https://registry.npmjs.org")
                (project.properties.get("npmauthtoken") as? String)?.let { authToken = it }
            }
        }

        publications {
            publication("js") {
                bundleKotlinDependencies = true
                shrinkwrapBundledDependencies = true
            }
        }
    }*/

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
    artifactId = artifactId
        .replace("-web", "")
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

fun PublishingExtension.registerPublishing() {
    publications {
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
                val nexusUsername: String? = project.findProperty("NEXUS_USERNAME") as? String
                val nexusPassword: String? = project.findProperty("NEXUS_PASSWORD") as? String
                username = nexusUsername
                password = nexusPassword
            }
        }
    }
}


val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)
