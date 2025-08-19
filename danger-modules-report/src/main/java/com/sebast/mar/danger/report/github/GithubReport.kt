package com.sebast.mar.danger.report.github

import com.sebast.mar.danger.report.DangerReport
import com.sebast.mar.danger.report.ReportConfigBuilder
import com.sebast.mar.danger.report.interceptor.ModuleInterceptor
import com.sebast.mar.danger.report.interceptor.helper.CommandLineImpl
import com.sebast.mar.danger.report.interceptor.helper.DangerWrapper
import com.sebast.mar.danger.report.internal.GetFilesImpl
import com.sebast.mar.danger.report.internal.GetModulesImpl
import systems.danger.kotlin.models.danger.DangerDSL

/**
 * Generates a GitHub comment with a report of the modules that have been modified in a pull request.
 *
 * This function is designed to be used within a Dangerfile. It analyzes the changes in the pull request,
 * identifies the modified modules, and posts a comment on GitHub summarizing these changes.
 *
 * The report can be customized using the [block] parameter, which provides a [ReportConfigBuilder]
 * to configure aspects like module naming, inclusion/exclusion rules, and comment formatting.
 *
 * **Usage:**
 * ```kotlin
 * // In your Dangerfile.df.kts
 * githubModuleReport {
 *     // Optional: Customize the report configuration
 *     moduleName { modulePath ->
 *         // Logic to determine the display name for a module based on its path
 *         modulePath.removePrefix(":").replace(":", " > ")
 *     }
 *     // Add more configurations as needed
 * }
 * ```
 *
 * @receiver The [DangerDSL] instance, providing access to Danger's functionalities.
 * @param block A lambda with a [ReportConfigBuilder] receiver to configure the report generation.
 * @throws IllegalStateException if this function is called outside of a GitHub environment
 */
public fun DangerDSL.githubModuleReport(
    block: ReportConfigBuilder.() -> Unit = {},
) {
    require(onGitHub) { "githubModuleReport can be used only in a GitHub environment." }

    val dangerWrapper = DangerWrapper(this)
    val commandLine = CommandLineImpl(this)
    val reportConfig = ReportConfigBuilder().apply(block).build()

    val getModules = GetModulesImpl(
        danger = dangerWrapper,
        getFiles = GetFilesImpl(commandLine),
        moduleInterceptor = reportConfig.moduleInterceptor,
    )

    val reportWriter = GithubReportWriter(
        reportConfig = reportConfig,
        getModules = getModules,
    )

    DangerReport(reportWriter = reportWriter)
        .generateReport()
}

internal fun DangerDSL.test() {
    githubModuleReport {
        writeSections = false
        moduleInterceptor = ModuleInterceptor { module ->
            module.takeIf { it.isFallback }?.copy(name = "Other sources") ?: module
        }
    }
}
