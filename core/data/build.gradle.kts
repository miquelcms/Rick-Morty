plugins {
    alias(libs.plugins.rickmorty.android.library)
}

android {
    namespace = "com.miquelcms.rickmorty.core.data"
}

dependencies {
    implementation(projects.core.domain)
}
