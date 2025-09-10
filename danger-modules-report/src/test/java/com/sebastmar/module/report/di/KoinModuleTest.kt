package com.sebastmar.module.report.di

import com.sebastmar.module.report.internal.ShouldLinkifyFiles
import com.sebastmar.module.report.internal.ShowCircleIndicators
import com.sebastmar.module.report.internal.ShowLineIndicators
import com.sebastmar.module.report.internal.SkipReportKeyword
import com.sebastmar.module.report.internal.di.reportModule
import com.sebastmar.module.report.internal.system.danger.DangerCommandLine
import com.sebastmar.module.report.internal.system.danger.DangerWrapper
import org.junit.jupiter.api.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify
import systems.danger.kotlin.models.danger.DangerDSL

class KoinModuleTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `verify koin configuration for Github`() {
        reportModule()
            .verify(injections = skipDefinitions())
    }

    @OptIn(KoinExperimentalAPI::class)
    private fun skipDefinitions() = injectedParameters(
        definition<ShouldLinkifyFiles>(Boolean::class),
        definition<ShowCircleIndicators>(Boolean::class),
        definition<ShowLineIndicators>(Boolean::class),
        definition<SkipReportKeyword>(Boolean::class),
        definition<DangerWrapper>(DangerDSL::class),
        definition<DangerCommandLine>(DangerDSL::class),
    )
}
