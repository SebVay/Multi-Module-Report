package com.sebastmar.multi.module.report.github

import com.sebastmar.module.report.BuilderBlock
import com.sebastmar.module.report.configuration.ConfigurationBuilder
import com.sebastmar.module.report.githubReport
import com.sebastmar.multi.module.report.github.internal.GithubCommandLine
import com.sebastmar.multi.module.report.github.internal.GithubWrapper
import com.sebastmar.multi.module.report.github.internal.GithubWriter
import com.sebastmar.multi.module.report.github.internal.GithubClient

/**
 * Generates a GitHub comment with a report of the modules that have been modified in a pull request.
 *
 * @param builder A lambda with [ConfigurationBuilder] receiver to configure report generation.
 *             Can be used to customize:
 *             - Report sections and headers
 *             - Module naming and filtering rules
 *             - Comment formatting options
 * @see ConfigurationBuilder
 */
public fun githubModuleReport(builder: BuilderBlock = {}) {
    val cmd = GithubCommandLine()
    val client = GithubClient(cmd)
    githubReport(
        systemCommandLine = cmd,
        systemWrapper = GithubWrapper(client),
        systemWriter = GithubWriter(client),
        configurationBuilder = builder,
    ).write()
}
