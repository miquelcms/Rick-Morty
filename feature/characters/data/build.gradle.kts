plugins {
    alias(libs.plugins.rickmorty.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.miquelcms.rickmorty.feature.characters.data"
}

dependencies {
    implementation(projects.feature.characters.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.analytics)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.retrofit.converter.kotlinx.serialization)
}
