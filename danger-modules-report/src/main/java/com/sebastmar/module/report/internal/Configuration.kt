package com.sebastmar.module.report.internal

import com.sebastmar.module.report.configuration.ModulesInterceptor

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

/**
 * Class used to customize the strings in the report.
 *
 * All parameters will use their default values if not provided.
 *
 * @param topSection A text to be displayed at the top of the report, markdown compatible.
 *  By default: `# Updated Modules`
 *  If set to `null`, no top section will be displayed.
 * @param bottomSection A text to be displayed at the bottom of the report, markdown compatible.
 * By default: `null` (no bottom section will be displayed)
 * If set to a String, this text will be displayed at the bottom of the report.
 * @param incorrectHostWarning A text to be displayed if the report is generated outside of a its host context.
 * By default: a warning message.
 * If set to `null`, no warning will be displayed.
 * @param projectRootModuleName The name to be displayed for the project's root module.
 * By default: `Project's Root`
 * @param unknownModuleName The name to be displayed for paths that doesn't belong to any module.
 * By default: `Others`
 */
internal data class ReportStrings(
    val topSection: String?,
    val bottomSection: String?,
    val incorrectHostWarning: String?,
    val projectRootModuleName: String,
    val unknownModuleName: String
)

@JvmInline
internal value class ShouldLinkifyFiles(val value: Boolean)

@JvmInline
internal value class ShowCircleIndicators(val value: Boolean)

@JvmInline
internal value class ShowLineIndicators(val value: Boolean)

@JvmInline
internal value class SkipReportKeyword(val value: String)
