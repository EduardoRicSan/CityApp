plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlin.serialization)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}


android {
    namespace = "com.tablegroup.ualaapptest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tablegroup.ualaapptest"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["MAPS_API_KEY"] = project.findProperty("MAPS_API_KEY") as? String ?: ""
        buildConfigField("String", "WEATHER_API_KEY", "\"${project.findProperty("WEATHER_API_KEY") ?: ""}\"")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/DEPENDENCIES"
            )
        }
    }

    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
            force("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.mockk.android)
    debugImplementation(libs.androidx.ui.tooling)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
    // Para pruebas instrumentadas (UI tests)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.google.hilt.compiler)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.androidx.room.testing)
    //Nav Controller
    implementation(libs.navigation.compose)
    implementation(libs.material.icons.extended)

    implementation(libs.androidx.core.splashscreen)

    //DataStore
    implementation(libs.datastore.preferences)

    //Dagger-hilt
    implementation(libs.hilt.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.serialization)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.dagger.hilt)

    //maps
    implementation(libs.maps.compose)
    implementation(libs.maps.utils)
    implementation(libs.maps.platform)
    implementation(libs.accompanist.permissions)

    //Coil
    implementation(libs.coil.compose)
    implementation(libs.coil3.svg)
    implementation(libs.coil.network.okhttp)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)


    implementation(project(":designSystem"))
    implementation(project(":core"))
    implementation(project(":domain"))
    testImplementation(kotlin("test"))
}