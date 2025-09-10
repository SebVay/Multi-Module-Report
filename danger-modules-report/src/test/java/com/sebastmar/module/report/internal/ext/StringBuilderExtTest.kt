package com.sebastmar.module.report.internal.ext

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StringBuilderExtTest {

    @Test
    fun `verify it wraps the content within tr tags when block appends a new text`() {
        val givenText = "row-content"

        val result = buildString {
            tr {
                append(givenText)
            }
        }

        val expected = "<tr>row-content</tr>"

        assertEquals(expected, result)
    }

    @Test
    fun `verify it writes only empty tr tags when the block is empty`() {
        val result = buildString {
            tr()
        }

        val expected = "<tr></tr>"

        assertEquals(expected, result)
    }

    @Test
    fun `verify it wraps the content within td tags when block appends text`() {
        val givenText = "cell"

        val result = buildString {
            td {
                append(givenText)
            }
        }

        val expected = "<td>cell</td>"

        assertEquals(expected, result)
    }

    @Test
    fun `verify it writes only empty td tags when the block is empty`() {
        val result = buildString { td() }

        val expected = "<td></td>"

        assertEquals(expected, result)
    }

    @Test
    fun `verify it wraps the content within th tags when block appends text`() {
        val givenText = "header"

        val result = buildString {
            th {
                append(givenText)
            }
        }

        val expected = "<th>header</th>"

        assertEquals(expected, result)
    }

    @Test
    fun `verify it writes only empty th tags when the block is empty`() {
        val result = buildString {
            th()
        }

        val expected = "<th></th>"

        assertEquals(expected, result)
    }

    @Test
    fun `verify it writes table and includes nested rows and cells`() {
        val givenHeader = "H1"
        val givenCellA = "A"
        val givenCellB = "B"

        val result = buildString {
            table {
                tr {
                    th { append(givenHeader) }
                }
                tr {
                    td { append(givenCellA) }
                    td { append(givenCellB) }
                }
            }
        }

        val expected = "<table><tr><th>H1</th></tr><tr><td>A</td><td>B</td></tr></table>"

        assertEquals(expected, result)
    }

    @Test
    fun `verify it writes only the table skeleton with a newline at the end when the block is empty`() {
        val result = buildString { table() }

        val expected = "<table></table>"

        assertEquals(expected, result)
    }
}
