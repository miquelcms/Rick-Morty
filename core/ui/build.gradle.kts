plugins {
    alias(libs.plugins.rickmorty.android.library)
    alias(libs.plugins.rickmorty.android.compose)
}

android {
    namespace = "com.miquelcms.rickmorty.core.ui"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.designsystem)
}
