package com.sebastmar.multi.module.report.danger

import com.sebastmar.module.report.BuilderBlock
import com.sebastmar.module.report.configuration.ConfigurationBuilder
import com.sebastmar.module.report.githubReport
import com.sebastmar.multi.module.report.danger.internal.DangerCommandLine
import com.sebastmar.multi.module.report.danger.internal.DangerWrapper
import com.sebastmar.multi.module.report.danger.internal.DangerWriter
import systems.danger.kotlin.models.danger.DangerDSL

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
public fun DangerDSL.githubModuleReport(builder: BuilderBlock = {}) {
    githubReport(
        systemCommandLine = DangerCommandLine(this),
        systemWrapper = DangerWrapper(this),
        systemWriter = DangerWriter(),
        configurationBuilder = builder,
    ).write()
}
