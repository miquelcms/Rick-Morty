import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

private val JAVA_VERSION = JavaVersion.VERSION_17
private val JVM_TARGET = JvmTarget.JVM_17

internal fun Project.configureAndroid(commonExtension: CommonExtension) {
    commonExtension.apply {
        compileSdk { version = release(libs.versionInt("compileSdk")) }
        defaultConfig.minSdk { version = release(libs.versionInt("minSdk")) }
        compileOptions.apply {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
}

internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JAVA_VERSION
        targetCompatibility = JAVA_VERSION
    }
    extensions.configure<KotlinJvmProjectExtension> {
        compilerOptions.jvmTarget.set(JVM_TARGET)
    }
}
