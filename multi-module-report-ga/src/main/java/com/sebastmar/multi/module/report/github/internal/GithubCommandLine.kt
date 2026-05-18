package com.sebastmar.multi.module.report.github.internal

import com.sebastmar.module.report.system.SystemCommandLine
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Implementation of [SystemCommandLine] that executes commands on the local machine.
 */
internal class GithubCommandLine : SystemCommandLine {
    override fun exec(
        command: String,
        arguments: List<String>,
    ): String {
        val fullCommand = buildList {
            addAll(command.split(" ").filter { it.isNotBlank() })
            addAll(arguments)
        }

        val process = ProcessBuilder(fullCommand)
            .redirectErrorStream(true)
            .start()

        val output = BufferedReader(InputStreamReader(process.inputStream)).readText()

        process.waitFor().also {
            println("Exit code for $fullCommand : $it")
        }

        return output
    }
}
