package com.sebastmar.module.report.internal.di.koin

import com.sebastmar.module.report.internal.Configuration
import com.sebastmar.module.report.internal.TableReport
import com.sebastmar.module.report.internal.di.koin.ModuleReportKoinContext.koin
import com.sebastmar.module.report.internal.di.reportModule
import com.sebastmar.module.report.internal.host.HostType
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import systems.danger.kotlin.models.danger.DangerDSL

internal object ModuleReportKoinContext {

    private val koinApp = koinApplication {
        modules(reportModule())
    }

    internal val koin = koinApp.koin
}

internal fun DangerDSL.getReport(hostType: HostType, configuration: Configuration): TableReport {
    return koin
        .inject<TableReport> { parametersOf(this, hostType, configuration) }
        .value
}
