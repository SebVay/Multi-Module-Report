package com.sebastmar.danger.report.di

import com.sebastmar.danger.report.internal.Report
import com.sebastmar.danger.report.internal.TableReport
import com.sebastmar.danger.report.internal.domain.CommandLine
import com.sebastmar.danger.report.internal.helper.DangerCommandLine
import com.sebastmar.danger.report.internal.helper.DangerWrapper
import com.sebastmar.danger.report.internal.helper.DangerWriter
import com.sebastmar.danger.report.internal.helper.DangerWriterImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import systems.danger.kotlin.models.danger.DangerDSL

internal fun DangerDSL.dangerModule() = module {
    singleOf(::DangerWriterImpl) bind DangerWriter::class

    single<Report> {
        TableReport(
            reportBuilder = get(),
            dangerWriter = get(),
            skipReport = get(),
        )
    }

    single { DangerWrapper(this@dangerModule) }

    single<CommandLine> { DangerCommandLine(this@dangerModule) }
}
