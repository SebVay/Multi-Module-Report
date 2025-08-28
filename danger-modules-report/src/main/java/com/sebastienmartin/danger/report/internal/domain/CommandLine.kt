package com.sebastienmartin.danger.report.internal.domain

/**
 * Represents a command line interface for executing shell commands.
 */
internal interface CommandLine {
    /**
     * Executes a command in the shell and returns the output.
     *
     * @param command The command to execute.
     * @param arguments A list of arguments to pass to the command. Defaults to an empty list.
     * @return The output of the command as a String.
     */
    fun exec(
        command: String,
        arguments: List<String> = emptyList(),
    ): String
}
