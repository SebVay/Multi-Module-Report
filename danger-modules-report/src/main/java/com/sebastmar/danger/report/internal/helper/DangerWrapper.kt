package com.sebastmar.danger.report.internal.helper

import systems.danger.kotlin.models.danger.DangerDSL
import systems.danger.kotlin.models.git.FilePath

internal class DangerWrapper(
    private val dangerContext: DangerDSL,
) {
    init {
        printContextToCI()
    }

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
     * Retrieves the target SHA of the current pull request.
     *
     * @return The SHA of the base branch of the pull request if running on GitHub,
     *         otherwise "origin/main" as a fallback for local testing.
     */
    internal fun targetSHA(): String {
        return when {
            dangerContext.onGitHub -> dangerContext.github.pullRequest.base.sha

            // Fallback for local testing
            else -> "origin/main"
        }
    }

    /**
     * Retrieves the HTML URL of the pull request.
     *
     * @return The HTML URL of the pull request.
     */
    internal fun htmlLink(): String {
        return when {
            dangerContext.onGitHub -> dangerContext.github.pullRequest.htmlURL

            // Fallback for local testing
            else -> "127.0.0.1"
        }
    }

    fun prBody(): String {
        return when {
            dangerContext.onGitHub -> dangerContext.github.pullRequest.body.orEmpty()
            else -> ""
        }
    }

    /**
     * Prints the context of the current Danger run to the CI console.
     * This includes information about the platform (GitHub, GitLab, BitBucket Server, or BitBucket Cloud)
     * and the Git context.
     */
    private fun printContextToCI() {
        when {
            dangerContext.onGitHub -> println(dangerContext.github)
            dangerContext.onGitLab -> println(dangerContext.gitlab)
            dangerContext.onBitBucketServer -> println(dangerContext.bitBucketServer)
            dangerContext.onBitBucketCloud -> println(dangerContext.bitBucketCloud)
        }
    }
}
