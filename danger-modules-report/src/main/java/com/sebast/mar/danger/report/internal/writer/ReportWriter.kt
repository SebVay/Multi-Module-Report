package com.sebast.mar.danger.report.internal.writer

import com.sebast.mar.danger.report.Module
import com.sebast.mar.danger.report.Status.Created
import com.sebast.mar.danger.report.Status.Deleted
import com.sebast.mar.danger.report.Status.Modified
import com.sebast.mar.danger.report.VersionedFile
import com.sebast.mar.danger.report.internal.GetModules

/**
 * Abstract base class for writing reports.
 * This class provides common functionality for report generation, such as accessing
 * created, modified, and deleted files, and defining abstract methods for writing
 * different parts of the report.
 *
 * Currently, it is only subclassed by GithubReportWriter.
 * Common logic may be extracted to this class in the future.
 */
internal abstract class ReportWriter(
    private val getModules: GetModules,
) {

    protected val modules: List<Module> by lazy { getModules() }

    protected val createdFiles: List<VersionedFile> by lazy {
        modules.flatMap(Module::files).filter { it.status == Created }
    }

    protected val modifiedFiles: List<VersionedFile> by lazy {
        modules.flatMap(Module::files).filter { it.status == Modified }
    }

    protected val deletedFiles: List<VersionedFile> by lazy {
        modules.flatMap(Module::files).filter { it.status == Deleted }
    }

    /**
     * Writes the sections of the report.
     */
    abstract fun writeSections()

    /**
     * Writes an HTML table to the report.
     * The table content is provided by the `block` lambda.
     *
     * @param block A lambda function that defines the content of the table.
     *              This lambda should append HTML table rows (`<tr>`) and cells (`<td>`, `<th>`)
     *              to the current output stream.
     */
    abstract fun writeTable(block: () -> Unit)

    /**
     * Writes the header row for the report table.
     * The header row includes columns for file status (e.g., Added, Modified, Deleted)
     * and the corresponding line changes.
     */
    abstract fun writeHeaderRow()

    /**
     * Writes the table rows for each module, displaying the number of added, modified, and deleted lines.
     */
    abstract fun writeModules()
}
