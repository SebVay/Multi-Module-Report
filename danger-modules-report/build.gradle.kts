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

    withJavadocJar()
    withSourcesJar()
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

    dependsOn("spotlessCheck", "detektMain")
}


val module = moduleInfo.module.get()
group = requireNotNull(module.group)
version = requireNotNull(module.version)

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name = "Danger Modules Report"
                url = "https://github.com/SebVay/Danger-Module-Report/"
                description = "A library for generating visually appealing danger module reports."
                inceptionYear = "2025"

                licenses {
                    license {
                        name = "MIT License"
                        url = "https://github.com/SebVay/Danger-Module-Report/blob/main/LICENSE"
                    }
                }

                developers {
                    developer {
                        id = "SebVay"
                        name = "Sébastien Martin"
                        email = "sebast.mar@gmail.com"
                    }
                }
            }
        }
    }

    repositories {
        // Publishing to root project's build directory allows the Dangerfile scripts
        // to reference the compiled artifact locally during the Danger script execution,
        // avoiding the need for remote publishing or dealing with system dependant .m2 paths (ie. Windows, Unix)
        maven {
            name = "RootProject"
            url = uri("${rootProject.rootDir}/build/local-maven-repository")
        }
    }
}