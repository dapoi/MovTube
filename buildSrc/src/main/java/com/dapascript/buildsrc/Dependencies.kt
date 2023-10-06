package com.dapascript.buildsrc

object AndroidProjectConfig {
    const val applicationId = "com.dapascript.movtube"
    const val minSdk = 24
    const val targetSdk = 34
    const val versionCode = 1
    const val versionName = "1.0"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object Libraries {
    // Core
    const val androidCore = "androidx.core:core-ktx:1.12.0"
    const val androidAppCompat = "androidx.appcompat:appcompat:1.6.1"
    const val androidMaterial = "com.google.android.material:material:1.9.0"
    const val androidConstraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"

    // Testing
    const val androidArchCore = "androidx.arch.core:core-testing:2.2.0"
    const val androidKotlinxCoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1"
    const val junit = "junit:junit:4.13.2"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:4.11.0"
    const val mockitoCore = "org.mockito:mockito-core:3.12.4"
    const val mockitoInline = "org.mockito:mockito-inline:3.12.4"
    const val androidTestExtJunit = "androidx.test.ext:junit:1.1.5"
    const val androidTestEspressoCore = "androidx.test.espresso:espresso-core:3.5.1"
    const val androidTestArchCore = "androidx.arch.core:core-testing:2.2.0"
    const val androidTestKotlinxCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1"

    // Navigation
    private const val navigationVersion = "2.7.3"
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:$navigationVersion"

    // Retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:2.9.0"

    // Square
    const val okhttp = "com.squareup.okhttp3:okhttp:4.11.0"
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.11.0"

    // Moshi
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:1.14.0"
    const val moshiKotlinCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:1.14.0"

    // Coroutines
    private const val coroutinesVersion = "1.7.1"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"

    // Coroutine lifecycle scopes
    private const val lifeCycleScopeVersion = "2.6.2"
    const val lifeCycleScopeViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleScopeVersion"
    const val lifeCycleScopeRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleScopeVersion"
    const val lifeCycleScopeLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifeCycleScopeVersion"

    // Dagger hilt
    private const val daggerHiltVersion = "2.44"
    const val daggerHilt = "com.google.dagger:hilt-android:$daggerHiltVersion"
    const val daggerHiltCompiler = "com.google.dagger:hilt-android-compiler:$daggerHiltVersion"

    // Appearance
    const val glide = "com.github.bumptech.glide:glide:4.14.2"
    const val shimmer = "com.facebook.shimmer:shimmer:0.5.0"
    const val circleImageView = "de.hdodenhof:circleimageview:3.1.0"

    // Room
    private const val roomVersion = "2.4.0"
    const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
    const val roomKtx = "androidx.room:room-ktx:$roomVersion"
    const val roomPaging = "androidx.room:room-paging:$roomVersion"
    const val roomCompiler = "androidx.room:room-compiler:$roomVersion"

    // API Splash Screen
    const val apiSplashScreen = "androidx.core:core-splashscreen:1.0.1"

    // Paging
    const val pagingRuntime = "androidx.paging:paging-runtime-ktx:3.2.1"

    // Youtube player
    const val youtubePlayer = "com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0"

}