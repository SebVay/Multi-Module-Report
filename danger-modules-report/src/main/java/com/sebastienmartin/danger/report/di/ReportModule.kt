package com.sebastienmartin.danger.report.di

import com.sebastienmartin.danger.report.ReportConfig
import com.sebastienmartin.danger.report.host.GithubReportBuilder
import com.sebastienmartin.danger.report.host.HostType
import com.sebastienmartin.danger.report.interceptor.ModulesInterceptor
import com.sebastienmartin.danger.report.internal.ReportBuilder
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
