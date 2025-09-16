// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.kapt) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.org.jetbrains.dokka) apply false
}
buildscript {
    dependencies {
        classpath(dependencyNotation = libs.kotlin.dokka)
        classpath(dependencyNotation = libs.kotlin.dokka.gradle.android)
    }
}
subprojects {
    apply(plugin = "org.jetbrains.dokka")
}
true // Needed to make the Suppress annotation work for the plugins block