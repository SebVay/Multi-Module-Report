package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.info.VersionedFile
import com.sebastmar.module.report.info.VersionedFile.Status
import com.sebastmar.module.report.internal.ShowLineIndicators
import com.sebastmar.module.report.internal.system.SystemCommandLine
import com.sebastmar.module.report.internal.system.SystemWrapper
import java.nio.file.Path
import kotlin.io.path.pathString

/**
 * Maps a list of file paths to a list of [VersionedFile] objects.
 */
internal interface GetVersionedFiles {
    operator fun invoke(
        files: List<String>,
        status: Status,
    ): List<VersionedFile>
}

internal class GetVersionedFilesImpl(
    private val systemCommandLine: SystemCommandLine,
    private val getProjectRoot: GetProjectRoot,
    private val showLineIndicators: ShowLineIndicators,
    private val systemWrapper: SystemWrapper,
) : GetVersionedFiles {

    private val regex by lazy {
        """1 file changed, (?:(\d+)\s+insertions\(\+\))?(?:,\s*)?(?:(\d+)\s+deletions\(-\))?""".toRegex()
    }

    private val projectRoot: Path by lazy { getProjectRoot() }

    override operator fun invoke(
        files: List<String>,
        status: Status,
    ): List<VersionedFile> {
        return files.map { filePath ->
            val fullPath = filePath.removePrefix("'a/' --dst-prefix='b/'")
            val fileName = fullPath.substringAfterLast("/")
            var insertions: Int? = null
            var deletions: Int? = null

            if (showLineIndicators.value) {
                val diffShortStat = runDiffStat(fullPath)
                val diffResult = regex.find(diffShortStat)

                if (diffResult != null) {
                    insertions = diffResult.groups[1]?.value?.toIntOrNull()
                    deletions = diffResult.groups[2]?.value?.toIntOrNull()
                }
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
     * The command compares the current state of the file against the target branch,
     * returning information about insertions and deletions.
     *
     * @param fullPath The path to the file relative to the project root.
     * @return A string containing the git diff shortstat output, which includes the number
     *         of insertions and deletions in the format:
     *         "1 file changed, X insertions(+), Y deletions(-)"
     */
    private fun runDiffStat(fullPath: String): String {
        val targetSHA = systemWrapper.targetSHA()
        return systemCommandLine.exec("git diff --shortstat $targetSHA -- ${projectRoot.pathString}/$fullPath")
    }
}
