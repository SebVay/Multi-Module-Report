package com.sebastienmartin.danger.report.internal.helper

internal fun StringBuilder.tr(block: () -> Unit = {}) {
    append("<tr>")
    block()
    append("</tr>")
}

internal fun StringBuilder.td(block: () -> Unit = {}) {
    append("<td>")
    block()
    append("</td>")
}

internal fun StringBuilder.th(block: () -> Unit = {}) {
    append("<th>")
    block()
    append("</th>")
}

internal fun StringBuilder.table(block: () -> Unit = {}) {
    appendLine("\n<table>")
    block()
    appendLine("</table>\n")
}
