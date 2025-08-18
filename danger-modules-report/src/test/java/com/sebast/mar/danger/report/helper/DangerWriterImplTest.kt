package com.sebast.mar.danger.report.helper

import com.sebast.mar.danger.report.writer.DangerWriterImpl
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import systems.danger.kotlin.markdown

internal class DangerWriterImplTest {
    private val writer = DangerWriterImpl()

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `writeMarkdown calls danger markdown`() {
        mockkStatic("systems.danger.kotlin.MainScriptKt")
        every { markdown(any()) } returns Unit

        val givenText = "Hello from Danger"

        writer.writeMarkdown(givenText)

        verify(exactly = 1) { markdown(givenText) }
    }
}
