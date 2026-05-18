package com.sebastmar.multi.module.report.github.internal

import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class GithubWriterTest {

    private val client: GithubClient = mockk(relaxed = true)
    private val writer = GithubWriter(client)

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `write creates a new comment when no existing comment is found`() {
        every { client.findExistingCommentId() } returns null

        val givenContent = "# Module Report\n| Module | Files |"

        writer.write(givenContent)

        val expectedBody = "${GithubClient.COMMENT_MARKER}\n$givenContent"
        verify(exactly = 1) { client.createComment(expectedBody) }
        verify(exactly = 0) { client.updateComment(any(), any()) }
    }

    @Test
    fun `write updates existing comment when marker comment is found`() {
        val givenCommentId = 12345L
        every { client.findExistingCommentId() } returns givenCommentId

        val givenContent = "# Module Report\n| Module | Files |"

        writer.write(givenContent)

        val expectedBody = "${GithubClient.COMMENT_MARKER}\n$givenContent"
        verify(exactly = 1) { client.updateComment(givenCommentId, expectedBody) }
        verify(exactly = 0) { client.createComment(any()) }
    }

    @Test
    fun `write falls back to creating a new comment when finding existing comment throws`() {
        every { client.findExistingCommentId() } throws RuntimeException("API error")

        val givenContent = "# Report"

        writer.write(givenContent)

        val expectedBody = "${GithubClient.COMMENT_MARKER}\n$givenContent"
        verify(exactly = 1) { client.createComment(expectedBody) }
        verify(exactly = 0) { client.updateComment(any(), any()) }
    }
}
