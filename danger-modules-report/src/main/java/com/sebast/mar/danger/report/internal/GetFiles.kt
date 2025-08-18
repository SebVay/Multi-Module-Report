package com.sebast.mar.danger.report.internal

import com.sebast.mar.danger.report.Status
import com.sebast.mar.danger.report.VersionedFile
import com.sebast.mar.danger.report.helper.CommandLine
import systems.danger.kotlin.models.git.FilePath

/**
 * Maps a list of file paths to a list of [VersionedFile] objects.
 */
internal interface GetFiles {
    operator fun invoke(
        files: List<FilePath>,
        status: Status,
    ): List<VersionedFile>
}

internal class GetFilesImpl(
    private val commandLine: CommandLine,
) : GetFiles {
    override operator fun invoke(
        files: List<FilePath>,
        status: Status,
    ): List<VersionedFile> {
        val projectRoot = commandLine.exec("git rev-parse --show-toplevel").trim()

        return files.map { filePath ->
            val fullPath = filePath.removePrefix("'a/' --dst-prefix='b/'")
            val fileName = fullPath.substringAfterLast("/")

            val regex =
                """1 file changed, (?:(\d+)\s+insertions\(\+\))?(?:,\s*)?(?:(\d+)\s+deletions\(-\))?""".toRegex()
            val diffShortStat = commandLine.exec("git diff --shortstat origin/main -- $projectRoot$fullPath")

            val (insertions, deletions) = regex.find(diffShortStat).let { match ->
                val insertions = match?.groups?.get(1)?.value?.toIntOrNull()
                val deletions = match?.groups?.get(2)?.value?.toIntOrNull()

                insertions to deletions
            }

            VersionedFile(
                name = fileName,
                fullPath = fullPath,
                insertions = insertions,
                deletions = deletions,
                status = status,
            )
        }
    }
}
