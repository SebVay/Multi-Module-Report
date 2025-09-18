package com.sebastmar.module.report.info

internal data class PullRequest(
    val htmlLink: String,
    val modules: List<Module>,
) {
    internal val createdFiles: List<VersionedFile> by lazy {
        modules.flatMap(Module::createdFiles)
    }

    internal val modifiedFiles: List<VersionedFile> by lazy {
        modules.flatMap(Module::modifiedFiles)
    }

    internal val deletedFiles: List<VersionedFile> by lazy {
        modules.flatMap(Module::deletedFiles)
    }
}
