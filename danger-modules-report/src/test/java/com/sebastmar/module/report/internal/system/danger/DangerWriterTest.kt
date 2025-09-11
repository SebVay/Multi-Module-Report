package com.sebastmar.module.report.internal.system.danger

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import systems.danger.kotlin.markdown

internal class DangerWriterTest {
    private val writer = DangerWriter()

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `writeMarkdown calls danger markdown`() {
        mockkStatic("systems.danger.kotlin.MainScriptKt")
        every { markdown(any()) } returns Unit

        val givenText = "Hello from Danger"

        writer.write(givenText)

        verify(exactly = 1) { markdown(givenText) }
    }
}
