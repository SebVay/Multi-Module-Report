package com.sebastmar.multi.module.report.danger.internal

import com.sebastmar.module.report.system.SystemWrapper
import systems.danger.kotlin.models.danger.DangerDSL

internal class DangerWrapper(
    private val dangerContext: DangerDSL,
) : SystemWrapper {
    init {
        printContextToCI()
    }

    /**
     * Retrieves a list of file paths that have been created in the current pull request.
     *
     * @return A list of [String] objects representing the created files.
     */
    override fun createdFiles(): List<String> = dangerContext.git.createdFiles

    /**
     * Retrieves a list of file paths that have been modified in the current pull request.
     *
     * @return A list of [String] objects representing the modified files.
     */
    override fun modifiedFiles(): List<String> = dangerContext.git.modifiedFiles

    /**
     * Retrieves a list of file paths that have been deleted in the current pull request.
     *
     * @return A list of [String] objects representing the deleted files.
     */
    override fun deletedFiles(): List<String> = dangerContext.git.deletedFiles

    /**
     * Retrieves the target SHA of the current pull request.
     *
     * @return The SHA of the base branch of the pull request if running on GitHub,
     *         otherwise "origin/main" as a fallback for local testing.
     */
    override fun targetSHA(): String {
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
    override fun htmlLink(): String {
        return when {
            dangerContext.onGitHub -> dangerContext.github.pullRequest.htmlURL

            // Fallback for local testing
            else -> "127.0.0.1"
        }
    }

    override fun prBody(): String {
        return when {
            dangerContext.onGitHub -> {
                val body = dangerContext.github.pullRequest.body
                body.orEmpty()
            }
            else -> ""
        }
    }

    override fun onGithub(): Boolean = dangerContext.onGitHub

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
