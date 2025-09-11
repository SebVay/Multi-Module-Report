package com.sebastmar.module.report.internal.di

import com.sebastmar.module.report.internal.TableReport
import com.sebastmar.module.report.internal.TableReportBuilder
import com.sebastmar.module.report.internal.domain.di.domainModule
import com.sebastmar.module.report.internal.host.HostType
import com.sebastmar.module.report.internal.host.github.GithubTableReportBuilder
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal fun reportModule(): Module = module {
    factoryOf(::TableReport)

    factory<TableReportBuilder> {
        val hostType: HostType = get()

        when (hostType) {
            HostType.Github -> GithubTableReportBuilder(get(), get(), get(), get(), get(), get())
        }
    }
}.apply {
    includes(domainModule())
}
