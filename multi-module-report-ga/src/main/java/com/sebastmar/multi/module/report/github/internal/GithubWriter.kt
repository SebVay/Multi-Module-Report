package com.sebastmar.multi.module.report.github.internal

import com.sebastmar.module.report.system.SystemWriter

/**
 * Writes the module report as a PR comment using the GitHub CLI.
 *
 * On the first run, creates a new comment. On subsequent runs, finds and updates
 * the existing comment using a hidden HTML marker to avoid comment spam.
 */
internal class GithubWriter(
    private val client: GithubClient,
) : SystemWriter<String> {

    override fun write(content: String) {
        val markedContent = "${GithubClient.COMMENT_MARKER}\n$content"
        val existingId = runCatching { client.findExistingCommentId() }.getOrNull()

        if (existingId != null) {
            client.updateComment(existingId, markedContent)
        } else {
            client.createComment(markedContent)
        }
    }
}
