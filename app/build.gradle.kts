plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.petfinderapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.petfinderapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    val lifecycleVersion = "2.6.2"
    val koinVersion = "3.5.0"
    val paginationVersion = "3.2.1"
    val navigationLibrary = "2.7.6"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    // Navigation lib
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationLibrary")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationLibrary")

    // Unit tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Mockito
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // Pagination library
    implementation("androidx.paging:paging-runtime-ktx:$paginationVersion")
    testImplementation("androidx.paging:paging-common-ktx:$paginationVersion")
    // Encrypted shared preferences
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    //Picasso image loading
    implementation("com.squareup.picasso:picasso:2.8")
    // Koin
    implementation("io.insert-koin:koin-android:$koinVersion")
    // Koin testing tools
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    // Needed JUnit version
    testImplementation("io.insert-koin:koin-test-junit4:$koinVersion")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}