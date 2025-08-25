package com.sebastienmartin.danger.report.github

import com.sebastienmartin.danger.report.DangerReport
import com.sebastienmartin.danger.report.Report
import com.sebastienmartin.danger.report.ReportBuilder
import com.sebastienmartin.danger.report.ReportConfigBuilder
import com.sebastienmartin.danger.report.internal.GetFiles
import com.sebastienmartin.danger.report.internal.GetFilesImpl
import com.sebastienmartin.danger.report.internal.GetModules
import com.sebastienmartin.danger.report.internal.GetModulesImpl
import com.sebastienmartin.danger.report.internal.GetPullRequest
import com.sebastienmartin.danger.report.internal.GetPullRequestImpl
import com.sebastienmartin.danger.report.internal.SkipReportImpl
import com.sebastienmartin.danger.report.internal.helper.CommandLine
import com.sebastienmartin.danger.report.internal.helper.CommandLineImpl
import com.sebastienmartin.danger.report.internal.helper.DangerWrapper
import com.sebastienmartin.danger.report.internal.helper.DangerWriter
import com.sebastienmartin.danger.report.internal.helper.DangerWriterImpl
import systems.danger.kotlin.models.danger.DangerDSL

/**
 * Generates a GitHub comment with a report of the modules that have been modified in a pull request.
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
    val dangerWriter: DangerWriter = DangerWriterImpl()
    val dangerWrapper = DangerWrapper(this)
    val commandLine: CommandLine = CommandLineImpl(this)
    val reportConfig = ReportConfigBuilder(isHostCorrect = onGitHub).apply(block).build()

    val getFiles: GetFiles = GetFilesImpl(
        commandLine = commandLine,
        runShortStatCommand = reportConfig.showLineIndicators,
    )

    val getModules: GetModules = GetModulesImpl(
        danger = dangerWrapper,
        getFiles = getFiles,
        modulesInterceptor = reportConfig.modulesInterceptor,
    )

    val getPullRequest: GetPullRequest = GetPullRequestImpl(
        danger = dangerWrapper,
        getModules = getModules,
    )

    val reportBuilder: ReportBuilder = GithubReportBuilder(
        reportConfig = reportConfig,
        getPullRequest = getPullRequest,
    )

    val dangerReport: Report = DangerReport(
        reportBuilder = reportBuilder,
        dangerWriter = dangerWriter,
        skipReport = SkipReportImpl(dangerWrapper, reportConfig),
    )

    dangerReport.write()
}
