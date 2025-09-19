package com.sebastmar.module.report.info

import com.sebastmar.module.report.info.VersionedFile.Status
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class VersionedFileTest {

    @Test
    fun `verify it calculates the sha-256 of the full path`() {
        val givenFullPath = "path/to/file.txt"

        val versionedFile = VersionedFile(
            name = "file.txt",
            fullPath = givenFullPath,
            status = Status.Modified,
        )

        val result = versionedFile.sha256Path

        val expected = "12b020d0502b352d2d48d7776595b587b0705bec467b434062c1bb503c3ff9dd"
        assertEquals(expected, result)
    }

    @Test
    fun `verify it returns zero when inserted lines are null`() {
        val givenFiles = listOf(
            versionedFile(insertions = null),
            versionedFile(insertions = null),
        )

        val result = givenFiles.getInsertedLines()

        val expected = 0
        assertEquals(expected, result)
    }

    @Test
    fun `verify it returns zero when deleted lines are null`() {
        val givenFiles = listOf(
            versionedFile(deletions = null),
            versionedFile(deletions = null),
        )

        val result = givenFiles.getDeletedLines()

        val expected = 0
        assertEquals(expected, result)
    }

    @Test
    fun `verify it sums inserted lines across files and ignores nulls`() {
        val givenFiles = listOf(
            versionedFile(insertions = 10),
            versionedFile(insertions = 20),
            versionedFile(insertions = null),
        )

        val result = givenFiles.getInsertedLines()

        val expected = 30
        assertEquals(expected, result)
    }

    @Test
    fun `verify it sums deleted lines across files and ignores nulls`() {
        val givenFiles = listOf(
            versionedFile(deletions = 10),
            versionedFile(deletions = 20),
            versionedFile(deletions = null),
        )

        val result = givenFiles.getDeletedLines()

        val expected = 30
        assertEquals(expected, result)
    }

    private fun versionedFile(
        insertions: Int? = null,
        deletions: Int? = null,
    ) = VersionedFile(
        name = "file.txt",
        fullPath = "path/to/file.txt",
        status = Status.Modified,
        insertions = insertions,
        deletions = deletions,
    )
}
