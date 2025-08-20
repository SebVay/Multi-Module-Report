@file:Suppress("MemberVisibilityCanBePrivate")

package com.sebast.mar.danger.report

import com.sebast.mar.danger.report.interceptor.ModulesInterceptor
import com.sebast.mar.danger.report.interceptor.NoOpModulesInterceptor


@DslMarker
public annotation class ReportDsl

/**
 * A builder class for configuring reports.
 *
 * This class provides a DSL for configuring various aspects of a report,
 * such as whether to write sections and how to intercept modules.
 */
@ReportDsl
public class ReportConfigBuilder internal constructor(
    private val isHostCorrect: Boolean,
) {
    /**
     * A text to be displayed at the top of the report, markdown compatible.
     *
     * By default: `# Updated Modules`
     *
     * If set to `null`, no top section will be displayed.
     */
    public var topSection: String? = "# Updated Modules"

    /**
     * Determines whether links to the diff page for each file are added to the report.
     * When `true`, links are added. When `false`, links are omitted.
     * Defaults to `true`.
     */
    public var linkifyFiles: Boolean = true

    /**
     * Toggles whether circle indicators are displayed to the left of each file name.
     * Default value is `true`.
     */
    public var showCircleIndicators: Boolean = true

    /**
     * Toggles whether line added/removed indicators are displayed to the right of the "Added", "Modified", "Deleted" sections.
     * Default value is `true`.
     */
    public var showLineIndicators: Boolean = true

    /**
     * Allows modules to be intercepted, modified, or omitted from the report.
     * By default, this is a [NoOpModulesInterceptor] which performs no interception.
     * @see ModulesInterceptor
     */
    public var modulesInterceptor: ModulesInterceptor = NoOpModulesInterceptor

    internal fun build(): ReportConfig = ReportConfig(
        isHostCorrect = isHostCorrect,
        topSection = topSection,
        linkifyFiles = linkifyFiles,
        showCircleIndicators = showCircleIndicators,
        showLineIndicators = showLineIndicators,
        modulesInterceptor = modulesInterceptor,
    )
}

/**
 * Represents the configuration of the report generator.
 *
 * @property topSection A text to be displayed at the top of the report, markdown compatible.
 * @property linkifyFiles Whether to convert file names to links to their diffs.
 * @property showCircleIndicators Whether to show circle indicators next to file names.
 * @property showLineIndicators Whether to show line added/removed indicators in the header sections.
 * @property modulesInterceptor Allows modules to be intercepted, modified, or omitted from the report.
 */
internal data class ReportConfig(
    val isHostCorrect: Boolean,
    val topSection: String?,
    val linkifyFiles: Boolean,
    val showCircleIndicators: Boolean,
    val showLineIndicators: Boolean,
    val modulesInterceptor: ModulesInterceptor,
)
