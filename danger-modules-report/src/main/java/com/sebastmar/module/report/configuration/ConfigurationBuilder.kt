@file:Suppress("MemberVisibilityCanBePrivate")

package com.sebastmar.module.report.configuration

import com.sebastmar.module.report.info.Module
import com.sebastmar.module.report.internal.Configuration
import com.sebastmar.module.report.internal.ShouldLinkifyFiles
import com.sebastmar.module.report.internal.ShowCircleIndicators
import com.sebastmar.module.report.internal.ShowLineIndicators
import com.sebastmar.module.report.internal.SkipReportKeyword

@DslMarker
public annotation class ConfigurationDsl

/**
 * A builder class for configuring reports.
 *
 * This class provides a DSL for configuring various aspects of a report,
 * such as whether to write sections and how to intercept modules.
 */
@ConfigurationDsl
public class ConfigurationBuilder internal constructor() {

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
     * Toggles whether the (+/-) ine indicator is displayed to next to the "Added", "Modified", "Deleted" sections.
     * Default value is `true`.
     */
    public var showLineIndicators: Boolean = true

    /**
     * Allows modules to be intercepted, modified, or omitted from the report.
     * By default, this is a [NoOpModulesInterceptor] which performs no interception.
     * @see ModulesInterceptor
     */
    private var modulesInterceptor: ModulesInterceptor = NoOpModulesInterceptor

    /**
     * Defines the strings used within the report.
     * This allows for customization and localization of the text displayed in the generated report.
     * Defaults to an instance of [ReportStrings] with default English strings.
     * @see ReportStrings
     */
    public var reportStrings: ReportStrings = ReportStrings()

    /**
     * Sets an interceptor for the list of modules used in the report generation process.
     *
     * The interceptor allows for customization of the modules list by modifying or filtering
     * the modules before they are processed further.
     *
     * Example:
     * ```kotlin
     * modulesInterceptor { modules ->
     *     modules.filterNot { it.name == "internal-module" } // Exclude a specific module
     * }
     * ```
     *
     * @param block A lambda function that takes the current list of [Module]s as input
     *              and returns a new, possibly modified, list of [Module]s.
     *              The returned list will be used for report generation.
     */
    public fun modulesInterceptor(block: (List<Module>) -> List<Module>) {
        modulesInterceptor = ModulesInterceptor(block)
    }

    /**
     * If the PR description contains these keywords, the report generation will be skipped.
     * Default value is `module-no-report`.
     */
    public var skipReportKeyword: String = "module-no-report"

    internal fun build(): Configuration = Configuration(
        shouldLinkifyFiles = ShouldLinkifyFiles(linkifyFiles),
        showCircleIndicators = ShowCircleIndicators(showCircleIndicators),
        showLineIndicators = ShowLineIndicators(showLineIndicators),
        modulesInterceptor = modulesInterceptor,
        skipReportKeyword = SkipReportKeyword(skipReportKeyword),
        reportStrings = reportStrings,
    )
}
