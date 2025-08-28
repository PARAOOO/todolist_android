import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val localProperties = Properties().apply {
    val localFile = File(rootDir, "local.properties")
    if (localFile.exists()) {
        load(localFile.inputStream())
    }
}

tasks.register("printVersionCode") {
    group = "versioning"
    doLast {
        println(android.defaultConfig.versionCode)
    }
}
tasks.register("printVersionName") {
    group = "versioning"
    doLast {
        println(android.defaultConfig.versionName)
    }
}

android {
    namespace = "com.paraooo.todolist"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.paraooo.todolist"
        minSdk = 26
        targetSdk = 35
        versionCode = 10
        versionName = "1.5.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        try {
            create("release") {
                storeFile = file(System.getenv("KEYSTORE_FILE") ?: localProperties.getProperty("KEYSTORE_FILE"))
                storePassword = System.getenv("KEYSTORE_PASSWORD") ?: localProperties.getProperty("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS") ?: localProperties.getProperty("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD") ?: localProperties.getProperty("KEY_PASSWORD")
            }
        }
        catch (e: Exception) {
            println("Signing config not found, using debug signing config.")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
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
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.kizitonwose.calendar.compose)

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)

    implementation(libs.androidx.glance.appwidget)
    implementation(libs.gson)

    implementation(libs.bottomsheetdialog.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    // only for test, it have to be deleted after test
    implementation(libs.retrofit)

    implementation(libs.converter.gson)

    implementation(libs.okhttp)

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":local"))
    implementation(project(":remote"))
}