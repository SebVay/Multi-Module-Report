package com.sebastmar.danger.report.di

import com.sebastmar.danger.report.ReportConfig
import com.sebastmar.danger.report.host.GithubReportBuilder
import com.sebastmar.danger.report.host.HostType
import com.sebastmar.danger.report.interceptor.ModulesInterceptor
import com.sebastmar.danger.report.internal.ReportBuilder
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun reportModule(
    reportConfig: ReportConfig,
    hostType: HostType,
): Module {
    return module {
        single<ReportConfig> { reportConfig }

        single<ModulesInterceptor> {
            get<ReportConfig>().modulesInterceptor
        }

        singleOf(
            when (hostType) {
                HostType.Github -> ::GithubReportBuilder
            },
        ) bind ReportBuilder::class
    }
}
