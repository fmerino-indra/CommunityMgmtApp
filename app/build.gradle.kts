plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // 23/05/2025
//    id("kotlin-kapt")
    alias(libs.plugins.kotlin.kapt)
//    alias(libs.plugins.ksp)

    id("androidx.navigation.safeargs.kotlin")

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)

    //23/05/2025
    //id("kotlin-parcelize")
    //kotlin("plugin.parcelize") version "1.9.20"
}

android {
    namespace = "org.fmm.communitymgmt"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.fmm.communitymgmt"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["auth0Domain"]= "@string/com_auth0_domain"
        manifestPlaceholders["auth0Scheme"]= applicationId!!
        manifestPlaceholders["auth0Host"]= "@string/com_auth0_host"
        manifestPlaceholders["appAuthRedirectScheme"]= "com.googleusercontent.apps.828614483216-ql47ddnq773mt6ge92ak0f1idkmlsb6d"

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
//            buildConfigField("String", "BASE_URL",  "\"http://192.168.1.150:3000/\"")
            buildConfigField("String", "BASE_URL",  "\"http://192.168.1.150:8080/api/\"")
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
     * Habilita el viewBinding y el dataBinding
     */
    buildFeatures {
        viewBinding = rootProject.extra["viewBindingEnabled"] as Boolean
//        dataBinding = rootProject.extra["dataBindingEnabled"] as Boolean
        dataBinding = true
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
//    ksp(libs.hilt.android.compiler)


    //Retrofit
    implementation(libs.retrofit)
//    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //kotlinx-serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    //kotlinx-datetime: LocalDateComponentSerializer
    implementation(libs.kotlinx.datetime)

    //AppAuth: Esta es de más bajo nivel o mejor, más manual
//    implementation(libs.appauth)

    //Oauth 2
//    implementation(libs.auth0)
    implementation(libs.androidx.biometric)

    // Credential Manager (Core)
    implementation(libs.androidx.credentials)

    // Google ID Token Credential (extensión para Google Sign In)
    implementation(libs.androidx.credentials.play.services.auth)

    // Opcional (si se quiere usar el botón federado de Google)
    implementation(libs.googleid)

    // Antiguo GoogleSignIn
    implementation(libs.play.services.auth)

    // JWT
    //implementation(libs.jjwt.root)
    //implementation("io.jsonwebtoken:jjwt-root:0.12.6")
    implementation(libs.fusionauth.jwt)

    // Coil (async images)
    implementation(libs.coil)

    // Swipe Refresh Layout para RecyclerView
    implementation (libs.androidx.swiperefreshlayout)

    // QR
    implementation (libs.zxing.android.embedded)

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")

    implementation(libs.qr.util)
    implementation(libs.cryptography.util)
}

tasks.register("listConfigurations") {
    doLast {
        configurations.filter {
            it.isCanBeResolved
        }.forEach { config ->
            println("Name: ${config.name}, canBeResolved: ${config.isCanBeResolved}, canBeConsumed: ${config.isCanBeConsumed}")
        }
    }
}
configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
    }
}