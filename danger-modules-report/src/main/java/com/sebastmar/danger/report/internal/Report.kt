package com.sebastmar.danger.report.internal

import com.sebastmar.danger.report.internal.domain.SkipReport

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
