package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.info.VersionedFile
import com.sebastmar.module.report.internal.ShowLineIndicators
import com.sebastmar.module.report.system.SystemCommandLine
import com.sebastmar.module.report.system.SystemWrapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.io.path.Path

class GetVersionedFilesImplTest {

    private val systemCommandLine: SystemCommandLine = mockk()
    private val getProjectRoot: GetProjectRoot = mockk()
    private val systemWrapper: SystemWrapper = mockk()

    private lateinit var getVersionedFiles: GetVersionedFiles

    private fun setUpGetFiles(
        showLineIndicators: Boolean = false,
    ) {
        getVersionedFiles = GetVersionedFilesImpl(
            systemCommandLine = systemCommandLine,
            getProjectRoot = getProjectRoot,
            systemWrapper = systemWrapper,
            showLineIndicators = ShowLineIndicators(showLineIndicators),
        )
    }

    @BeforeEach
    fun setUp() {
        setUpGetFiles()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify it returns versioned files without line indicators when disabled`() {
        val givenFiles: List<String> = listOf(
            "src/main/FileA.kt",
            "README.md",
        )

        every { getProjectRoot() } returns Path("/project/root")

        val result = getVersionedFiles(givenFiles, VersionedFile.Status.Modified)

        val expected = listOf(
            VersionedFile(
                name = "FileA.kt",
                fullPath = "src/main/FileA.kt",
                status = VersionedFile.Status.Modified,
                insertions = null,
                deletions = null,
            ),
            VersionedFile(
                name = "README.md",
                fullPath = "README.md",
                status = VersionedFile.Status.Modified,
                insertions = null,
                deletions = null,
            ),
        )

        assertEquals(expected, result)
    }

    @Test
    fun `verify it parses insertions and deletions when line indicators are enabled`() {
        val givenFiles: List<String> = listOf("src/Feature.kt")
        val givenSha = "SHA"
        val givenShortStat = "1 file changed, 10 insertions(+), 3 deletions(-)"
        val givenRoot = Path("/repo")

        every { getProjectRoot() } returns givenRoot
        every { systemWrapper.targetSHA() } returns givenSha
        every { systemCommandLine.exec("git diff --shortstat $givenSha -- $givenRoot/src/Feature.kt") } returns givenShortStat

        setUpGetFiles(showLineIndicators = true)

        val result = getVersionedFiles(givenFiles, VersionedFile.Status.Created)

        val expected = listOf(
            VersionedFile(
                name = "Feature.kt",
                fullPath = "src/Feature.kt",
                status = VersionedFile.Status.Created,
                insertions = 10,
                deletions = 3,
            ),
        )

        assertEquals(expected, result)
    }

    @Test
    fun `verify it handles missing insertions or deletions in shortstat`() {
        val givenFiles: List<String> = listOf(
            "lib/Util.kt",
            "docs/Guide.md",
            "docs/NothingChangedFile.md",
        )
        val givenSha = "SHA"
        val givenShortStatOnlyInsertions = "1 file changed, 7 insertions(+)"
        val givenShortStatOnlyDeletions = "1 file changed, 5 deletions(-)"
        val givenShortStatNothingChanged = "Nothing changed"
        val givenRoot = Path("/code")

        every { getProjectRoot() } returns givenRoot
        every { systemWrapper.targetSHA() } returns givenSha
        every { systemCommandLine.exec("git diff --shortstat $givenSha -- $givenRoot/lib/Util.kt") } returns givenShortStatOnlyInsertions
        every { systemCommandLine.exec("git diff --shortstat $givenSha -- $givenRoot/docs/Guide.md") } returns givenShortStatOnlyDeletions
        every { systemCommandLine.exec("git diff --shortstat $givenSha -- $givenRoot/docs/NothingChangedFile.md") } returns givenShortStatNothingChanged

        setUpGetFiles(showLineIndicators = true)

        val result = getVersionedFiles(givenFiles, VersionedFile.Status.Modified)

        val expected = listOf(
            VersionedFile(
                name = "Util.kt",
                fullPath = "lib/Util.kt",
                status = VersionedFile.Status.Modified,
                insertions = 7,
                deletions = null,
            ),
            VersionedFile(
                name = "Guide.md",
                fullPath = "docs/Guide.md",
                status = VersionedFile.Status.Modified,
                insertions = null,
                deletions = 5,
            ),
            VersionedFile(
                name = "NothingChangedFile.md",
                fullPath = "docs/NothingChangedFile.md",
                status = VersionedFile.Status.Modified,
                insertions = null,
                deletions = null,
            ),
        )

        assertEquals(expected, result)
    }

    @Test
    fun `verify it returns an empty list when given files is empty`() {
        val givenFiles: List<String> = emptyList()

        every { getProjectRoot() } returns Path("/any")
        setUpGetFiles(showLineIndicators = true)

        val result = getVersionedFiles(givenFiles, VersionedFile.Status.Deleted)

        val expected: List<VersionedFile> = emptyList()
        assertEquals(expected, result)
    }
}
