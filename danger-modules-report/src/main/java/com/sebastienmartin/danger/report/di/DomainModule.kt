package com.sebastienmartin.danger.report.di

import com.sebastienmartin.danger.report.ReportConfig
import com.sebastienmartin.danger.report.internal.domain.GetFiles
import com.sebastienmartin.danger.report.internal.domain.GetFilesImpl
import com.sebastienmartin.danger.report.internal.domain.GetModules
import com.sebastienmartin.danger.report.internal.domain.GetModulesImpl
import com.sebastienmartin.danger.report.internal.domain.GetPullRequest
import com.sebastienmartin.danger.report.internal.domain.GetPullRequestImpl
import com.sebastienmartin.danger.report.internal.domain.SkipReport
import com.sebastienmartin.danger.report.internal.domain.SkipReportImpl
import org.koin.dsl.module

internal val domainModule = module {

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
