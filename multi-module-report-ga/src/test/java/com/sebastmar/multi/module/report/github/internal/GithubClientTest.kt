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

    private fun mockPrNumber(prNumber: String = "42") {
        every {
            commandLine.exec("gh", listOf("pr", "view", "--json", "number", "--jq", ".number"))
        } returns "$prNumber\n"
    }

    @Test
    fun `prUrl calls gh with correct arguments and trims output`() {
        mockPrNumber()
        every { commandLine.exec("gh", listOf("pr", "view", "42", "--json", "url", "--jq", ".url")) } returns "  https://github.com/org/repo/pull/1  \n"

        val result = client.prUrl()

        assertEquals("https://github.com/org/repo/pull/1", result)
    }

    @Test
    fun `prBody calls gh with correct arguments and trims output`() {
        mockPrNumber()
        every { commandLine.exec("gh", listOf("pr", "view", "42", "--json", "body", "--jq", ".body")) } returns "  PR description  \n"

        val result = client.prBody()

        assertEquals("PR description", result)
    }

    @Test
    fun `prNumber calls gh with correct arguments and trims output`() {
        mockPrNumber()

        val result = client.prNumber()

        assertEquals("42", result)
    }

    @Test
    fun `prAddedFiles returns parsed list of added file paths`() {
        mockPrNumber()
        val jqExpr = """.files[] | select((((.changeType? // .status) // "") | ascii_upcase) == "ADDED") | (.path // "")"""
        every { commandLine.exec("gh", listOf("pr", "view", "42", "--json", "files", "--jq", jqExpr)) } returns "src/Main.kt\nsrc/Utils.kt\n"

        val result = client.prAddedFiles()

        assertEquals(listOf("src/Main.kt", "src/Utils.kt"), result)
    }

    @Test
    fun `prModifiedFiles returns parsed list of modified file paths`() {
        mockPrNumber()
        val jqExpr = """.files[] | select((((.changeType? // .status) // "") | ascii_upcase) == "MODIFIED") | (.path // "")"""
        every { commandLine.exec("gh", listOf("pr", "view", "42", "--json", "files", "--jq", jqExpr)) } returns "build.gradle.kts\n"

        val result = client.prModifiedFiles()

        assertEquals(listOf("build.gradle.kts"), result)
    }

    @Test
    fun `prRemovedFiles returns parsed list of removed file paths`() {
        mockPrNumber()
        val jqExpr = """.files[] | select((((.changeType? // .status) // "") | ascii_upcase) == "REMOVED") | (.path // "")"""
        every { commandLine.exec("gh", listOf("pr", "view", "42", "--json", "files", "--jq", jqExpr)) } returns "old/File.kt\n"

        val result = client.prRemovedFiles()

        assertEquals(listOf("old/File.kt"), result)
    }

    @Test
    fun `prAddedFiles filters blank lines and duplicates`() {
        mockPrNumber()
        val jqExpr = """.files[] | select((((.changeType? // .status) // "") | ascii_upcase) == "ADDED") | (.path // "")"""
        every { commandLine.exec("gh", listOf("pr", "view", "42", "--json", "files", "--jq", jqExpr)) } returns "file.kt\n\nfile.kt\n  \n"

        val result = client.prAddedFiles()

        assertEquals(listOf("file.kt"), result)
    }

    @Test
    fun `findExistingCommentId returns id when comment with marker exists`() {
        mockPrNumber()
        every {
            commandLine.exec(
                "gh",
                listOf(
                    "api",
                    "repos/{owner}/{repo}/issues/42/comments",
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
        mockPrNumber()
        every {
            commandLine.exec(
                "gh",
                listOf(
                    "api",
                    "repos/{owner}/{repo}/issues/42/comments",
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
        mockPrNumber()
        val body = "test body"
        client.createComment(body)

        verify(exactly = 1) {
            commandLine.exec("gh", listOf("pr", "comment", "42", "--body", body))
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
