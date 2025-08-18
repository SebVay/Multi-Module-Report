package com.sebast.mar.danger.report.github

import com.sebast.mar.danger.report.DangerReport
import com.sebast.mar.danger.report.ReportConfigBuilder
import com.sebast.mar.danger.report.helper.CommandLineImpl
import com.sebast.mar.danger.report.helper.DangerWrapper
import com.sebast.mar.danger.report.internal.GetFilesImpl
import com.sebast.mar.danger.report.internal.GetModulesImpl
import systems.danger.kotlin.models.danger.DangerDSL

public fun DangerDSL.githubModuleReport(
    block: ReportConfigBuilder.() -> Unit,
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
