package com.sebastmar.module.report

import com.sebastmar.module.report.configuration.ConfigurationBuilder
import com.sebastmar.module.report.internal.Configuration
import com.sebastmar.module.report.internal.di.koin.getReport
import com.sebastmar.module.report.internal.host.HostType
import systems.danger.kotlin.models.danger.DangerDSL

internal typealias BuilderBlock = (ConfigurationBuilder.() -> Unit)

/**
 * Generates a GitHub comment with a report of the modules that have been modified in a pull request.
 *
 * @receiver The [DangerDSL] instance, providing access to Danger's functionalities.
 * @param builder A lambda with [ConfigurationBuilder] receiver to configure report generation.
 *             Can be used to customize:
 *             - Report sections and headers
 *             - Module naming and filtering rules
 *             - Comment formatting options
 * @see ConfigurationBuilder
 */
public fun DangerDSL.githubModuleReport(
    builder: BuilderBlock = {},
) {
    getReport(HostType.Github, builder.buildConfiguration())
        .write()
}

private fun BuilderBlock.buildConfiguration(): Configuration {
    return ConfigurationBuilder()
        .apply(this)
        .build()
}

// public fun githubActionModuleReport() { ... }
// public fun DangerDSL.gitlabModuleReport() { ... }
// public fun DangerDSL.bitbucketModuleReport() { ... }
