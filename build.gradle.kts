// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    //23/05/2025
    alias(libs.plugins.kotlin.kapt) apply false
    //id("com.google.dagger.hilt.android") version "2.51.1" apply false
    alias(libs.plugins.hilt.android) apply false
    //alias(libs.plugins.ksp) apply false

    // Navegación segura SafeArgs
    id("androidx.navigation.safeargs.kotlin") version "2.8.9" apply false


}
val viewBindingEnabled by extra(true)
val dataBindingEnabled by extra(true)