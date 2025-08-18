package com.sebast.mar.danger.report.helper

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import systems.danger.kotlin.models.danger.DangerDSL

internal class CommandLineImplTest {
    private lateinit var danger: DangerDSL
    private lateinit var commandLine: CommandLine

    @BeforeEach
    fun setUp() {
        danger = mockk()
        commandLine = CommandLineImpl(danger)
    }

    @Test
    fun `verify exec calls DangerDSL exec with provided command and args`() {
        val givenCommand = "echo"
        val givenArgs = listOf("hello", "world")
        val expected = "ok"

        every { danger.utils.exec(any(), any()) } returns expected

        val output = commandLine.exec(givenCommand, givenArgs)

        assertEquals(expected, output)
        verify(exactly = 1) { danger.utils.exec(givenCommand, givenArgs) }
    }
}
