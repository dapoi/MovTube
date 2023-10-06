import com.dapascript.buildsrc.AndroidProjectConfig

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.dapascript.movtube"
    compileSdk = 34

    defaultConfig {
        applicationId = AndroidProjectConfig.applicationId
        minSdk = AndroidProjectConfig.minSdk
        targetSdk = AndroidProjectConfig.targetSdk
        versionCode = AndroidProjectConfig.versionCode
        versionName = AndroidProjectConfig.versionName
        testInstrumentationRunner = AndroidProjectConfig.testInstrumentationRunner
    }

    buildTypes {
        getByName("release") {
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
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    // Core
    api(com.dapascript.buildsrc.Libraries.androidCore)
    api(com.dapascript.buildsrc.Libraries.androidAppCompat)
    api(com.dapascript.buildsrc.Libraries.androidMaterial)
    api(com.dapascript.buildsrc.Libraries.androidConstraintLayout)

    // Testing
    testImplementation(com.dapascript.buildsrc.Libraries.androidArchCore)
    testImplementation(com.dapascript.buildsrc.Libraries.androidKotlinxCoroutinesTest)
    testImplementation(com.dapascript.buildsrc.Libraries.junit)
    testImplementation(com.dapascript.buildsrc.Libraries.mockWebServer)
    testImplementation(com.dapascript.buildsrc.Libraries.mockitoCore)
    testImplementation(com.dapascript.buildsrc.Libraries.mockitoInline)
    androidTestImplementation(com.dapascript.buildsrc.Libraries.androidTestExtJunit)
    androidTestImplementation(com.dapascript.buildsrc.Libraries.androidTestEspressoCore)
    androidTestImplementation(com.dapascript.buildsrc.Libraries.androidTestArchCore)
    androidTestImplementation(com.dapascript.buildsrc.Libraries.androidTestKotlinxCoroutinesTest)

    // Navigation
    implementation(com.dapascript.buildsrc.Libraries.navigationFragmentKtx)

    // Retrofit
    implementation(com.dapascript.buildsrc.Libraries.retrofit)
    implementation(com.dapascript.buildsrc.Libraries.retrofitMoshiConverter)

    // Square
    implementation(com.dapascript.buildsrc.Libraries.okhttp)
    implementation(com.dapascript.buildsrc.Libraries.okhttpLoggingInterceptor)

    // Moshi
    implementation(com.dapascript.buildsrc.Libraries.moshiKotlin)
    kapt(com.dapascript.buildsrc.Libraries.moshiKotlinCodeGen)

    // Coroutines
    implementation(com.dapascript.buildsrc.Libraries.coroutinesCore)
    implementation(com.dapascript.buildsrc.Libraries.coroutinesAndroid)

    // Coroutine lifecycle scopes
    implementation(com.dapascript.buildsrc.Libraries.lifeCycleScopeViewModelKtx)
    implementation(com.dapascript.buildsrc.Libraries.lifeCycleScopeRuntimeKtx)
    implementation(com.dapascript.buildsrc.Libraries.lifeCycleScopeLiveDataKtx)

    // Dagger hilt
    implementation(com.dapascript.buildsrc.Libraries.daggerHilt)
    kapt(com.dapascript.buildsrc.Libraries.daggerHiltCompiler)

    // Appearance
    implementation(com.dapascript.buildsrc.Libraries.glide)
    implementation(com.dapascript.buildsrc.Libraries.shimmer)
    implementation(com.dapascript.buildsrc.Libraries.circleImageView)

    // Room
    implementation(com.dapascript.buildsrc.Libraries.roomRuntime)
    implementation(com.dapascript.buildsrc.Libraries.roomKtx)
    implementation(com.dapascript.buildsrc.Libraries.roomPaging)
    kapt(com.dapascript.buildsrc.Libraries.roomCompiler)

    // API Splash Screen
    implementation(com.dapascript.buildsrc.Libraries.apiSplashScreen)

    // Paging
    implementation(com.dapascript.buildsrc.Libraries.pagingRuntime)

    // Youtube player
    implementation(com.dapascript.buildsrc.Libraries.youtubePlayer)
}