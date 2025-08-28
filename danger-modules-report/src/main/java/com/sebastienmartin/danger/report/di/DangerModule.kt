package com.sebastienmartin.danger.report.di

import com.sebastienmartin.danger.report.internal.DangerReport
import com.sebastienmartin.danger.report.internal.Report
import com.sebastienmartin.danger.report.internal.domain.CommandLine
import com.sebastienmartin.danger.report.internal.helper.DangerCommandLine
import com.sebastienmartin.danger.report.internal.helper.DangerWrapper
import com.sebastienmartin.danger.report.internal.helper.DangerWriter
import com.sebastienmartin.danger.report.internal.helper.DangerWriterImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import systems.danger.kotlin.models.danger.DangerDSL

internal fun DangerDSL.dangerModule() = module {
    singleOf(::DangerWriterImpl) bind DangerWriter::class

    single<Report> {
        DangerReport(
            reportBuilder = get(),
            dangerWriter = get(),
            skipReport = get(),
        )
    }

    single { DangerWrapper(this@dangerModule) }

    single<CommandLine> { DangerCommandLine(this@dangerModule) }
}
