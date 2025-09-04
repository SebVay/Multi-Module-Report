package com.sebastmar.danger.report.internal.domain

import com.sebastmar.danger.report.info.PullRequest
import com.sebastmar.danger.report.internal.helper.DangerWrapper

internal interface GetPullRequest {
    operator fun invoke(): PullRequest
}

internal class GetPullRequestImpl(
    private val danger: DangerWrapper,
    private val getUpdatedModules: GetUpdatedModules,
) : GetPullRequest {
    override fun invoke(): PullRequest {
        val modules = getUpdatedModules()

        return PullRequest(
            htmlLink = danger.htmlLink(),
            modules = modules,
        )
    }
}
