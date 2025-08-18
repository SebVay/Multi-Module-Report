plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    explicitApi()

    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    compileOnly(files("libs/danger-kotlin.jar"))
    testImplementation(files("libs/danger-kotlin.jar"))

    // JUnit
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.core)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.mockk)
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint(libs.versions.ktlint.get()).editorConfigOverride(
            mapOf(
                "ktlint_standard_function-naming" to "disabled",
                "ktlint_standard_function-expression-body" to "disabled"
            )
        )
        trimTrailingWhitespace()
        endWithNewline()
    }
}

detekt {
    parallel = true
    buildUponDefaultConfig = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("codeQualityCheck") {
    group = "verification"
    description = "Runs Spotless and Detekt"

    dependsOn("spotlessApply", "detekt")
}


val module = moduleInfo.module.get()
group = requireNotNull(module.group)
version = requireNotNull(module.version)

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name = "Danger Report Configuration"
                description = "A library for configuring danger reports."
                inceptionYear = "2025"
            }
        }
    }
}