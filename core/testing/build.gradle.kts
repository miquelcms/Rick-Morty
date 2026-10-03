plugins {
    alias(libs.plugins.rickmorty.android.library)
}

android {
    namespace = "com.miquelcms.rickmorty.core.testing"
}

dependencies {
    api(projects.core.analytics)

    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
    api(libs.okhttp.mockwebserver)
    api(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.converter.kotlinx.serialization)
}
