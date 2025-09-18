package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.internal.ReportStrings

internal interface StringProvider {
    fun topSection(): String?
    fun bottomSection(): String?
    fun incorrectHostWarning(): String?
    fun unknownModuleName(): String
    fun projectRootModuleName(): String
}

internal class StringProviderImpl(
    private val reportStrings: ReportStrings,
) : StringProvider {
    override fun topSection(): String? = reportStrings.topSection

    override fun bottomSection(): String? = reportStrings.bottomSection

    override fun incorrectHostWarning(): String? = reportStrings.incorrectHostWarning

    override fun unknownModuleName(): String = reportStrings.unknownModuleName

    override fun projectRootModuleName(): String = reportStrings.projectRootModuleName
}
