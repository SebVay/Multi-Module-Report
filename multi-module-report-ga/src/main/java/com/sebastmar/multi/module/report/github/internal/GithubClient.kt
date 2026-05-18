package com.sebastmar.multi.module.report.github.internal

import com.sebastmar.module.report.system.SystemCommandLine

/**
 * Client over the GitHub CLI.
 * All gh calls should live here.
 *
 * In GitHub Actions, the checkout is a detached HEAD (merge commit),
 * so `gh pr view` cannot auto-detect the PR. We resolve the PR number
 * from the `GITHUB_PR_NUMBER` env var (set by the workflow) or fall
 * back to `gh pr view` for local usage.
 */
@Suppress("TooManyFunctions")
internal class GithubClient(
    private val commandLine: SystemCommandLine,
) {
    companion object {
        const val COMMENT_MARKER = "<!-- multi-module-report -->"
    }

    private val resolvedPrNumber: String by lazy {
        System.getenv("GITHUB_PR_NUMBER")?.takeIf { it.isNotBlank() }
            ?: commandLine.exec(
                command = "gh",
                arguments = listOf("pr", "view", "--json", "number", "--jq", ".number"),
            ).trim()
    }

    /**
     * Returns the PR number, resolved from env var or gh CLI.
     */
    fun prNumber(): String = resolvedPrNumber

    /**
     * Searches existing PR comments for one containing [COMMENT_MARKER].
     * Returns the comment ID if found, null otherwise.
     */
    fun findExistingCommentId(): Long? {
        val output = commandLine.exec(
            command = "gh",
            arguments = listOf(
                "api",
                "repos/{owner}/{repo}/issues/$resolvedPrNumber/comments",
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
        commandLine.exec(
            command = "gh",
            arguments = listOf("pr", "comment", resolvedPrNumber, "--body", body),
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

    fun prUrl(): String {
        val url = System.getenv("GITHUB_PR_URL")?.takeIf { it.isNotBlank() }
        if (url != null) return url

        return commandLine.exec(
            command = "gh",
            arguments = listOf(
                "pr",
                "view",
                resolvedPrNumber,
                "--json",
                "url",
                "--jq",
                ".url",
            ),
        ).trim()
    }

    fun prBody(): String {
        return commandLine.exec(
            command = "gh",
            arguments = listOf(
                "pr",
                "view",
                resolvedPrNumber,
                "--json",
                "body",
                "--jq",
                ".body",
            ),
        ).trim()
    }

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
            arguments = listOf(
                "pr",
                "view",
                resolvedPrNumber,
                "--json",
                "files",
                "--jq",
                jqExpr,
            ),
        )
        return output
            .lines()
            .map { it.trim().trim('"') }
            .filter { it.isNotBlank() }
            .distinct()
    }
}
