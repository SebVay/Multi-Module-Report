@file:Suppress("MaxLineLength")

package com.sebastmar.multi.module.report.github.internal

import com.sebastmar.module.report.system.SystemCommandLine
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class GithubClientTest {

    private val commandLine: SystemCommandLine = mockk(relaxed = true)
    private val client = GithubClient(commandLine)

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `prUrl calls gh with correct arguments and trims output`() {
        every { commandLine.exec("gh", listOf("pr", "view", "--json", "url", "--jq", ".url")) } returns "  https://github.com/org/repo/pull/1  \n"

        val result = client.prUrl()

        assertEquals("https://github.com/org/repo/pull/1", result)
    }

    @Test
    fun `prBody calls gh with correct arguments and trims output`() {
        every { commandLine.exec("gh", listOf("pr", "view", "--json", "body", "--jq", ".body")) } returns "  PR description  \n"

        val result = client.prBody()

        assertEquals("PR description", result)
    }

    @Test
    fun `prNumber calls gh with correct arguments and trims output`() {
        every { commandLine.exec("gh", listOf("pr", "view", "--json", "number", "--jq", ".number")) } returns "42\n"

        val result = client.prNumber()

        assertEquals("42", result)
    }

    @Test
    fun `prAddedFiles returns parsed list of added file paths`() {
        val jqExpr = """.files[] | select((((.changeType? // .status) // "") | ascii_upcase) == "ADDED") | (.path // "")"""
        every { commandLine.exec("gh", listOf("pr", "view", "--json", "files", "--jq", jqExpr)) } returns "src/Main.kt\nsrc/Utils.kt\n"

        val result = client.prAddedFiles()

        assertEquals(listOf("src/Main.kt", "src/Utils.kt"), result)
    }

    @Test
    fun `prModifiedFiles returns parsed list of modified file paths`() {
        val jqExpr = """.files[] | select((((.changeType? // .status) // "") | ascii_upcase) == "MODIFIED") | (.path // "")"""
        every { commandLine.exec("gh", listOf("pr", "view", "--json", "files", "--jq", jqExpr)) } returns "build.gradle.kts\n"

        val result = client.prModifiedFiles()

        assertEquals(listOf("build.gradle.kts"), result)
    }

    @Test
    fun `prRemovedFiles returns parsed list of removed file paths`() {
        val jqExpr = """.files[] | select((((.changeType? // .status) // "") | ascii_upcase) == "REMOVED") | (.path // "")"""
        every { commandLine.exec("gh", listOf("pr", "view", "--json", "files", "--jq", jqExpr)) } returns "old/File.kt\n"

        val result = client.prRemovedFiles()

        assertEquals(listOf("old/File.kt"), result)
    }

    @Test
    fun `prAddedFiles filters blank lines and duplicates`() {
        val jqExpr = """.files[] | select((((.changeType? // .status) // "") | ascii_upcase) == "ADDED") | (.path // "")"""
        every { commandLine.exec("gh", listOf("pr", "view", "--json", "files", "--jq", jqExpr)) } returns "file.kt\n\nfile.kt\n  \n"

        val result = client.prAddedFiles()

        assertEquals(listOf("file.kt"), result)
    }

    @Test
    fun `findExistingCommentId returns id when comment with marker exists`() {
        every { commandLine.exec("gh", listOf("pr", "view", "--json", "number", "--jq", ".number")) } returns "7\n"
        every {
            commandLine.exec(
                "gh",
                listOf(
                    "api",
                    "repos/{owner}/{repo}/issues/7/comments",
                    "--jq",
                    ".[] | select(.body | contains(\"${GithubClient.COMMENT_MARKER}\")) | .id",
                ),
            )
        } returns "98765\n"

        val result = client.findExistingCommentId()

        assertEquals(98765L, result)
    }

    @Test
    fun `findExistingCommentId returns null when no matching comment`() {
        every { commandLine.exec("gh", listOf("pr", "view", "--json", "number", "--jq", ".number")) } returns "7\n"
        every {
            commandLine.exec(
                "gh",
                listOf(
                    "api",
                    "repos/{owner}/{repo}/issues/7/comments",
                    "--jq",
                    ".[] | select(.body | contains(\"${GithubClient.COMMENT_MARKER}\")) | .id",
                ),
            )
        } returns "\n"

        val result = client.findExistingCommentId()

        assertNull(result)
    }

    @Test
    fun `createComment calls gh pr comment with correct arguments`() {
        every { commandLine.exec("gh", listOf("pr", "view", "--json", "number", "--jq", ".number")) } returns "7\n"

        val body = "test body"
        client.createComment(body)

        verify(exactly = 1) {
            commandLine.exec("gh", listOf("pr", "comment", "7", "--body", body))
        }
    }

    @Test
    fun `updateComment calls gh api PATCH with correct arguments`() {
        val commentId = 12345L
        val body = "updated body"

        client.updateComment(commentId, body)

        verify(exactly = 1) {
            commandLine.exec(
                "gh",
                listOf(
                    "api",
                    "repos/{owner}/{repo}/issues/comments/$commentId",
                    "--method",
                    "PATCH",
                    "--field",
                    "body=$body",
                ),
            )
        }
    }
}
