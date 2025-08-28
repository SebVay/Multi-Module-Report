package com.sebastienmartin.danger.report.internal.domain

import com.sebastienmartin.danger.report.info.PullRequest
import com.sebastienmartin.danger.report.internal.helper.DangerWrapper

internal interface GetPullRequest {
    operator fun invoke(): PullRequest
}

internal class GetPullRequestImpl(
    private val danger: DangerWrapper,
    private val getModules: GetModules,
) : GetPullRequest {
    override fun invoke(): PullRequest {
        val modules = getModules()

        return PullRequest(
            htmlLink = danger.htmlLink(),
            modules = modules,
        )
    }
}
