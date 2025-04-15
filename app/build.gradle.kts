plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
//    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")

//    alias(libs.plugins.kotlin.kapt)

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
//    id("androidx.navigation.safeargs.kotlin")

//    id("com.google.dagger.hilt.android")
//    kotlin("plugin.serialization") version "1.9.24"
//    kotlin("plugin.serialization")
}

android {
    namespace = "org.fmm.communitymgmt"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.fmm.communitymgmt"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL",  "\"http://10.0.2.2:8080/\"")
            resValue("string","fmmName", "CommunityMgmtApp")
        }
        debug {
            isDebuggable = true
            buildConfigField("String", "BASE_URL",  "\"http://192.168.1.150:3000/\"")
            resValue("string","fmmName", "[DEBUG] CommunityMgmtAppDebug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    /**
     * Habilita el viewBinding
     */
    buildFeatures {
        viewBinding = rootProject.extra["viewBindingEnabled"] as Boolean
        buildConfig=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Jetpack Security
    implementation(libs.androidx.security.crypto.ktx)

    //NavComponent
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //DaggerHilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //Retrofit
    implementation(libs.retrofit)
//    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //kotlinx-serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
}