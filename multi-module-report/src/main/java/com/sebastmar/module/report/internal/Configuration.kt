package com.sebastmar.module.report.internal

import com.sebastmar.module.report.configuration.ModulesInterceptor

internal data class Configuration(
    val shouldLinkifyFiles: ShouldLinkifyFiles,
    val showCircleIndicators: ShowCircleIndicators,
    val showLineIndicators: ShowLineIndicators,
    val modulesInterceptor: ModulesInterceptor,
    val skipReportKeyword: SkipReportKeyword,
    val reportStrings: ReportStrings,
)

internal data class ReportStrings(
    val topSection: String?,
    val bottomSection: String?,
    val projectRootModuleName: String,
    val unknownModuleName: String,
)

@JvmInline
internal value class ShouldLinkifyFiles(val value: Boolean)

@JvmInline
internal value class ShowCircleIndicators(val value: Boolean)

@JvmInline
internal value class ShowLineIndicators(val value: Boolean)

@JvmInline
internal value class SkipReportKeyword(val value: String)
