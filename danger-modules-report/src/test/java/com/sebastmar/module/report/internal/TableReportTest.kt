package com.sebastmar.module.report.internal

import com.sebastmar.module.report.internal.domain.SkipReport
import com.sebastmar.module.report.internal.system.SystemWriter
import io.mockk.Call
import io.mockk.MockKAnswerScope
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

private typealias UnitLambda = () -> Unit
private typealias MockKAnswerCall = MockKAnswerScope<Unit, Unit>.(Call) -> Unit

internal class TableReportTest {

    private val tableReportBuilder: TableReportBuilder = mockk(relaxed = true)
    private val systemWriter: SystemWriter<String> = mockk(relaxed = true)
    private val skipReport: SkipReport = mockk()

    private val tableReport = TableReport(
        systemWriter = systemWriter,
        skipReport = skipReport,
        reportBuilder = tableReportBuilder,
    )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify it orchestrates the sections, table, and writes the built markdown`() {
        val givenBuiltMarkdown = "<html>table</html>"

        every { skipReport() } returns false
        every { tableReportBuilder.table(any()) } answers invokeTableCallback()
        every { tableReportBuilder.getReport() } returns givenBuiltMarkdown

        tableReport.write()

        verifyOrder {
            tableReportBuilder.topSection()
            tableReportBuilder.table(any())
            tableReportBuilder.headerRow()
            tableReportBuilder.moduleRows()
            tableReportBuilder.bottomSection()
            systemWriter.write(givenBuiltMarkdown)
        }
    }

    @Test
    fun `verify it does nothing when the report must be skipped`() {
        every { skipReport() } returns true

        tableReport.write()

        verify { tableReportBuilder wasNot called }
        verify { systemWriter wasNot called }
    }

    /**
     * Helper function to create a MockK answer that invokes the first argument as a lambda.
     *
     * In this specific test, it's used to "activate" the lambda passed in the `reportBuilder.table(any())` call, ensuring
     * that lambda is executed, which in turn calls `headerRow()` and `moduleRows()`.
     *
     * @return A MockKAnswerScope that invokes the first argument as a lambda.
     */
    private fun invokeTableCallback(): MockKAnswerCall {
        return {
            firstArg<UnitLambda>().invoke()
        }
    }
}
