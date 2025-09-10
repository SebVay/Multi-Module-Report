package com.sebastmar.module.report.internal.system.danger

import com.sebastmar.module.report.internal.system.SystemWriter
import systems.danger.kotlin.markdown

internal class DangerWriter : SystemWriter<String> {
    override fun write(content: String) {
        markdown(content)
    }
}
