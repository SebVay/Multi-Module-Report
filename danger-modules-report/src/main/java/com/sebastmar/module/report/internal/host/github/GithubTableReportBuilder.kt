package com.sebastmar.module.report.internal.host.github

import com.sebastmar.module.report.info.Module
import com.sebastmar.module.report.info.PullRequest
import com.sebastmar.module.report.info.VersionedFile
import com.sebastmar.module.report.info.getDeletedLines
import com.sebastmar.module.report.info.getInsertedLines
import com.sebastmar.module.report.internal.ShouldLinkifyFiles
import com.sebastmar.module.report.internal.ShowCircleIndicators
import com.sebastmar.module.report.internal.ShowLineIndicators
import com.sebastmar.module.report.internal.TableReportBuilder
import com.sebastmar.module.report.internal.domain.GetPullRequest
import com.sebastmar.module.report.internal.domain.StringProvider
import com.sebastmar.module.report.internal.ext.table
import com.sebastmar.module.report.internal.ext.td
import com.sebastmar.module.report.internal.ext.th
import com.sebastmar.module.report.internal.ext.tr
import com.sebastmar.module.report.internal.system.SystemWrapper

/**
 * A builder class for creating GitHub-specific table-based reports.
 *
 * This class is responsible for creating a structured HTML report designed for GitHub.
 * The report includes sections such as a top warning for incorrect hosts, summaries of
 * added, modified, and deleted files, and module-specific details presented in tabular format.
 *
 * The builder supports customizable options, such as displaying line indicators, linking files
 * to their specific diff locations, and showing colored circle indicators for file status.
 *
 * @constructor Initializes the builder with dependencies and configuration options for the report.
 * @param stringProvider Provides string resources for various report sections such as top and bottom sections.
 * @param showLineIndicators Controls whether to show the count of added and deleted lines for files.
 * @param shouldLinkifyFiles Determines whether files should be linked to their respective GitHub file diffs.
 * @param showCircleIndicators Specifies whether to display colored circle indicators for file status
 *                             (added, modified, deleted).
 * @param systemWrapper Provides system-level details, such as the host platform and pull request's environment.
 * @param builder A mutable string builder for composing the HTML content of the report.
 * @param getPullRequest A function that retrieves the current pull request to generate the report for.
 */
@Suppress("LongParameterList")
internal class GithubTableReportBuilder(
    private val stringProvider: StringProvider,
    private val showLineIndicators: ShowLineIndicators,
    private val shouldLinkifyFiles: ShouldLinkifyFiles,
    private val showCircleIndicators: ShowCircleIndicators,
    private val systemWrapper: SystemWrapper,
    getPullRequest: GetPullRequest,
    private val builder: StringBuilder = StringBuilder(),
) : TableReportBuilder(getPullRequest) {

    override fun topSection(): Unit = with(builder) {
        if (systemWrapper.onGithub().not()) {
            appendLine(stringProvider.incorrectHostWarning())
        }

        val topSection = stringProvider.topSection()
        if (topSection != null) {
            appendLine(topSection)
        }
    }

    override fun bottomSection() = with(builder) {
        val bottomSection = stringProvider.bottomSection()
        if (bottomSection != null) {
            appendLine(bottomSection)
        }
    }

    override fun table(block: () -> Unit): Unit = with(builder) {
        table(block)
        append("\n\n")
    }

    override fun headerRow() = with(builder) {
        tr {
            th()

            if (pullRequest.createdFiles.isNotEmpty()) {
                th {
                    append("Added")

                    if (showLineIndicators.value) {
                        val totalAdded = "+" + pullRequest.createdFiles.getInsertedLines()

                        append(" (${totalAdded.greenFlavor()})")
                    }
                }
            }

            if (pullRequest.modifiedFiles.isNotEmpty()) {
                th {
                    append("Modified")

                    if (showLineIndicators.value) {
                        val totalAdded = "+" + pullRequest.modifiedFiles.getInsertedLines()
                        val totalDeleted = "-" + pullRequest.modifiedFiles.getDeletedLines()

                        append(" (${totalAdded.greenFlavor()} / ${totalDeleted.redFlavor()})")
                    }
                }
            }

            if (pullRequest.deletedFiles.isNotEmpty()) {
                th {
                    append("Deleted")

                    if (showLineIndicators.value) {
                        val totalDeleted = "-" + pullRequest.deletedFiles.getDeletedLines()

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
    override fun moduleRows() = with(builder) {
        pullRequest.modules.sortedBy(Module::type).forEach { module ->
            tr {
                td {
                    append("<div style=\"display: inline-block;\"><b>${module.name}</b></div>")
                }

                if (pullRequest.createdFiles.isNotEmpty()) {
                    filesColumn(module.createdFiles, "🟢")
                }

                if (pullRequest.modifiedFiles.isNotEmpty()) {
                    filesColumn(module.modifiedFiles, "🟡")
                }

                if (pullRequest.deletedFiles.isNotEmpty()) {
                    filesColumn(module.deletedFiles, "🔴")
                }
            }
        }
    }

    override fun getReport(): String = builder.toString()

    private fun StringBuilder.filesColumn(
        moduleFiles: List<VersionedFile>,
        circleIndicator: String,
    ) {
        td {
            moduleFiles
                .map { file ->
                    when (shouldLinkifyFiles.value) {
                        true -> pullRequest.getLinkOf(file)
                        false -> file.name
                    }
                }
                .forEach { fileName ->
                    if (showCircleIndicators.value) {
                        append("$circleIndicator&nbsp;")
                    }
                    append("$fileName<br>")
                }
        }
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
}
