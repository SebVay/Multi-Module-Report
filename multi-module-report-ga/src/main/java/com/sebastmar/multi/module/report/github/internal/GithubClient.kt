package com.sebastmar.multi.module.report.github.internal

import com.sebastmar.module.report.system.SystemCommandLine

/**
 * Client over the GitHub CLI.
 * All gh calls should live here.
 */
@Suppress("TooManyFunctions")
internal class GithubClient(
    private val commandLine: SystemCommandLine,
) {
    companion object {
        const val COMMENT_MARKER = "<!-- multi-module-report -->"
    }

    /**
     * Returns the PR number for the current branch.
     */
    fun prNumber(): String = commandLine.exec(
        command = "gh",
        arguments = listOf("pr", "view", "--json", "number", "--jq", ".number"),
    ).trim()

    /**
     * Searches existing PR comments for one containing [COMMENT_MARKER].
     * Returns the comment ID if found, null otherwise.
     */
    fun findExistingCommentId(): Long? {
        val prNumber = prNumber()
        val output = commandLine.exec(
            command = "gh",
            arguments = listOf(
                "api",
                "repos/{owner}/{repo}/issues/$prNumber/comments",
                "--jq",
                ".[] | select(.body | contains(\"$COMMENT_MARKER\")) | .id",
            ),
        )
        return output.trim().lines().firstOrNull { it.isNotBlank() }?.toLongOrNull()
    }

    /**
     * Creates a new comment on the current PR.
     */
    fun createComment(body: String) {
        val prNumber = prNumber()
        commandLine.exec(
            command = "gh",
            arguments = listOf("pr", "comment", prNumber, "--body", body),
        )
    }

    /**
     * Updates an existing comment by its ID.
     */
    fun updateComment(commentId: Long, body: String) {
        commandLine.exec(
            command = "gh",
            arguments = listOf(
                "api",
                "repos/{owner}/{repo}/issues/comments/$commentId",
                "--method",
                "PATCH",
                "--field",
                "body=$body",
            ),
        )
    }

    fun prUrl(): String = commandLine.exec(
        command = "gh",
        arguments = listOf("pr", "view", "--json", "url", "--jq", ".url"),
    ).trim()

    fun prBody(): String = commandLine.exec(
        command = "gh",
        arguments = listOf("pr", "view", "--json", "body", "--jq", ".body"),
    ).trim()

    /**
     * Returns the list of file paths added in the PR using gh + jq.
     */
    fun prAddedFiles(): List<String> = queryFilesByType("ADDED")

    /**
     * Returns the list of file paths modified in the PR using gh + jq.
     */
    fun prModifiedFiles(): List<String> = queryFilesByType("MODIFIED")

    /**
     * Returns the list of file paths removed in the PR using gh + jq.
     */
    fun prRemovedFiles(): List<String> = queryFilesByType("REMOVED")

    // Helper that fetches paths for a given change type
    private fun queryFilesByType(type: String): List<String> {
        val changeType = """((.changeType? // .status) // "")"""
        val jqExpr = """.files[] | select(($changeType | ascii_upcase) == "$type") | (.path // "")"""
        val output = commandLine.exec(
            command = "gh",
            arguments = listOf("pr", "view", "--json", "files", "--jq", jqExpr),
        )
        return output
            .lines()
            .map { it.trim().trim('"') }
            .filter { it.isNotBlank() }
            .distinct()
    }
}
