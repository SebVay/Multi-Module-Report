package com.sebastienmartin.danger.report

import com.sebastienmartin.danger.report.internal.SkipReport
import com.sebastienmartin.danger.report.internal.helper.DangerWriter

/**
 * Represents a danger report that can be generated.
 *
 * This class is responsible for orchestrating the generation of a report
 * using a provided [ReportBuilder].
 *
 * @property reportBuilder The [ReportBuilder] used to output the report content.
 */
internal class DangerReport internal constructor(
    private val reportBuilder: ReportBuilder,
    private val dangerWriter: DangerWriter,
    skipReport: SkipReport,
) : Report(skipReport) {
    override fun writeReport() {
        with(reportBuilder) {
            topSection()

            table {
                headerRow()

                moduleRows()
            }

            bottomSection()
        }

        dangerWriter.writeMarkdown(reportBuilder.build())
    }
}
