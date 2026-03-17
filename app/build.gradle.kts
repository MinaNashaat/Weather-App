plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    kotlin("plugin.serialization") version "2.1.10"
}

android {
    namespace = "com.mina.weatherapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.mina.weatherapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.navArgs)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.google.android.gms:play-services-location:21.3.0")

    val room_version = "2.8.4"
    implementation("androidx.room:room-ktx:${room_version}")
    implementation("androidx.room:room-runtime:$room_version")
//    annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    implementation("org.osmdroid:osmdroid-android:6.1.20")


    testImplementation("androidx.test:core-ktx:1.2.0")
    testImplementation("org.robolectric:robolectric:4.16.1")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")

    testImplementation("androidx.arch.core:core-testing:2.2.0")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
//    testImplementation("app.cash.turbine:turbine:1.2.1")
    //mockk
    testImplementation("io.mockk:mockk:1.14.9")
    androidTestImplementation("io.mockk:mockk-android:1.14.9")
    androidTestImplementation("androidx.test:core-ktx:1.2.0")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
}