plugins {
    alias(libs.plugins.rickmorty.jvm.library)
}

dependencies {
    implementation(projects.core.domain)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
