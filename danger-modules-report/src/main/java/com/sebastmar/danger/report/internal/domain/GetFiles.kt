package com.sebastmar.danger.report.internal.domain

import com.sebastmar.danger.report.ReportConfig
import com.sebastmar.danger.report.info.VersionedFile
import com.sebastmar.danger.report.info.VersionedFile.Status
import com.sebastmar.danger.report.internal.helper.DangerWrapper
import systems.danger.kotlin.models.git.FilePath
import java.nio.file.Path
import kotlin.io.path.pathString

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
    private val getProjectRoot: GetProjectRoot,
    private val reportConfig: ReportConfig,
    private val dangerWrapper: DangerWrapper,
) : GetFiles {

    private val regex by lazy {
        """1 file changed, (?:(\d+)\s+insertions\(\+\))?(?:,\s*)?(?:(\d+)\s+deletions\(-\))?""".toRegex()
    }

    private val projectRoot: Path by lazy { getProjectRoot() }

    override operator fun invoke(
        files: List<FilePath>,
        status: Status,
    ): List<VersionedFile> {
        return files.map { filePath ->
            val fullPath = filePath.removePrefix("'a/' --dst-prefix='b/'")
            val fileName = fullPath.substringAfterLast("/")
            var insertions: Int? = null
            var deletions: Int? = null

            if (reportConfig.showLineIndicators) {
                val diffShortStat = runDiffStat(fullPath)

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

    /**
     * Executes a git diff command to get the short statistics for a specific file.
     *
     * The command compares the current state of the file against the ref branch,
     * returning information about insertions and deletions.
     *
     * @param fullPath The path to the file relative to the project root.
     * @return A string containing the git diff shortstat output, which includes the number
     *         of insertions and deletions in the format:
     *         "1 file changed, X insertions(+), Y deletions(-)"
     */
    private fun runDiffStat(fullPath: String): String {
        val targetBranch = dangerWrapper.targetBranch()
        return commandLine.exec("git diff --shortstat $targetBranch -- ${projectRoot.pathString}/$fullPath")
    }
}
