package com.sebastmar.danger.report

import com.sebastmar.danger.report.di.getReport
import com.sebastmar.danger.report.host.HostType
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
    getReport(HostType.Github, builder).write()
}

// public fun DangerDSL.gitlabModuleReport() { ... }

// public fun DangerDSL.bitbucketModuleReport() { ... }
