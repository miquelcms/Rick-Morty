plugins {
    alias(libs.plugins.rickmorty.android.library)
    alias(libs.plugins.rickmorty.android.compose)
}

android {
    namespace = "com.miquelcms.rickmorty.feature.characters.ui"
}

dependencies {
    implementation(projects.feature.characters.domain)
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.analytics)
}
