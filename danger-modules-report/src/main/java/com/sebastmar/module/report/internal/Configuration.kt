package com.sebastmar.module.report.internal

import com.sebastmar.module.report.configuration.ModulesInterceptor
import com.sebastmar.module.report.configuration.ReportStrings

/**
 * Represents the configuration for generating and formatting reports.
 *
 * @property shouldLinkifyFiles Determines whether file paths in the report should be linkified.
 * @property showCircleIndicators Indicates whether circular indicators should be displayed in the report.
 * @property showLineIndicators Indicates whether line indicators should be displayed in the report.
 * @property modulesInterceptor Interface for intercepting and modifying the list of modules to be reported.
 * @property skipReportKeyword Keyword used to skip reporting.
 * @property reportStrings Contains customizable strings for the report.
 */
internal data class Configuration(
    val shouldLinkifyFiles: ShouldLinkifyFiles,
    val showCircleIndicators: ShowCircleIndicators,
    val showLineIndicators: ShowLineIndicators,
    val modulesInterceptor: ModulesInterceptor,
    val skipReportKeyword: SkipReportKeyword,
    val reportStrings: ReportStrings,
)

@JvmInline
internal value class ShouldLinkifyFiles(val value: Boolean)

@JvmInline
internal value class ShowCircleIndicators(val value: Boolean)

@JvmInline
internal value class ShowLineIndicators(val value: Boolean)

@JvmInline
internal value class SkipReportKeyword(val value: String)
