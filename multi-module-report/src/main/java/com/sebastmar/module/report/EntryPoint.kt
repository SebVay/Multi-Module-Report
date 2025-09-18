package com.sebastmar.module.report

import com.sebastmar.module.report.configuration.ConfigurationBuilder
import com.sebastmar.module.report.internal.Configuration
import com.sebastmar.module.report.internal.di.koin.ModuleReportKoinContext.koin
import com.sebastmar.module.report.internal.host.HostType
import com.sebastmar.module.report.system.SystemCommandLine
import com.sebastmar.module.report.system.SystemWrapper
import com.sebastmar.module.report.system.SystemWriter
import org.koin.core.parameter.parametersOf

public typealias BuilderBlock = (ConfigurationBuilder.() -> Unit)

/**
 * Generates a table-based report for the current system and configuration.
 *
 * @param systemCommandLine An interface for executing shell commands.
 * @param systemWrapper An interface for interacting with the underlying system where the pull request is executed.
 * @param systemWriter An interface that defines the writer used by the report.
 * @param configurationBuilder A builder block for constructing the configuration object.
 * @return An instance of Report containing the generated report as a String.
 *
 * Usage:
 * ```
 * githubReport(
 *     systemCommandLine = YourCommandLine(),
 *     systemWrapper = YourSystemWrapper(),
 *     systemWriter = YourWriter()
 * ) {
 *     skipReportKeyword = "SKIP"
 *     reportStrings {
 *         topSection = "Custom Top Section"
 *         bottomSection = "Custom Bottom Section"
 *     }
 * }
 * ```
 *
 * The configuration block allows customization of report sections, headers,
 * module naming rules, and other formatting options.
 */
public fun githubReport(
    systemCommandLine: SystemCommandLine,
    systemWrapper: SystemWrapper,
    systemWriter: SystemWriter<String>,
    configurationBuilder: BuilderBlock = {},
): Report<String> {
    return koin.inject<Report<String>> {
        parametersOf(
            HostType.Github,
            systemCommandLine,
            systemWrapper,
            systemWriter,
            configurationBuilder.buildConfiguration(),
        )
    }.value
}

private fun BuilderBlock.buildConfiguration(): Configuration {
    return ConfigurationBuilder()
        .apply(this)
        .build()
}
