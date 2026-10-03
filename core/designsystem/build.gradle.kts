plugins {
    alias(libs.plugins.rickmorty.android.library)
    alias(libs.plugins.rickmorty.android.compose)
}

android {
    namespace = "com.miquelcms.rickmorty.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.material3)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
