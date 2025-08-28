plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.spotless)
}

spotless {
    kotlin {
        target(
            "**/*.kt",
            "**/*.kts",
        )
        ktlint(libs.versions.ktlint.get()).editorConfigOverride(
            mapOf(
                "ktlint_standard_function-naming" to "disabled",
                "ktlint_standard_function-expression-body" to "disabled",
            ),
        )
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.register("codeQualityCheck") {
    group = "verification"
    description = "Runs Spotless and Detekt"
}.configureOnProjectsEvaluated {
    dependsOn("spotlessCheck")
    dependsOn(subprojects.mapNotNull { it.tasks.findByName("detektMain") })
}

/**
 * Configures a [Task] after all projects have been evaluated.
 * This is useful for tasks that depend on other tasks or configurations that are not available
 * during the initial project evaluation.
 *
 * @param block The configuration block to be applied to the task.
 */
private fun TaskProvider<Task>.configureOnProjectsEvaluated(block: Task.() -> Unit) {
    gradle.projectsEvaluated {
        this@configureOnProjectsEvaluated.configure(block)
    }
}
