package com.sebastmar.module.report.system

/**
 * Interface for interacting with the underlying systems where the Pull Request is being executed.
 *
 * This provides access to information about the current pull request, such as modified files,
 * target SHA, and whether it's running on GitHub.
 */
public interface SystemWrapper {

    public fun createdFiles(): List<String>
    public fun modifiedFiles(): List<String>
    public fun deletedFiles(): List<String>

    /**
     * The target SHA of the pull request.
     * This represents the SHA of the base branch that the pull request is targeting.
     *
     * @return The target SHA as a String.
     */
    public fun targetSHA(): String

    /**
     * Returns the HTML link to the current pull request.
     * @return The HTML link as a String.
     */
    public fun htmlLink(): String

    /**
     * Gets the body of the pull request.
     * @return The pull request body as a string.
     */
    public fun prBody(): String
    public fun onGithub(): Boolean
}
