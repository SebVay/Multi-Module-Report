package com.sebast.mar.danger.report.github

import com.sebast.mar.danger.report.ReportConfig
import com.sebast.mar.danger.report.info.PullRequest
import com.sebast.mar.danger.report.info.VersionedFile
import com.sebast.mar.danger.report.info.getDeletedLines
import com.sebast.mar.danger.report.info.getInsertedLines
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

    override fun topSection() = with(writer) {
        if (reportConfig.isHostCorrect.not()) {
            appendLine(INCORRECT_HOST_WARNING)
        }

        if (!reportConfig.topSection.isNullOrEmpty()) {
            appendLine(reportConfig.topSection)
        }
    }

    override fun bottomSection() = with(writer) {
        if (reportConfig.bottomSection != null) {
            appendLine(reportConfig.bottomSection)
        }
    }

    override fun table(block: () -> Unit) {
        writer.table(block)
    }

    override fun headerRow() = with(writer) {
        val createdFiles = pullRequest.createdFiles
        val modifiedFiles = pullRequest.modifiedFiles
        val deletedFiles = pullRequest.deletedFiles

        tr {
            th()

            if (createdFiles.isNotEmpty()) {
                th {
                    append("Added")

                    if (reportConfig.showLineIndicators) {
                        val totalAdded = "+" + createdFiles.getInsertedLines()

                        append(" (${totalAdded.greenFlavor()})")
                    }
                }
            }

            if (modifiedFiles.isNotEmpty()) {
                th {
                    append("Modified")

                    if (reportConfig.showLineIndicators) {
                        val totalAdded = "+" + modifiedFiles.getInsertedLines()
                        val totalDeleted = "-" + modifiedFiles.getDeletedLines()

                        append(" (${totalAdded.greenFlavor()} / ${totalDeleted.redFlavor()})")
                    }
                }
            }

            if (deletedFiles.isNotEmpty()) {
                th {
                    append("Deleted")

                    if (reportConfig.showLineIndicators) {
                        val totalDeleted = "-" + deletedFiles.getDeletedLines()

                        append(" (${totalDeleted.redFlavor()})")
                    }
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
    override fun moduleRows() = with(writer) {
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
                    filesColumn(module.createdFiles, "🟢")
                }

                if (modifiedFiles.isNotEmpty()) {
                    filesColumn(module.modifiedFiles, "🟡")
                }

                if (deletedFiles.isNotEmpty()) {
                    filesColumn(module.deletedFiles, "🔴")
                }
            }
        }
    }

    private fun StringBuilder.filesColumn(
        moduleFiles: List<VersionedFile>,
        circleIndicator: String,
    ) {
        td {
            moduleFiles
                .map { file ->
                    when (reportConfig.linkifyFiles) {
                        true -> pullRequest.getLinkOf(file)
                        false -> file.name
                    }
                }
                .forEach { fileName ->
                    if (reportConfig.showCircleIndicators) {
                        append("$circleIndicator&nbsp;")
                    }
                    append("$fileName<br>")
                }
        }
    }

    override fun build(): String {
        return writer.toString()
    }

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
        return "<a href=\"$htmlLink/files#diff-${file.sha256Path}\">${file.name}</a>"
    }

    companion object {
        private val INCORRECT_HOST_WARNING = """
            🚧🚧🚧
            ### `githubModuleReport` has been called outside a Github context.
            Use it only for local testing, some functionalities or html link could be missing.
            🚧🚧🚧
        """.trimIndent()
    }
}
