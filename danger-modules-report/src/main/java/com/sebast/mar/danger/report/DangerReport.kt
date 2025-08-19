package com.sebast.mar.danger.report

import com.sebast.mar.danger.report.internal.writer.DangerWriter
import com.sebast.mar.danger.report.internal.writer.ReportBuilder

/**
 * Represents a danger report that can be generated.
 *
 * This class is responsible for orchestrating the generation of a report
 * using a provided [ReportBuilder].
 *
 * @property reportBuilder The [ReportBuilder] used to output the report content.
 */
public class DangerReport internal constructor(
    private val reportBuilder: ReportBuilder,
    private val dangerWriter: DangerWriter,
) {
    public fun writeReport() {
        with(reportBuilder) {
            sections()

            table {
                headerRow()

                moduleRows()
            }
        }

        dangerWriter.writeMarkdown(reportBuilder.build())
    }
}
