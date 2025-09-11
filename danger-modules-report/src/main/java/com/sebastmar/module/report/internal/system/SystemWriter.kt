package com.sebastmar.module.report.internal.system

/**
 * Interface for writing content to the system.
 */
internal interface SystemWriter<Content> {
    fun write(content: Content)
}
