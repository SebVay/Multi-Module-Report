package com.sebastmar.danger.report.internal.domain

import com.sebastmar.danger.report.ReportConfig
import com.sebastmar.danger.report.internal.helper.DangerWrapper

/**
 * Interface for determining if a report generation should be skipped.
 */
internal interface SkipReport {
    operator fun invoke(): Boolean
}

/**
 * Checks if the pull request body contains a specific keyword defined in [ReportConfig] to determine
 * if the report generation should be skipped.
 */
internal class SkipReportImpl(
    private val danger: DangerWrapper,
    private val reportConfig: ReportConfig,
) : SkipReport {
    override fun invoke(): Boolean = danger.prBody().contains(reportConfig.skipReportKeyword)
}
