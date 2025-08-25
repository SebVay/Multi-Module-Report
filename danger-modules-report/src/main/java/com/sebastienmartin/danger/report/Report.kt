package com.sebastienmartin.danger.report

import com.sebastienmartin.danger.report.internal.SkipReport

/**
 * A base class for reporting issues found during the analysis.
 *
 * @property skipReport A lambda function that determines whether the report should be skipped.
 */
internal abstract class Report(
    private val skipReport: SkipReport,
) {
    fun write() {
        if (!skipReport()) {
            writeReport()
        }
    }

    protected abstract fun writeReport()
}
