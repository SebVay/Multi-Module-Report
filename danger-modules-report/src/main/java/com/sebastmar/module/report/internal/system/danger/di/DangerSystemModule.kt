package com.sebastmar.module.report.internal.system.danger.di

import com.sebastmar.module.report.internal.system.SystemCommandLine
import com.sebastmar.module.report.internal.system.SystemWrapper
import com.sebastmar.module.report.internal.system.SystemWriter
import com.sebastmar.module.report.internal.system.danger.DangerCommandLine
import com.sebastmar.module.report.internal.system.danger.DangerWrapper
import com.sebastmar.module.report.internal.system.danger.DangerWriter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun dangerSystemModule() = module {
    singleOf(::DangerWriter) bind SystemWriter::class

    singleOf(::DangerWrapper) bind SystemWrapper::class

    singleOf(::DangerCommandLine) bind SystemCommandLine::class
}
