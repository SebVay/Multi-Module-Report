package com.sebastmar.module.report

import com.sebastmar.module.report.internal.TableReport
import com.sebastmar.module.report.internal.TableReportBuilder
import com.sebastmar.module.report.internal.di.koin.ModuleReportKoinContext
import com.sebastmar.module.report.internal.system.SystemWriter
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.koin.core.Koin
import org.koin.dsl.module
import org.koin.test.KoinTest
import systems.danger.kotlin.models.danger.DangerDSL

internal class EntryPointTest : KoinTest {

    override fun getKoin(): Koin = ModuleReportKoinContext.koin

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify githubModuleReport builds a Danger Github report and write into it`() {
        val givenTableReport = mockk<TableReport>(relaxed = true)
        val givenModule = module { single { givenTableReport } }
        val givenDanger = mockk<DangerDSL>(relaxed = true)

        ModuleReportKoinContext.koin.loadModules(
            allowOverride = true,
            modules = listOf(givenModule),
        )

        with(givenDanger) {
            githubModuleReport()
        }

        verify { givenTableReport.write() }
    }

    /**
     * This test ensures that even when using the real implementations of [TableReport] and
     * [TableReportBuilder], the report content eventually reaches the mocked [SystemWriter].
     */
    @Test
    fun `verify githubModuleReport writes into the system writer ultimately`() {
        val givenDanger = mockk<DangerDSL>(relaxed = true)
        val givenSystemWriter = mockk<SystemWriter<String>>(relaxed = true)
        val givenModule = module { single { givenSystemWriter } }

        ModuleReportKoinContext.koin.loadModules(
            allowOverride = true,
            modules = listOf(givenModule),
        )

        with(givenDanger) {
            githubModuleReport {
                reportStrings = reportStrings.copy(
                    topSection = "Top Section",
                    bottomSection = "Bottom Section",
                    incorrectHostWarning = "Warning",
                )
            }
        }

        verify { givenSystemWriter.write(any()) }
    }
}
