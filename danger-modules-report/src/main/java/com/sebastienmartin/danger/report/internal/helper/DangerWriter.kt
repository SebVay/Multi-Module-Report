package com.sebastienmartin.danger.report.internal.helper

import systems.danger.kotlin.markdown

/**
 * Interface for writing content to the Danger report.
 */
internal interface DangerWriter {
    fun writeMarkdown(markdown: String)
}

internal class DangerWriterImpl : DangerWriter {
    override fun writeMarkdown(markdown: String) {
        markdown(markdown)
    }
}
