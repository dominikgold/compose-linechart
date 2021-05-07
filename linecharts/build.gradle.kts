plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

group = "com.dominikgold"

android {
    compileSdk = 30
    buildToolsVersion = "30.0.2"

    defaultConfig {
        minSdk = 21
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.composeVersion
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                artifactId = "compose-linechart"
                from(components["release"])
                groupId = "com.dominikgold.compose-linechart"
            }
        }
    }
}

dependencies {
    implementation(Dependencies.kotlinSdk)

    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)

    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composeFoundation)

    testImplementation(Dependencies.jUnit)
    testImplementation(Dependencies.kluent)
    testImplementation(Dependencies.mockitoKotlin)
}