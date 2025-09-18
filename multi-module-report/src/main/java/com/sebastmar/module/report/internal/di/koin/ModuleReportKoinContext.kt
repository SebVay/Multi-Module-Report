package com.sebastmar.module.report.internal.di.koin

import com.sebastmar.module.report.internal.di.reportModule
import org.koin.dsl.koinApplication

internal object ModuleReportKoinContext {

    private val koinApp = koinApplication {
        modules(reportModule())
    }

    internal val koin = koinApp.koin
}
