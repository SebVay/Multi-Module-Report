package com.sebast.mar.danger.report.internal.writer

import com.sebast.mar.danger.report.info.PullRequest
import com.sebast.mar.danger.report.internal.GetPullRequest

/**
 * Abstract base class for writing reports.
 * This class provides common functionality for report generation, such as accessing
 * created, modified, and deleted files, and defining abstract methods for writing
 * different parts of the report.
 *
 * Currently, it is only subclassed by GithubReportWriter.
 * Common logic may be extracted to this class in the future.
 */
internal abstract class ReportBuilder(
    private val getPullRequest: GetPullRequest,
) {

    protected val pullRequest: PullRequest by lazy { getPullRequest() }

    /**
     * Writes the top section of the report if it exists.
     * If the top section in reportConfig is null or empty, nothing will be written.
     * The section content is appended directly to the writer without any modifications.
     */
    abstract fun sections()

    /**
     * Writes an HTML table to the report.
     * The table content is provided by the `block` lambda.
     *
     * @param block A lambda function that defines the content of the table.
     *              This lambda should append HTML table rows (`<tr>`) and cells (`<td>`, `<th>`)
     *              to the current output stream.
     */
    abstract fun table(block: () -> Unit)

    /**
     * Writes the header row for the report table.
     * The header row includes columns for file status (e.g., Added, Modified, Deleted)
     * and the corresponding line changes.
     */
    abstract fun headerRow()

    /**
     * Writes the table rows for each module, displaying the number of added, modified, and deleted lines.
     */
    abstract fun moduleRows()

    abstract fun build(): String
}
