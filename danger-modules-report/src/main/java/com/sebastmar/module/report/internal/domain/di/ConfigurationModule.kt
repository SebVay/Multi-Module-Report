package com.sebastmar.module.report.internal.domain.di

import com.sebastmar.module.report.configuration.ModulesInterceptor
import com.sebastmar.module.report.internal.Configuration
import com.sebastmar.module.report.internal.ShouldLinkifyFiles
import com.sebastmar.module.report.internal.ShowCircleIndicators
import com.sebastmar.module.report.internal.ShowLineIndicators
import com.sebastmar.module.report.internal.SkipReportKeyword
import com.sebastmar.module.report.internal.domain.StringProvider
import com.sebastmar.module.report.internal.domain.StringProviderImpl
import org.koin.dsl.module

/**
 * Provides access to the configuration values used within the module report library.
 */
internal fun configurationModule() = module {
    factory<ShouldLinkifyFiles> { get<Configuration>().shouldLinkifyFiles }
    factory<ShowCircleIndicators> { get<Configuration>().showCircleIndicators }
    factory<ShowLineIndicators> { get<Configuration>().showLineIndicators }
    factory<ModulesInterceptor> { get<Configuration>().modulesInterceptor }
    factory<SkipReportKeyword> { get<Configuration>().skipReportKeyword }
    factory<StringProvider> { StringProviderImpl(get<Configuration>().reportStrings) }
}
