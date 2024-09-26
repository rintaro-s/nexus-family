plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.lorinta.nexus_family"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lorinta.nexus_family"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

//        testInstrumentationRunnwer = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        vectorDrawables.useSupportLibrary = true
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")
    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.gms:play-services-location:21.0.1") // 位置情報
    implementation ("javax.mail:mail:1.4.7") // JavaMail API
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation ("androidx.activity:activity-ktx:1.6.1")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.compose.runtime:runtime:1.5.1")
//    implementation ('com.sun.mail:android-mail:1.6.7') // メール送信
//    implementation ('com.sun.mail:android-activation:1.6.7') // メール送信の依存関係

}