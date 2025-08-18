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
public class ReportConfigBuilder {
    private var writeSections: Boolean = true
    private var moduleInterceptor: ModuleInterceptor = NoOpModuleInterceptor

    internal fun build(): ReportConfig = ReportConfig(
        writeSections = writeSections,
        moduleInterceptor = moduleInterceptor,
    )
}

/**
 * Represents the configuration of the report generator.
 *
 * @property writeSections If true, sections are written to Danger.
 * @property moduleInterceptor Allows modules to be intercepted, modified, or omitted from the report.
 */
internal data class ReportConfig(
    val writeSections: Boolean = true,
    val moduleInterceptor: ModuleInterceptor,
)
