package com.sebastmar.module.report.internal.system.danger

import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import systems.danger.kotlin.models.bitbucket.BitBucketCloud
import systems.danger.kotlin.models.bitbucket.BitBucketServer
import systems.danger.kotlin.models.danger.DangerDSL
import systems.danger.kotlin.models.git.Git
import systems.danger.kotlin.models.github.GitHub
import systems.danger.kotlin.models.github.GitHubPR
import systems.danger.kotlin.models.gitlab.GitLab
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class DangerWrapperTest {

    private val danger = mockk<DangerDSL>(relaxed = true)
    private val dangerWrapper = DangerWrapper(danger)

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify createdFiles returns list from Danger git`() {
        val givenGit = mockk<Git>()

        every { danger.git } returns givenGit
        every { givenGit.createdFiles } returns listOf("file1.kt", "file2.kt")

        val result = dangerWrapper.createdFiles()

        assertEquals(listOf("file1.kt", "file2.kt"), result)
    }

    @Test
    fun `verify modifiedFiles returns list from Danger git`() {
        val givenGit = mockk<Git>()

        every { danger.git } returns givenGit
        every { givenGit.modifiedFiles } returns listOf("updatedFile.kt")

        val result = dangerWrapper.modifiedFiles()

        assertEquals(listOf("updatedFile.kt"), result)
    }

    @Test
    fun `verify deletedFiles returns list from Danger git`() {
        val givenGit = mockk<Git>()

        every { danger.git } returns givenGit
        every { givenGit.deletedFiles } returns listOf("deletedFile.kt")

        val result = dangerWrapper.deletedFiles()

        assertEquals(listOf("deletedFile.kt"), result)
    }

    @Test
    fun `verify targetSHA returns base sha on GitHub`() {
        val givenGithub = mockk<GitHub>()
        val givenPr = mockk<GitHubPR>()

        every { danger.onGitHub } returns true
        every { danger.github } returns givenGithub
        every { givenGithub.pullRequest } returns givenPr
        every { givenPr.base.sha } returns "baseSHA"

        assertEquals("baseSHA", dangerWrapper.targetSHA())
    }

    @Test
    fun `verify targetSHA returns origin main when not on any known hosts`() {
        every { danger.onGitHub } returns false
        assertEquals("origin/main", dangerWrapper.targetSHA())
    }

    @Test
    fun `verify htmlLink returns PR url on GitHub`() {
        val givenGithub = mockk<GitHub>()
        val givenPr = mockk<GitHubPR>()

        every { danger.onGitHub } returns true
        every { danger.github } returns givenGithub
        every { givenGithub.pullRequest } returns givenPr
        every { givenPr.htmlURL } returns "https://github.com/org/repo/pull/1"

        assertEquals("https://github.com/org/repo/pull/1", dangerWrapper.htmlLink())
    }

    @Test
    fun `verify htmlLink returns localhost when not on any known hosts`() {
        every { danger.onGitHub } returns false
        assertEquals("127.0.0.1", dangerWrapper.htmlLink())
    }

    @Test
    fun `verify prBody returns body on GitHub`() {
        val givenGithub = mockk<GitHub>()
        val givenPr = mockk<GitHubPR>()

        every { danger.onGitHub } returns true
        every { danger.github } returns givenGithub
        every { givenGithub.pullRequest } returns givenPr
        every { givenPr.body } returns "body"

        assertEquals("body", dangerWrapper.prBody())
    }

    @Test
    fun `verify prBody returns empty if github body is null`() {
        val givenGithub = mockk<GitHub>()
        val givenPr = mockk<GitHubPR>()

        every { danger.onGitHub } returns true
        every { danger.github } returns givenGithub
        every { givenGithub.pullRequest } returns givenPr
        every { givenPr.body } returns null

        assertEquals("", dangerWrapper.prBody())
    }

    @Test
    fun `verify prBody returns empty when not on any known hosts`() {
        every { danger.onGitHub } returns false
        assertEquals("", dangerWrapper.prBody())
    }

    @Test
    fun `verify onGithub returns true when on GitHub`() {
        every { danger.onGitHub } returns true
        assertTrue(dangerWrapper.onGithub())
    }

    @Test
    fun `verify onGithub returns false when not on GitHub`() {
        every { danger.onGitHub } returns false
        assertFalse(dangerWrapper.onGithub())
    }

    @Test
    fun `verify it prints the GitHub context if on GitHub`() {
        val givenGitHub = mockk<GitHub>()
        val givenDanger = mockk<DangerDSL>(relaxed = true)
        val givenGitHubPrint = "Printed GitHub"

        every { givenDanger.onGitHub } returns true
        every { givenDanger.github } returns givenGitHub
        every { givenGitHub.toString() } returns givenGitHubPrint

        val printed = ByteArrayOutputStream().use { buffer ->
            System.setOut(PrintStream(buffer))

            DangerWrapper(givenDanger)

            buffer.toString().trimEnd()
        }

        assertEquals(givenGitHubPrint, printed)
    }

    @Test
    fun `verify it prints the GitLab context if on GitLab`() {
        val givenGitLab = mockk<GitLab>()
        val givenDanger = mockk<DangerDSL>(relaxed = true)
        val givenGitLabPrint = "Printed GitLab"

        every { givenDanger.onGitLab } returns true
        every { givenDanger.gitlab } returns givenGitLab
        every { givenGitLab.toString() } returns givenGitLabPrint

        val result = ByteArrayOutputStream().use { buffer ->
            System.setOut(PrintStream(buffer))

            DangerWrapper(givenDanger)

            buffer.toString().trimEnd()
        }

        assertEquals(givenGitLabPrint, result)
    }

    @Test
    fun `verify it prints the BitBucketCloud context if on BitBucketCloud`() {
        val givenBitBucketCloud = mockk<BitBucketCloud>()
        val givenDanger = mockk<DangerDSL>(relaxed = true)
        val givenBitBucketCloudPrint = "Printed BitBucketCloud"

        every { givenDanger.onBitBucketCloud } returns true
        every { givenDanger.bitBucketCloud } returns givenBitBucketCloud
        every { givenBitBucketCloud.toString() } returns givenBitBucketCloudPrint

        val result = ByteArrayOutputStream().use { buffer ->
            System.setOut(PrintStream(buffer))

            DangerWrapper(givenDanger)

            buffer.toString().trimEnd()
        }

        assertEquals(givenBitBucketCloudPrint, result)
    }

    @Test
    fun `verify it prints the BitBucketServer context if on BitBucketServer`() {
        val givenBitBucketServer = mockk<BitBucketServer>()
        val givenDanger = mockk<DangerDSL>(relaxed = true)
        val givenBitBucketServerPrint = "Printed BitBucketServer"

        every { givenDanger.onBitBucketServer } returns true
        every { givenDanger.bitBucketServer } returns givenBitBucketServer
        every { givenBitBucketServer.toString() } returns givenBitBucketServerPrint

        val result = ByteArrayOutputStream().use { buffer ->
            System.setOut(PrintStream(buffer))

            DangerWrapper(givenDanger)

            buffer.toString().trimEnd()
        }

        assertEquals(givenBitBucketServerPrint, result)
    }
}
