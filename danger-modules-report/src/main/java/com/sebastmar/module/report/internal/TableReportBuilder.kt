package com.sebastmar.module.report.internal

import com.sebastmar.module.report.internal.domain.GetPullRequest

/**
 * Abstract base class for building table-based reports.
 * This class provides common functionality for generating reports that are primarily
 * structured as HTML tables.
 *
 * It is designed to be subclassed by specific report writers that need to present
 * data in a tabular format (e.g., GithubReportWriter).
 * Common table-building logic may be extracted to this class in the future.
 */
internal abstract class TableReportBuilder(
    getPullRequest: GetPullRequest,
) : ReportBuilder<String>(getPullRequest) {

    /**
     * Writes the top section of the report if it exists.
     * If the top section in reportConfig is null or empty, nothing will be written.
     */
    abstract fun topSection()

    /**
     * Writes the bottom section of the report if it exists.
     * If the bottom section in reportConfig is null or empty, nothing will be written.
     */
    abstract fun bottomSection()

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
}
