package com.sebastmar.danger.report.internal.domain

import java.nio.file.Path
import kotlin.io.path.Path

/**
 * Interface for retrieving the project root directory.
 */
internal interface GetProjectRoot {
    operator fun invoke(): Path
}

/**
 * Implementation of [GetProjectRoot] that uses the `git rev-parse --show-toplevel` command
 * to determine the project's root directory.
 *
 * @property commandLine The [CommandLine] instance used to execute shell commands.
 */
internal class GetProjectRootImpl(
    private val commandLine: CommandLine,
) : GetProjectRoot {

    override fun invoke(): Path {
        return Path(commandLine.exec("git rev-parse --show-toplevel").trim())
    }
}
