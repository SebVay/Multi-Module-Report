package com.sebastienmartin.danger.report

import com.sebastienmartin.danger.report.di.initGraph
import com.sebastienmartin.danger.report.host.HostType
import com.sebastienmartin.danger.report.internal.Report
import systems.danger.kotlin.models.danger.DangerDSL

/**
 * Generates a GitHub comment with a report of the modules that have been modified in a pull request.
 *
 * @receiver The [DangerDSL] instance, providing access to Danger's functionalities.
 * @param builder A lambda with [ReportConfigBuilder] receiver to configure report generation.
 *             Can be used to customize:
 *             - Report sections and headers
 *             - Module naming and filtering rules
 *             - Comment formatting options
 * @see ReportConfig
 */
public fun DangerDSL.githubModuleReport(
    builder: ReportConfigBuilder.() -> Unit = {},
) {
    getReport(HostType.Github, builder)
        .write()
}

/**
 * Internal function to create and configure a [Report] instance.
 *
 * This function serves as the core logic for generating reports across different host types.
 * It initializes the dependency injection graph with the provided host type and report configuration.
 */
internal fun DangerDSL.getReport(
    hostType: HostType,
    builder: ReportConfigBuilder.() -> Unit,
): Report {
    val isHostCorrect = when (hostType) {
        HostType.Github -> onGitHub
    }

    val reportConfig: ReportConfig = ReportConfigBuilder(isHostCorrect)
        .apply(builder)
        .build()

    return initGraph(hostType, reportConfig).get<Report>()
}
