package com.sebastmar.module.report.internal.system.danger

import com.sebastmar.module.report.internal.system.SystemCommandLine
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import systems.danger.kotlin.models.danger.DangerDSL

internal class DangerCommandLineTest {
    private val danger: DangerDSL = mockk()
    private val systemCommandLine: SystemCommandLine = DangerCommandLine(danger)

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify exec calls DangerDSL exec with provided command and args`() {
        val givenCommand = "echo"
        val givenArgs = listOf("hello", "world")
        val expected = "ok"

        every { danger.utils.exec(any(), any()) } returns expected

        val output = systemCommandLine.exec(givenCommand, givenArgs)

        assertEquals(expected, output)
        verify(exactly = 1) { danger.utils.exec(givenCommand, givenArgs) }
    }
}
