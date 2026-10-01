plugins {
    alias(libs.plugins.rickmorty.android.library)
}

android {
    namespace = "com.miquelcms.rickmorty.core.analytics"
}

dependencies {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
}
