package com.sebast.mar.danger.report.github

import com.sebast.mar.danger.report.ReportConfig
import com.sebast.mar.danger.report.Status
import com.sebast.mar.danger.report.VersionedFile
import com.sebast.mar.danger.report.helper.table
import com.sebast.mar.danger.report.helper.td
import com.sebast.mar.danger.report.helper.th
import com.sebast.mar.danger.report.helper.tr
import com.sebast.mar.danger.report.internal.GetModules
import com.sebast.mar.danger.report.writer.ReportWriter

internal class GithubReportWriter(
    private val reportConfig: ReportConfig,
    getModules: GetModules,
) : ReportWriter(getModules) {

    private val writer = StringBuilder()

    /**
     * Writes the "Updated Modules" section to the report.
     * This section is only written if [ReportConfig.writeSections] is true.
     */
    override fun writeSections() {
        writer.takeIf { reportConfig.writeSections }?.append(
            """
            # Updated Modules
            """.trimIndent(),
        )
    }

    override fun writeTable(block: () -> Unit) {
        writer.table(block)
    }

    override fun writeHeaderRow(): Unit = with(writer) {
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
    override fun writeModules(): Unit = with(writer) {
        modules.forEach { module ->
            tr {
                td {
                    append("<div style=\"display: inline-block;\"><b>${module.name}</b></div>")
                }

                if (createdFiles.isNotEmpty()) {
                    td {
                        module.files
                            .filter { it.status == Status.Created }
                            .forEach {
                                append("🟢&nbsp;${it.getFileLink("danger.htmlUrl()")}<br>")
                            }
                    }
                }

                if (modifiedFiles.isNotEmpty()) {
                    td {
                        module.files
                            .filter { it.status == Status.Modified }
                            .forEach { versionedFile ->
                                append("🟡&nbsp;${versionedFile.getFileLink("danger.htmlUrl()")}<br>")
                            }
                    }
                }

                if (deletedFiles.isNotEmpty()) {
                    td {
                        module.files
                            .filter { it.status == Status.Deleted }
                            .forEach {
                                append("🔴&nbsp;${it.getFileLink("danger.htmlUrl()")}<br>")
                            }
                    }
                }
            }
        }
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
    private fun VersionedFile.getFileLink(pullRequestURL: String): String {
        return "<a href=\"$pullRequestURL/files#diff-$sha256Path\">$name</a>"
    }
}
