package com.sebast.mar.danger.report.helper

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
    internal fun htmlUrl(): String {
        return if (dangerContext.onGitHub) {
            dangerContext.github.pullRequest.htmlURL
        } else {
            throw UnsupportedOperationException("This method is only available on GitHub")
        }
    }
}
