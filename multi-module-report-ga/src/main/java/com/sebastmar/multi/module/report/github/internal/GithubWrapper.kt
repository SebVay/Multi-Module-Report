package com.sebastmar.multi.module.report.github.internal

import com.sebastmar.module.report.system.SystemWrapper

internal class GithubWrapper(
    private val github: GithubClient,
) : SystemWrapper {

    // Cache PR changes and expose the three lists
    private data class PrChanges(
        val added: List<String>,
        val modified: List<String>,
        val removed: List<String>,
    )

    private val prChanges: PrChanges by lazy { fetchPrChanges() }

    override fun createdFiles(): List<String> = prChanges.added
    override fun modifiedFiles(): List<String> = prChanges.modified
    override fun deletedFiles(): List<String> = prChanges.removed

    override fun targetSHA(): String = System.getenv("GITHUB_BASE_SHA") ?: "origin/main"

    override fun htmlLink(): String {
        // Try GitHub CLI first, then fall back to env/default
        return runCatching {
            github.prUrl()
        }.getOrNull()
            ?: System.getenv("GITHUB_PR_HTML_URL")
            ?: "127.0.0.1"
    }

    override fun prBody(): String {
        // Try GitHub CLI first, then fall back to env/default
        return runCatching {
            github.prBody()
        }.getOrNull()?.takeIf { it.isNotBlank() }
            ?: System.getenv("GITHUB_PR_BODY").orEmpty()
    }

    override fun onGithub(): Boolean = System.getenv("GITHUB_ACTIONS") == "true" ||
        System.getenv("GITHUB_REPOSITORY") != null

    // Fetch added/modified/removed using dedicated client calls (no TSV parsing)
    private fun fetchPrChanges(): PrChanges {
        fun List<String>.clean() = this.map { it.trim().trim('"') }.filter { it.isNotBlank() }.distinct()

        val added = runCatching { github.prAddedFiles() }.getOrNull().orEmpty().clean()
        val modified = runCatching { github.prModifiedFiles() }.getOrNull().orEmpty().clean()
        val removed = runCatching { github.prRemovedFiles() }.getOrNull().orEmpty().clean()

        return PrChanges(
            added = added,
            modified = modified,
            removed = removed,
        )
    }
}
