package com.sebastmar.danger.report.internal.domain

import com.sebastmar.danger.report.info.VersionedFile
import com.sebastmar.danger.report.info.VersionedFile.Status
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
    private val runShortStatCommand: Boolean,
) : GetFiles {

    private val regex by lazy {
        """1 file changed, (?:(\d+)\s+insertions\(\+\))?(?:,\s*)?(?:(\d+)\s+deletions\(-\))?""".toRegex()
    }

    private val projectRoot by lazy {
        commandLine.exec("git rev-parse --show-toplevel").trim()
    }

    override operator fun invoke(
        files: List<FilePath>,
        status: Status,
    ): List<VersionedFile> {
        return files.map { filePath ->
            val fullPath = filePath.removePrefix("'a/' --dst-prefix='b/'")
            val fileName = fullPath.substringAfterLast("/")
            var insertions: Int? = null
            var deletions: Int? = null

            if (runShortStatCommand) {
                val diffShortStat = commandLine.exec("git diff --shortstat origin/main -- $projectRoot/$fullPath")

                val match = regex.find(diffShortStat)

                insertions = match?.groups?.get(1)?.value?.toIntOrNull()
                deletions = match?.groups?.get(2)?.value?.toIntOrNull()
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
