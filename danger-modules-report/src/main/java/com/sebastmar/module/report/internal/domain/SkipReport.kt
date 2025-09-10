package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.internal.SkipReportKeyword
import com.sebastmar.module.report.internal.system.SystemWrapper

/**
 * Interface for determining if a report generation should be skipped.
 */
internal interface SkipReport {
    operator fun invoke(): Boolean
}

/**
 * Checks if the pull request body contains a specific keyword defined in [SkipReportKeyword] to determine
 * if the report generation should be skipped.
 */
internal class SkipReportImpl(
    private val systemWrapper: SystemWrapper,
    private val skipReportKeyword: SkipReportKeyword,
) : SkipReport {
    override fun invoke(): Boolean = systemWrapper.prBody().contains(skipReportKeyword.value)
}
