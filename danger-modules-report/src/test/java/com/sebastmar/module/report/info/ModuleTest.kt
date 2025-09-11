package com.sebastmar.module.report.info

import com.sebastmar.module.report.info.VersionedFile.Status
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ModuleTest {

    @Test
    fun `verify it returns empty lists when there are no files`() {
        val givenModule = Module(
            name = "moduleA",
            files = emptyList(),
        )

        val created = givenModule.createdFiles
        val modified = givenModule.modifiedFiles
        val deleted = givenModule.deletedFiles

        val expectedCreated: List<VersionedFile> = emptyList()
        val expectedModified: List<VersionedFile> = emptyList()
        val expectedDeleted: List<VersionedFile> = emptyList()

        assertEquals(expectedCreated, created)
        assertEquals(expectedModified, modified)
        assertEquals(expectedDeleted, deleted)
    }

    @Test
    fun `verify it returns only the created files`() {
        val givenCreatedA = VersionedFile(name = "A.kt", fullPath = "moduleA/src/A.kt", status = Status.Created)
        val givenCreatedB = VersionedFile(name = "B.kt", fullPath = "moduleA/src/B.kt", status = Status.Created)
        val givenModified = VersionedFile(name = "C.kt", fullPath = "moduleA/src/C.kt", status = Status.Modified)
        val givenDeleted = VersionedFile(name = "D.kt", fullPath = "moduleA/src/D.kt", status = Status.Deleted)

        val givenModule =
            Module(name = "moduleA", files = listOf(givenCreatedA, givenCreatedB, givenModified, givenDeleted))

        val result = givenModule.createdFiles

        val expected = listOf(givenCreatedA, givenCreatedB)

        assertEquals(expected, result)
    }

    @Test
    fun `verify it returns only the modified files`() {
        val givenCreated = VersionedFile(name = "A.kt", fullPath = "moduleA/src/A.kt", status = Status.Created)
        val givenModifiedA = VersionedFile(name = "B.kt", fullPath = "moduleA/src/B.kt", status = Status.Modified)
        val givenModifiedB = VersionedFile(name = "C.kt", fullPath = "moduleA/src/C.kt", status = Status.Modified)
        val givenDeleted = VersionedFile(name = "D.kt", fullPath = "moduleA/src/D.kt", status = Status.Deleted)

        val givenModule = Module(
            name = "moduleA",
            files = listOf(givenCreated, givenModifiedA, givenModifiedB, givenDeleted)
        )

        val result = givenModule.modifiedFiles

        val expected = listOf(givenModifiedA, givenModifiedB)

        assertEquals(expected, result)
    }

    @Test
    fun `verify it returns only the deleted files`() {
        val givenCreated = VersionedFile(name = "A.kt", fullPath = "moduleA/src/A.kt", status = Status.Created)
        val givenModified = VersionedFile(name = "B.kt", fullPath = "moduleA/src/B.kt", status = Status.Modified)
        val givenDeletedA = VersionedFile(name = "C.kt", fullPath = "moduleA/src/C.kt", status = Status.Deleted)
        val givenDeletedB = VersionedFile(name = "D.kt", fullPath = "moduleA/src/D.kt", status = Status.Deleted)
        val givenModule = Module(name = "moduleA", files = listOf(givenCreated, givenModified, givenDeletedA, givenDeletedB))

        val result = givenModule.deletedFiles

        val expected = listOf(givenDeletedA, givenDeletedB)

        assertEquals(expected, result)
    }
}
