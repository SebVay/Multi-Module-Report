package com.sebastmar.module.report.system

/**
 * Interface for writing content to the system.
 */
public interface SystemWriter<Content> {
    public fun write(content: Content)
}
