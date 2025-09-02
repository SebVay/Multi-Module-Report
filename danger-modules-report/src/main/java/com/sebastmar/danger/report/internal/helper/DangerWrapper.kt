package com.sebastmar.danger.report.internal.helper

import systems.danger.kotlin.models.danger.DangerDSL
import systems.danger.kotlin.models.git.FilePath

internal class DangerWrapper(
    private val dangerContext: DangerDSL,
) {
    /**
     * Retrieves a list of file paths that have been created in the current pull request.
     *
     * @return A list of [FilePath] objects representing the created files.
     */
    internal fun createdFiles() = dangerContext.git.createdFiles

    /**
     * Retrieves a list of file paths that have been modified in the current pull request.
     *
     * @return A list of [FilePath] objects representing the modified files.
     */
    internal fun modifiedFiles() = dangerContext.git.modifiedFiles

    /**
     * Retrieves a list of file paths that have been deleted in the current pull request.
     *
     * @return A list of [FilePath] objects representing the deleted files.
     */
    internal fun deletedFiles() = dangerContext.git.deletedFiles

    /**
     * Retrieves the HTML URL of the pull request.
     *
     * @return The HTML URL of the pull request.
     */
    internal fun htmlLink(): String {
        return when {
            dangerContext.onGitHub -> dangerContext.github.pullRequest.htmlURL
            else -> "127.0.0.1"
        }
    }

    internal fun prBody(): String {
        return when {
            dangerContext.onGitHub -> dangerContext.github.pullRequest.body.orEmpty()
            else -> ""
        }
    }

    internal fun onGithub(): Boolean = dangerContext.onGitHub
}
