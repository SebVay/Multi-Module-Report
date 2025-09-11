package com.sebastmar.module.report.internal.domain.di

import com.sebastmar.module.report.internal.domain.GetAllVersionedFiles
import com.sebastmar.module.report.internal.domain.GetAllVersionedFilesImpl
import com.sebastmar.module.report.internal.domain.GetProjectRoot
import com.sebastmar.module.report.internal.domain.GetProjectRootImpl
import com.sebastmar.module.report.internal.domain.GetPullRequest
import com.sebastmar.module.report.internal.domain.GetPullRequestImpl
import com.sebastmar.module.report.internal.domain.GetUpdatedModules
import com.sebastmar.module.report.internal.domain.GetUpdatedModulesImpl
import com.sebastmar.module.report.internal.domain.GetVersionedFiles
import com.sebastmar.module.report.internal.domain.GetVersionedFilesImpl
import com.sebastmar.module.report.internal.domain.SkipReport
import com.sebastmar.module.report.internal.domain.SkipReportImpl
import com.sebastmar.module.report.internal.system.di.dangerSystemModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun domainModule() = module {
    factoryOf(::GetVersionedFilesImpl) bind GetVersionedFiles::class

    factoryOf(::GetAllVersionedFilesImpl) bind GetAllVersionedFiles::class

    factoryOf(::GetUpdatedModulesImpl) bind GetUpdatedModules::class

    factoryOf(::GetPullRequestImpl) bind GetPullRequest::class

    factoryOf(::SkipReportImpl) bind SkipReport::class

    factoryOf(::GetProjectRootImpl) bind GetProjectRoot::class
}.apply {
    includes(
        dangerSystemModule(),
        configurationModule(),
    )
}
