package com.sebastienmartin.danger.report.di

import com.sebastienmartin.danger.report.ReportConfig
import com.sebastienmartin.danger.report.host.HostType
import org.koin.core.Koin
import org.koin.dsl.koinApplication
import systems.danger.kotlin.models.danger.DangerDSL

/**
 * Sets up the Koin dependency injection graph.
 *
 * @param hostType The type of the CI/CD host environment (e.g., GitHub, GitLab).
 * @param reportConfig The configuration for generating the report.
 * @return A [Koin] instance with all the necessary modules loaded.
 */
internal fun DangerDSL.initGraph(hostType: HostType, reportConfig: ReportConfig): Koin {
    return koinApplication {
        modules(
            domainModule,
            reportModule(reportConfig, hostType),
            dangerModule(),
        )
    }.koin
}
