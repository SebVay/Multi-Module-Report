package com.sebastmar.module.report

/**
 * Represents a report that can be written and whose contents can be retrieved.
 *
 * @param Output The type of content that the report produces.
 */
public interface Report<Output> {
    public fun write()

    public fun content(): Output
}
