package com.sebastmar.module.report.internal

import com.sebastmar.module.report.info.PullRequest
import com.sebastmar.module.report.internal.domain.GetPullRequest

internal abstract class ReportBuilder<Output>(
    protected val getPullRequest: GetPullRequest,
) {
    protected val pullRequest: PullRequest by lazy { getPullRequest() }

    /**
     * Builds the final report.
     *
     * @return The complete report as [Output].
     */
    abstract fun getReport(): Output
}
