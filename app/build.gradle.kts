plugins {
    alias(libs.plugins.rickmorty.android.application)
    alias(libs.plugins.rickmorty.android.compose)
}

android {
    namespace = "com.miquelcms.rickmorty"

    defaultConfig {
        applicationId = "com.miquelcms.rickmorty"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
}

dependencies {
    implementation(projects.core.analytics)
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(projects.feature.characters.data)
    implementation(projects.feature.characters.ui)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel)
    testImplementation(projects.core.testing)
    testImplementation(libs.koin.test)
}
