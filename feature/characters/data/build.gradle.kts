plugins {
    alias(libs.plugins.rickmorty.android.library)
}

android {
    namespace = "com.miquelcms.rickmorty.feature.characters.data"
}

dependencies {
    implementation(projects.feature.characters.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}
