import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.canbe.contactbackup"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.canbe.contactbackup"
        minSdk = 24
        targetSdk = 35
        versionCode = 250702
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        val properties = Properties().apply {
            load(FileInputStream("${rootDir}/local.properties"))
        }

        create("release") {
            storeFile = file("../keystore/cbKeystore.jks")
            keyAlias = "${properties["keyAlias"]}"
            storePassword = "${properties["storePassword"]}"
            keyPassword = "${properties["keyPassword"]}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    // Core Android 기능
    implementation(libs.androidx.core.ktx)
    // Lifecycle + Coroutine
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Compose 관련
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // 테스트
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Compose navigation: rememberNavController()
    implementation(libs.androidx.navigation.compose)
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    // Timber
    implementation(libs.timber)
    // Coil
    implementation(libs.coil.compose)
    // Gson
    implementation(libs.gson)
    // Module
    implementation(project(":feat-webview"))
}