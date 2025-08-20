@file:Suppress("MemberVisibilityCanBePrivate")

package com.sebast.mar.danger.report

import com.sebast.mar.danger.report.interceptor.ModuleInterceptor
import com.sebast.mar.danger.report.interceptor.NoOpModuleInterceptor

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
    public var topSection: String? = "# Updated Modules"
    public var moduleInterceptor: ModuleInterceptor = NoOpModuleInterceptor

    internal fun build(): ReportConfig = ReportConfig(
        isHostCorrect = isHostCorrect,
        topSection = topSection,
        moduleInterceptor = moduleInterceptor,
    )
}

/**
 * Represents the configuration of the report generator.
 *
 * @property topSection A text to be displayed at the top of the report, markdown compatible.
 * @property moduleInterceptor Allows modules to be intercepted, modified, or omitted from the report.
 */
internal data class ReportConfig(
    val isHostCorrect: Boolean,
    val topSection: String?,
    val moduleInterceptor: ModuleInterceptor,
)
