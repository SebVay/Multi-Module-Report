package com.sebastmar.module.report.internal

import com.sebastmar.module.report.internal.domain.SkipReport
import com.sebastmar.module.report.system.SystemWriter
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class BaseReportTest {

    private val skipReport: SkipReport = mockk()
    private val reportBuilder: ReportBuilder<String> = mockk()
    private val systemWriter: SystemWriter<String> = mockk(relaxed = true)
    private val report = FakeReport(skipReport, reportBuilder, systemWriter)

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify it prepare and write the report content`() {
        val givenContent = "Report Content"

        every { skipReport() } returns false
        every { reportBuilder.getReport() } returns givenContent

        report.write()

        assertEquals(1, report.prepareCalls)
        verify { reportBuilder.getReport() }
        verify { systemWriter.write(givenContent) }
    }

    @Test
    fun `verify it doesn't call anything when skipped`() {
        every { skipReport() } returns true

        report.write()

        assertEquals(0, report.prepareCalls)
        verify { reportBuilder wasNot Called }
    }
}

private class FakeReport(
    skipReport: SkipReport,
    reportBuilder: ReportBuilder<String>,
    systemWriter: SystemWriter<String>,
) : BaseReport<String, ReportBuilder<String>>(skipReport, reportBuilder, systemWriter) {

    var prepareCalls: Int = 0
        private set

    override fun ReportBuilder<String>.buildReport() {
        prepareCalls++
    }
}
