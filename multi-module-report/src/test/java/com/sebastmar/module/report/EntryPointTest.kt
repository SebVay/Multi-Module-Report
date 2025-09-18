package com.sebastmar.module.report

import com.sebastmar.module.report.system.SystemCommandLine
import com.sebastmar.module.report.system.SystemWrapper
import com.sebastmar.module.report.system.SystemWriter
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class EntryPointTest {

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify the report calls the SystemWriter`() {
        val givenSystemWrapper = mockk<SystemWrapper>(relaxed = true)
        val givenSystemWriter = mockk<SystemWriter<String>>(relaxed = true)
        val givenSystemCommandLine = mockk<SystemCommandLine>(relaxed = true)

        every { givenSystemWrapper.modifiedFiles() } returns listOf("a/b/c/File.kt")

        val githubReport = githubReport(
            givenSystemCommandLine,
            givenSystemWrapper,
            givenSystemWriter,
        ) {
            showLineIndicators = false
            showCircleIndicators = false
            linkifyFiles = false

            reportStrings {
                topSection = null
                bottomSection = null
            }
        }

        githubReport.write()

        val expectedContent = "<table>" +
            "<tr><th></th><th>Modified</th></tr>" +
            "<tr><td><div style=\"display: inline-block;\"><b>Others</b></div></td><td>File.kt<br></td></tr>" +
            "</table>\n\n"

        verify(exactly = 1) { givenSystemWriter.write(expectedContent) }
    }
}
