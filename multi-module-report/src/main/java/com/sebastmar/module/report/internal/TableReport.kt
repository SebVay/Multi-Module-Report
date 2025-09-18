package com.sebastmar.module.report.internal

import com.sebastmar.module.report.internal.domain.SkipReport
import com.sebastmar.module.report.system.SystemWriter

/**
 * Represents a table report that can be generated.
 *
 * This class is responsible for orchestrating the generation of a report
 * using a provided [TableReportBuilder].
 */
internal class TableReport internal constructor(
    reportBuilder: TableReportBuilder,
    skipReport: SkipReport,
    systemWriter: SystemWriter<String>,
) : BaseReport<String, TableReportBuilder>(
    skipReport = skipReport,
    reportBuilder = reportBuilder,
    systemWriter = systemWriter,
) {
    override fun TableReportBuilder.buildReport() {
        topSection()

        table {
            headerRow()

            moduleRows()
        }

        bottomSection()
    }
}
