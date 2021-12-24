plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

androidExtensions {
    isExperimental = true
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.sirekanyan.knigopis"
        minSdk = 21
        targetSdk = 31
        versionCode = 31
        versionName = "0.3.4"
        setProperty("archivesBaseName", "$applicationId-$versionName-$versionCode")
        manifestPlaceholders["LOGIN_CALLBACK_SCHEME"] = "e270636c0efc6cad95130113d3bbafc3"
        manifestPlaceholders["LOGIN_CALLBACK_HOST"] = "532b8e7fc54c52b6df5b55181acc241a"
        manifestPlaceholders["LOGIN_CALLBACK_PATH"] = "$versionCode"
        manifestPlaceholders.forEach { (key, value) ->
            buildConfigField("String", key, "\"$value\"")
        }
        vectorDrawables.useSupportLibrary = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // androidx libraries
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.browser:browser:1.2.0")

    // rxjava
    implementation("io.reactivex.rxjava2:rxjava:2.2.19")
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // okhttp
    @Suppress("GradleDependency")
    implementation("com.squareup.okhttp3:logging-interceptor:3.14.9")

    // etc
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.github.bumptech.glide:glide:4.11.0")

    // crash reporting
    implementation("ch.acra:acra-http:5.8.4")
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
            "repository" to "sirekanyan/knigopis"
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
