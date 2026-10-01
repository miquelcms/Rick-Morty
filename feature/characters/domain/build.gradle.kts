plugins {
    alias(libs.plugins.rickmorty.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}
