package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.internal.system.SystemCommandLine
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.io.path.Path

class GetProjectRootImplTest {

    private val systemCommandLine: SystemCommandLine = mockk()
    private val getProjectRoot: GetProjectRoot = GetProjectRootImpl(
        systemCommandLine = systemCommandLine,
    )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify it returns a Path built from the command output`() {
        val givenCommand = "git rev-parse --show-toplevel"

        every { systemCommandLine.exec(givenCommand) } returns "/lib/code"

        val result = getProjectRoot()

        assertEquals(Path("/lib/code"), result)
    }

    @Test
    fun `verify it trims the command output before building the Path`() {
        val givenCommand = "git rev-parse --show-toplevel"

        every { systemCommandLine.exec(givenCommand) } returns "  /lib/code\n\t"

        val result = getProjectRoot()

        assertEquals(Path("/lib/code"), result)
    }
}
