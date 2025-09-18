package com.sebastmar.multi.module.report.danger.internal

import com.sebastmar.module.report.system.SystemWriter
import systems.danger.kotlin.markdown

internal class DangerWriter : SystemWriter<String> {
    override fun write(content: String) {
        markdown(content)
    }
}
