package com.sebastmar.module.report.di

import com.sebastmar.module.report.internal.ShouldLinkifyFiles
import com.sebastmar.module.report.internal.ShowCircleIndicators
import com.sebastmar.module.report.internal.ShowLineIndicators
import com.sebastmar.module.report.internal.SkipReportKeyword
import com.sebastmar.module.report.internal.di.reportModule
import com.sebastmar.module.report.internal.domain.GetAllVersionedFilesImpl
import com.sebastmar.module.report.internal.domain.GetProjectRootImpl
import com.sebastmar.module.report.internal.domain.GetPullRequestImpl
import com.sebastmar.module.report.internal.domain.GetVersionedFilesImpl
import com.sebastmar.module.report.internal.domain.SkipReportImpl
import com.sebastmar.module.report.system.SystemCommandLine
import com.sebastmar.module.report.system.SystemWrapper
import org.junit.jupiter.api.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify

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
        definition<GetVersionedFilesImpl>(SystemCommandLine::class, SystemWrapper::class),
        definition<GetAllVersionedFilesImpl>(SystemWrapper::class),
        definition<GetPullRequestImpl>(SystemWrapper::class),
        definition<SkipReportImpl>(SystemWrapper::class),
        definition<GetProjectRootImpl>(SystemCommandLine::class),
    )
}
