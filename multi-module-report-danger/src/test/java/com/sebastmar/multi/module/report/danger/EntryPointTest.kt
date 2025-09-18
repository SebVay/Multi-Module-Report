package com.sebastmar.multi.module.report.danger

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import systems.danger.kotlin.markdown
import systems.danger.kotlin.models.danger.DangerDSL

internal class EntryPointTest {

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    /**
     * End to end test, verifying everything is wired up correctly and the Danger writer is ultimately called.
     */
    @Test
    fun `verify githubModuleReport writes into the system writer ultimately`() {
        mockkStatic("systems.danger.kotlin.MainScriptKt") {
            val givenDanger = mockk<DangerDSL>(relaxed = true)

            every { markdown(any()) } returns Unit

            every { givenDanger.onGitHub } returns true
            every { givenDanger.git.createdFiles } returns listOf("a/b/c/Created.kt")
            every { givenDanger.git.deletedFiles } returns listOf("a/b/c/Deleted.kt")
            every { givenDanger.git.modifiedFiles } returns listOf("a/b/c/Modified.kt")

            givenDanger.githubModuleReport {
                showCircleIndicators = false
                showLineIndicators = false
                linkifyFiles = false

                reportStrings {
                    topSection = null
                    bottomSection = null
                    unknownModuleName = "Unknown Module"
                }
            }

            val expected = "<table>" +
                "<tr><th></th><th>Added</th><th>Modified</th><th>Deleted</th></tr>" +
                "<tr>" +
                "<td><div style=\"display: inline-block;\"><b>Unknown Module</b></div></td>" +
                "<td>Created.kt<br></td><td>Modified.kt<br></td><td>Deleted.kt<br></td>" +
                "</tr>" +
                "</table>\n\n"

            verify(exactly = 1) { markdown(expected) }
        }
    }
}
