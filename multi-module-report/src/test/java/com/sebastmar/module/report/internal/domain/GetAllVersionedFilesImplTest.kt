package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.info.VersionedFile
import com.sebastmar.module.report.info.VersionedFile.Status.Created
import com.sebastmar.module.report.info.VersionedFile.Status.Deleted
import com.sebastmar.module.report.info.VersionedFile.Status.Modified
import com.sebastmar.module.report.system.SystemWrapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetAllVersionedFilesImplTest {

    private val systemWrapper: SystemWrapper = mockk()
    private val getVersionedFiles: GetVersionedFiles = mockk()

    private val getAllVersionedFiles: GetAllVersionedFiles = GetAllVersionedFilesImpl(
        systemWrapper = systemWrapper,
        getVersionedFiles = getVersionedFiles,
    )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify it returns the list of created, modified and deleted files preserving their order`() {
        val givenCreatedPath = listOf("a/new/FileA.kt")
        val givenModifiedPath = listOf("b/src/FileB.kt", "b/src/FileB2.kt")
        val givenDeletedPath = listOf("c/old/FileC.kt")

        val givenCreated = listOf(
            VersionedFile(name = "FileA.kt", fullPath = "path", status = Created),
        )
        val givenModified = listOf(
            VersionedFile(name = "FileB.kt", fullPath = "path", status = Modified),
            VersionedFile(name = "FileB2.kt", fullPath = "path", status = Modified),
        )
        val givenDeleted = listOf(
            VersionedFile(name = "FileC.kt", fullPath = "path", status = Deleted),
        )

        every { systemWrapper.createdFiles() } returns givenCreatedPath
        every { systemWrapper.modifiedFiles() } returns givenModifiedPath
        every { systemWrapper.deletedFiles() } returns givenDeletedPath

        every { getVersionedFiles(givenCreatedPath, Created) } returns givenCreated
        every { getVersionedFiles(givenModifiedPath, Modified) } returns givenModified
        every { getVersionedFiles(givenDeletedPath, Deleted) } returns givenDeleted

        val result = getAllVersionedFiles()

        val expected = listOf(
            VersionedFile(name = "FileA.kt", fullPath = "path", status = Created),
            VersionedFile(name = "FileB.kt", fullPath = "path", status = Modified),
            VersionedFile(name = "FileB2.kt", fullPath = "path", status = Modified),
            VersionedFile(name = "FileC.kt", fullPath = "path", status = Deleted),
        )

        assertEquals(expected, result)
    }
}
