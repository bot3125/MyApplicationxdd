plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

configurations.all {
    resolutionStrategy {
        force("androidx.core:core-ktx:1.15.0")
    }
}

android {
    namespace = "com.example.myapplicationxd"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplicationxd"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "35.0.0"
}

dependencies {
    // Retrofit para llamadas HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // AndroidX AppCompat y Coroutines
    implementation("androidx.appcompat:appcompat:1.3.1") // Asegura que tienes AppCompat para `AppCompatActivity`
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0") // lifecycleScope para ViewModels
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2") // Coroutines

    // WorkManager para tareas en segundo plano
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // Dependencias de Compose
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.foundation:foundation")

    // Firebase Messaging para notificaciones push
    implementation("com.google.firebase:firebase-messaging:23.2.1")

    // Otras dependencias de AndroidX y Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Dependencias para pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.01"))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Herramientas de depuración
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
