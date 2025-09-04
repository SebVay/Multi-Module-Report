package com.sebastmar.danger.report.di

import com.sebastmar.danger.report.internal.domain.GetFiles
import com.sebastmar.danger.report.internal.domain.GetFilesImpl
import com.sebastmar.danger.report.internal.domain.GetProjectRoot
import com.sebastmar.danger.report.internal.domain.GetProjectRootImpl
import com.sebastmar.danger.report.internal.domain.GetPullRequest
import com.sebastmar.danger.report.internal.domain.GetPullRequestImpl
import com.sebastmar.danger.report.internal.domain.GetUpdatedModules
import com.sebastmar.danger.report.internal.domain.GetUpdatedModulesImpl
import com.sebastmar.danger.report.internal.domain.SkipReport
import com.sebastmar.danger.report.internal.domain.SkipReportImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun domainModule() = module {
    singleOf(::GetFilesImpl) bind GetFiles::class

    singleOf(::GetUpdatedModulesImpl) bind GetUpdatedModules::class

    singleOf(::GetPullRequestImpl) bind GetPullRequest::class

    singleOf(::SkipReportImpl) bind SkipReport::class

    singleOf(::GetProjectRootImpl) bind GetProjectRoot::class
}
