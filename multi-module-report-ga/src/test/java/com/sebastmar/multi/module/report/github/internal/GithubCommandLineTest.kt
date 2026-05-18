package com.sebastmar.multi.module.report.github.internal

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.OS

internal class GithubCommandLineTest {

    private val commandLine = GithubCommandLine()

    @Test
    @DisabledOnOs(OS.WINDOWS)
    fun `exec runs a simple command and returns its output`() {
        val result = commandLine.exec("echo", listOf("hello"))

        assertEquals("hello", result.trim())
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    fun `exec handles command with multiple arguments`() {
        val result = commandLine.exec("echo", listOf("hello", "world"))

        assertTrue(result.trim().contains("hello"))
        assertTrue(result.trim().contains("world"))
    }
}
