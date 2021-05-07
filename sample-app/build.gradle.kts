plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.2"

    defaultConfig {
        applicationId = "com.dominikgold.compose_linechart.sample"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.composeVersion
    }
}

dependencies {

    implementation(Dependencies.androidXCore)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.materialComponents)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.activityCompose)
}