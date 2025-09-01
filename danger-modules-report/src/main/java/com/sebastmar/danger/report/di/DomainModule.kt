package com.sebastmar.danger.report.di

import com.sebastmar.danger.report.ReportConfig
import com.sebastmar.danger.report.internal.domain.GetFiles
import com.sebastmar.danger.report.internal.domain.GetFilesImpl
import com.sebastmar.danger.report.internal.domain.GetModules
import com.sebastmar.danger.report.internal.domain.GetModulesImpl
import com.sebastmar.danger.report.internal.domain.GetPullRequest
import com.sebastmar.danger.report.internal.domain.GetPullRequestImpl
import com.sebastmar.danger.report.internal.domain.SkipReport
import com.sebastmar.danger.report.internal.domain.SkipReportImpl
import org.koin.dsl.module

internal fun domainModule() = module {
    single<GetFiles> {
        GetFilesImpl(
            commandLine = get(),
            runShortStatCommand = get<ReportConfig>().showLineIndicators,
        )
    }

    single<GetModules> {
        GetModulesImpl(
            danger = get(),
            getFiles = get(),
            modulesInterceptor = get(),
        )
    }

    single<GetPullRequest> {
        GetPullRequestImpl(
            danger = get(),
            getModules = get(),
        )
    }

    single<SkipReport> {
        SkipReportImpl(
            danger = get(),
            reportConfig = get(),
        )
    }
}
