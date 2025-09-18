package com.sebastmar.multi.module.report.danger.internal

import com.sebastmar.module.report.system.SystemCommandLine
import systems.danger.kotlin.models.danger.DangerDSL

/**
 * Implementation of [SystemCommandLine] that uses the Danger context to execute commands.
 */
internal class DangerCommandLine(
    private val dangerContext: DangerDSL,
) : SystemCommandLine {
    override fun exec(
        command: String,
        arguments: List<String>,
    ): String = dangerContext.utils.exec(command, arguments)
}
