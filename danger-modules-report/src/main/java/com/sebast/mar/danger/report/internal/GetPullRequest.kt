package com.sebast.mar.danger.report.internal

import com.sebast.mar.danger.report.info.PullRequest
import com.sebast.mar.danger.report.internal.helper.DangerWrapper

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
