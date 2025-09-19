package com.sebastmar.module.report.internal.ext

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
    append("<table>")
    block()
    append("</table>")
}
