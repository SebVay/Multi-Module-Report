package com.sebast.mar.danger.report.github

import com.sebast.mar.danger.report.ReportConfig
import com.sebast.mar.danger.report.info.PullRequest
import com.sebast.mar.danger.report.info.VersionedFile
import com.sebast.mar.danger.report.info.VersionedFile.Status
import com.sebast.mar.danger.report.internal.GetPullRequest
import com.sebast.mar.danger.report.internal.helper.table
import com.sebast.mar.danger.report.internal.helper.td
import com.sebast.mar.danger.report.internal.helper.th
import com.sebast.mar.danger.report.internal.helper.tr
import com.sebast.mar.danger.report.internal.writer.ReportBuilder

internal class GithubReportBuilder(
    private val reportConfig: ReportConfig,
    getPullRequest: GetPullRequest,
) : ReportBuilder(getPullRequest) {

    private val writer = StringBuilder()

    override fun sections(): Unit = with(writer) {
        if (reportConfig.isHostIncorrect) {
            appendLine(INCORRECT_HOST_WARNING)
        }

        if (!reportConfig.topSection.isNullOrEmpty()) {
            appendLine(reportConfig.topSection)
        }
    }

    override fun table(block: () -> Unit) {
        writer.table(block)
    }

    override fun headerRow(): Unit = with(writer) {
        val createdFiles = pullRequest.createdFiles
        val modifiedFiles = pullRequest.modifiedFiles
        val deletedFiles = pullRequest.deletedFiles

        tr {
            th()

            if (createdFiles.isNotEmpty()) {
                val totalAdded = "+" + getInsertedLines(createdFiles, Status.Created)

                th {
                    append("Added (${totalAdded.greenFlavor()})")
                }
            }

            if (modifiedFiles.isNotEmpty()) {
                val totalAdded = "+" + getInsertedLines(modifiedFiles, Status.Modified)
                val totalDeleted = "-" + getDeletedLines(modifiedFiles, Status.Modified)

                th {
                    append("Modified (${totalAdded.greenFlavor()} / ${totalDeleted.redFlavor()})")
                }
            }

            if (deletedFiles.isNotEmpty()) {
                val totalDeleted = "-" + getDeletedLines(deletedFiles, Status.Deleted)

                th {
                    append("Deleted (${totalDeleted.redFlavor()})")
                }
            }
        }
    }

    /**
     * Writes the module information to the HTML report.
     * Each module is displayed in a table row with its name and a list of created, modified, and removed files.
     * Created files are marked with a green circle, modified files with a yellow circle,
     * and removed files with a red circle.
     * Each file is linked to its corresponding URL.
     */
    override fun moduleRows(): Unit = with(writer) {
        val modules = pullRequest.modules
        val createdFiles = pullRequest.createdFiles
        val modifiedFiles = pullRequest.modifiedFiles
        val deletedFiles = pullRequest.deletedFiles

        modules.forEach { module ->
            tr {
                td {
                    append("<div style=\"display: inline-block;\"><b>${module.name}</b></div>")
                }

                if (createdFiles.isNotEmpty()) {
                    td {
                        module.createdFiles
                            .map { file -> pullRequest.getLinkOf(file) }
                            .forEach { link -> append("🟢&nbsp;$link<br>") }
                    }
                }

                if (modifiedFiles.isNotEmpty()) {
                    td {
                        module.modifiedFiles
                            .map { file -> pullRequest.getLinkOf(file) }
                            .forEach { link -> append("🟡&nbsp;$link<br>") }
                    }
                }

                if (deletedFiles.isNotEmpty()) {
                    td {
                        module.deletedFiles
                            .map { file -> pullRequest.getLinkOf(file) }
                            .forEach { link -> append("🔴&nbsp;$link<br>") }
                    }
                }
            }
        }
    }

    override fun build(): String {
        return writer.toString()
    }

    /**
     * Calculates the total number of inserted lines for files with a specific status.
     */
    private fun getInsertedLines(
        versionedFiles: List<VersionedFile>,
        status: Status,
    ): Int = versionedFiles
        .filter { it.status == status }
        .sumOf { it.insertions ?: 0 }

    /**
     * Calculates the total number of deleted lines for files with a specific status.
     */
    private fun getDeletedLines(
        versionedFiles: List<VersionedFile>,
        status: Status,
    ) = versionedFiles
        .filter { it.status == status }
        .sumOf { it.deletions ?: 0 }

    /**
     * Formats the string to be displayed in green color using LaTeX-like syntax.
     * This syntax is compatible with Github markdown.
     */
    private fun String.greenFlavor(): String = "\$\\color{Green}{\\textsf{$this}}\$"

    /**
     * Formats the string to be displayed in red color using LaTeX-like syntax.
     * This syntax is compatible with Github markdown.
     */
    private fun String.redFlavor() = "\$\\color{Red}{\\textsf{$this}}\$"

    /**
     * Generates an HTML hyperlink to the specific file diff on GitHub.
     *
     * The link is constructed using the pull request's HTML URL and the SHA-256 hash
     * of the file's path, which is a format GitHub uses to identify specific file changes
     * within a pull request's "Files changed" tab.
     *
     * @return An HTML `<a>` tag string that links to the file diff on GitHub.
     *         The link text will be the `name` of the file.
     */
    private fun PullRequest.getLinkOf(file: VersionedFile): String {
        return "<a href=\"${this.htmlLink}/files#diff-${file.sha256Path}\">${file.name}</a>"
    }

    companion object {
        private val INCORRECT_HOST_WARNING = """
            🚧🚧🚧
            ### githubModuleReport has been called outside a Github context.
            - Use it only for local testing, some functionalities or html link could be missing.
            🚧🚧🚧
        """.trimIndent()
    }
}
