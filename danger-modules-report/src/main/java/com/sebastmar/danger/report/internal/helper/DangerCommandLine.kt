package com.sebastmar.danger.report.internal.helper

import com.sebastmar.danger.report.internal.domain.CommandLine
import systems.danger.kotlin.models.danger.DangerDSL

/**
 * Implementation of [CommandLine] that uses the Danger context to execute commands.
 */
internal class DangerCommandLine(
    private val dangerContext: DangerDSL,
) : CommandLine {
    override fun exec(
        command: String,
        arguments: List<String>,
    ): String = dangerContext.utils.exec(command, arguments)
}
