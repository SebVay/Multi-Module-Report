package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.system.SystemCommandLine
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
 * @property systemCommandLine The [SystemCommandLine] instance used to execute shell commands.
 */
internal class GetProjectRootImpl(
    private val systemCommandLine: SystemCommandLine,
) : GetProjectRoot {

    override fun invoke(): Path {
        return Path(systemCommandLine.exec("git rev-parse --show-toplevel").trim())
    }
}
