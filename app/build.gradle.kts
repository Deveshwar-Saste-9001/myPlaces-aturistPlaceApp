import org.gradle.internal.impldep.com.amazonaws.PredefinedClientConfigurations.defaultConfig


plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myplaces"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myplaces"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.circleimageview)
    implementation(libs.glide)
    implementation(libs.volley)
    implementation(libs.play.services.location)
    implementation(libs.paperdb)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.ml.natural.language)
    implementation(libs.firebase.ml.natural.language.language.id.model)
    implementation(libs.firebase.ml.natural.language.translate.model)
    implementation(libs.firebase.ml.natural.language.translate)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}