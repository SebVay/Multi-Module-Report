plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jacoco)

    // Quality Plugins
    alias(libs.plugins.detekt)

    // Publishing Plugins
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
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

    // DI
    implementation(files("libs/koin-core-jvm-4.1.0.jar"))

    // JUnit
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.core)
    testRuntimeOnly(libs.junit.platform.launcher)

    testImplementation(libs.koinJunit5)
    testImplementation(libs.mockk)
}

detekt {
    parallel = true
    buildUponDefaultConfig = true
    source.from("src/main/kotlin", "src/test/kotlin")
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

// Include Koin in the published Jar
tasks.withType<Jar> {
    from(zipTree("libs/koin-core-jvm-4.1.0.jar"))

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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
                description = "A library for generating visually appealing module reports with Danger-Kotlin."
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

                scm {
                    url = "https://github.com/SebVay/Danger-Module-Report"
                    connection = "scm:git:git://github.com/SebVay/Danger-Module-Report.git"
                    developerConnection = "scm:git:ssh://github.com/SebVay/Danger-Module-Report.git"
                }
            }
        }
    }

    repositories {
        // Avoid dealing with system-dependent .m2 paths (e.g., Windows, Unix) for local development
        maven {
            name = "RootProject"
            url = uri("${rootProject.rootDir}/build/local-maven-repository")
        }
    }
}

signing {
    val armoredSigningKey: String? = System.getenv("SIGNING_PRIVATE_KEY")
    val signingPassword: String? = System.getenv("SIGNING_PASSWORD")

    if (signingPassword != null && armoredSigningKey != null) {
        println("Signing with provided credentials...")
        useInMemoryPgpKeys(armoredSigningKey, signingPassword)
        sign(publishing.publications["mavenJava"])
    }
}
