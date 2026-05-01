import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("org.sirekanyan.version-checker")
}

private val appPackageName = property("appPackageName") as String
private val appVersionName = property("appVersionName") as String
private val appVersionCode = property("appVersionCode") as String

base {
    archivesName.set("$appPackageName-$appVersionName-$appVersionCode")
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
        allWarningsAsErrors = true
        freeCompilerArgs.add("-Xannotation-target-all")
    }
}

android {
    namespace = appPackageName
    compileSdk = 36
    defaultConfig {
        applicationId = appPackageName
        minSdk = 23
        //noinspection OldTargetApi
        targetSdk = 35
        versionName = appVersionName
        versionCode = appVersionCode.toInt()
        manifestPlaceholders["LOGIN_CALLBACK_SCHEME"] = "e270636c0efc6cad95130113d3bbafc3"
        manifestPlaceholders["LOGIN_CALLBACK_HOST"] = "532b8e7fc54c52b6df5b55181acc241a"
        manifestPlaceholders["LOGIN_CALLBACK_PATH"] = "$versionCode"
        manifestPlaceholders.forEach { (key, value) ->
            buildConfigField("String", key, "\"$value\"")
        }
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        System.getenv("TEST_TOKEN")?.let {
            testInstrumentationRunnerArguments["testToken"] = it
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }
    flavorDimensions += "store"
    productFlavors {
        create("fdroid") {
            dimension = "store"
            signingConfig = signingConfigs.create("release") {
                storeFile = System.getenv("SIGNING_KEYSTORE_FILE")?.let(::file)
                storePassword = System.getenv("SIGNING_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
            }
        }
        create("play") {
            dimension = "store"
            listOf("ACRA_URI", "ACRA_LOGIN", "ACRA_PASSWORD").forEach { key ->
                buildConfigField("String", key, System.getenv(key)?.let { "\"$it\"" } ?: "null")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    lint {
        warningsAsErrors = true
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    // androidx libraries
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0")
    implementation("androidx.browser:browser:1.10.0")

    // rxjava
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    // retrofit
    //noinspection NewerVersionAvailable
    implementation("com.squareup.retrofit2:retrofit:2.12.0")
    //noinspection NewerVersionAvailable
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.12.0")
    //noinspection NewerVersionAvailable
    implementation("com.squareup.retrofit2:converter-gson:2.12.0")

    // okhttp
    //noinspection NewerVersionAvailable
    implementation("com.squareup.okhttp3:logging-interceptor:3.14.9")

    // etc
    implementation("com.google.android.material:material:1.13.0")
    implementation("com.github.bumptech.glide:glide:5.0.7")

    // crash reporting
    add("playImplementation", "ch.acra:acra-http:5.13.1")

    // tests
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")
}

versionCheckerOptions {
    "com.squareup.okhttp3:logging-interceptor" lessThan "4.0"
    "com.squareup.retrofit2" lessThan "3.0"
}

task("updateReadme") {
    dependsOn("assembleRelease")
    doLast {
        val releaseVariant = android.applicationVariants.first { it.name == "release" }
        val releaseFiles = releaseVariant.outputs.map { it.outputFile }
        val apkFile = releaseFiles.single { it.exists() && it.extension == "apk" }
        val defaultConfig = android.defaultConfig
        val properties = mapOf(
            "apkSize" to "%.2f".format(apkFile.length().toFloat() / 1024 / 1024),
            "applicationId" to defaultConfig.applicationId,
            "versionName" to defaultConfig.versionName,
            "versionCode" to defaultConfig.versionCode?.toString(),
            "minSdkVersion" to defaultConfig.minSdkVersion?.apiLevel?.toString(),
            "targetSdkVersion" to defaultConfig.targetSdkVersion?.apiLevel?.toString(),
            "repository" to "sirekanian/knigopis",
        )
        properties.forEach { (key, value) ->
            if (value.isNullOrBlank()) {
                logger.warn("Readme property '$key' is empty")
            }
        }
        rootProject.file("README.md").printWriter().use { readme ->
            rootProject.file("readme.md").forEachLine { inputLine ->
                readme.appendLine(
                    properties.entries.fold(inputLine) { line, (key, value) ->
                        line.replace("{{$key}}", value.orEmpty())
                    }
                )
            }
        }
    }
}
