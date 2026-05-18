package com.sebastmar.multi.module.report.github.internal

import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GithubWrapperTest {

    private val client: GithubClient = mockk(relaxed = true)
    private val wrapper = GithubWrapper(client)

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `createdFiles delegates to GithubClient prAddedFiles`() {
        every { client.prAddedFiles() } returns listOf("new/File.kt")

        assertEquals(listOf("new/File.kt"), wrapper.createdFiles())
    }

    @Test
    fun `modifiedFiles delegates to GithubClient prModifiedFiles`() {
        every { client.prModifiedFiles() } returns listOf("changed/File.kt")

        assertEquals(listOf("changed/File.kt"), wrapper.modifiedFiles())
    }

    @Test
    fun `deletedFiles delegates to GithubClient prRemovedFiles`() {
        every { client.prRemovedFiles() } returns listOf("old/File.kt")

        assertEquals(listOf("old/File.kt"), wrapper.deletedFiles())
    }

    @Test
    fun `htmlLink returns PR url from client`() {
        every { client.prUrl() } returns "https://github.com/org/repo/pull/1"

        assertEquals("https://github.com/org/repo/pull/1", wrapper.htmlLink())
    }

    @Test
    fun `htmlLink returns fallback when client throws`() {
        every { client.prUrl() } throws RuntimeException("No PR")

        // Falls back to env var or default
        val result = wrapper.htmlLink()

        // When no env var is set, falls back to "127.0.0.1"
        assertEquals("127.0.0.1", result)
    }

    @Test
    fun `prBody returns body from client`() {
        every { client.prBody() } returns "PR description"

        assertEquals("PR description", wrapper.prBody())
    }

    @Test
    fun `prBody returns empty when client returns blank`() {
        every { client.prBody() } returns "  "

        // Falls back to env var or empty string
        val result = wrapper.prBody()
        assertEquals("", result)
    }

    @Test
    fun `prBody returns empty when client throws`() {
        every { client.prBody() } throws RuntimeException("No PR")

        val result = wrapper.prBody()
        assertEquals("", result)
    }

    @Test
    fun `createdFiles returns empty list when client throws`() {
        every { client.prAddedFiles() } throws RuntimeException("API error")

        assertEquals(emptyList<String>(), wrapper.createdFiles())
    }

    @Test
    fun `modifiedFiles returns empty list when client throws`() {
        every { client.prModifiedFiles() } throws RuntimeException("API error")

        assertEquals(emptyList<String>(), wrapper.modifiedFiles())
    }

    @Test
    fun `deletedFiles returns empty list when client throws`() {
        every { client.prRemovedFiles() } throws RuntimeException("API error")

        assertEquals(emptyList<String>(), wrapper.deletedFiles())
    }
}
