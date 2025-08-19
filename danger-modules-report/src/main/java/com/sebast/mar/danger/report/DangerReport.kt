package com.sebast.mar.danger.report

import com.sebast.mar.danger.report.internal.writer.ReportWriter

/**
 * Represents a danger report that can be generated.
 *
 * This class is responsible for orchestrating the generation of a report
 * using a provided [ReportWriter].
 *
 * @property reportWriter The [ReportWriter] used to output the report content.
 */
public class DangerReport internal constructor(
    private val reportWriter: ReportWriter,
) {
    public fun generateReport(): Unit = with(reportWriter) {
        writeSections()

        writeTable {
            writeHeaderRow()

            writeModules()
        }
    }
}
