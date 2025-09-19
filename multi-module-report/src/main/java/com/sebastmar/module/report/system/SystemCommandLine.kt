package com.sebastmar.module.report.system

/**
 * Represents a command line interface for executing shell commands.
 */
public interface SystemCommandLine {
    /**
     * Executes a command in the shell and returns the output.
     *
     * @param command The command to execute.
     * @param arguments A list of arguments to pass to the command. Defaults to an empty list.
     * @return The output of the command as a String.
     */
    public fun exec(
        command: String,
        arguments: List<String> = emptyList(),
    ): String
}
