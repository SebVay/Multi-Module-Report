package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.info.PullRequest
import com.sebastmar.module.report.system.SystemWrapper

internal interface GetPullRequest {
    operator fun invoke(): PullRequest
}

internal class GetPullRequestImpl(
    private val systemWrapper: SystemWrapper,
    private val getUpdatedModules: GetUpdatedModules,
) : GetPullRequest {
    override fun invoke(): PullRequest {
        val modules = getUpdatedModules()

        return PullRequest(
            htmlLink = systemWrapper.htmlLink(),
            modules = modules,
        )
    }
}
