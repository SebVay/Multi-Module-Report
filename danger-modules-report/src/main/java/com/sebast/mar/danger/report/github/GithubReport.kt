package com.sebast.mar.danger.report.github

import com.sebast.mar.danger.report.DangerReport
import com.sebast.mar.danger.report.ReportConfigBuilder
import com.sebast.mar.danger.report.internal.GetFilesImpl
import com.sebast.mar.danger.report.internal.GetModulesImpl
import com.sebast.mar.danger.report.internal.GetPullRequestImpl
import com.sebast.mar.danger.report.internal.helper.CommandLineImpl
import com.sebast.mar.danger.report.internal.helper.DangerWrapper
import com.sebast.mar.danger.report.internal.writer.DangerWriterImpl
import systems.danger.kotlin.models.danger.DangerDSL

/**
 * Generates a GitHub comment with a report of the modules that have been modified in a pull request.
 *
 * This function orchestrates the report generation by:
 * 1. Creating wrappers for Danger context and command line operations
 * 2. Building report configuration from provided options
 * 3. Setting up file, module and pull request retrievers
 * 4. Initializing the report writer
 * 5. Generating and writing the final report
 *
 * @receiver The [DangerDSL] instance, providing access to Danger's functionalities.
 * @param block A lambda with [ReportConfigBuilder] receiver to configure report generation.
 *             Can be used to customize:
 *             - Report sections and headers
 *             - Module naming and filtering rules
 *             - Comment formatting options
 */
public fun DangerDSL.githubModuleReport(
    block: ReportConfigBuilder.() -> Unit = {},
) {
    val dangerWriter = DangerWriterImpl()
    val dangerWrapper = DangerWrapper(this)
    val commandLine = CommandLineImpl(this)
    val reportConfig = ReportConfigBuilder(isHostCorrect = onGitHub).apply(block).build()

    val getFiles = GetFilesImpl(
        commandLine = commandLine,
        runShortStatCommand = reportConfig.showLineIndicators,
    )

    val getModules = GetModulesImpl(
        danger = dangerWrapper,
        getFiles = getFiles,
        modulesInterceptor = reportConfig.modulesInterceptor,
    )

    val getPullRequest = GetPullRequestImpl(
        danger = dangerWrapper,
        getModules = getModules,
    )

    val reportWriter = GithubReportBuilder(
        reportConfig = reportConfig,
        getPullRequest = getPullRequest,
    )

    DangerReport(
        reportBuilder = reportWriter,
        dangerWriter = dangerWriter,
    )
        .writeReport()
}
