package com.sebastmar.danger.report.info

import com.sebastmar.danger.report.info.VersionedFile.Status

internal data class PullRequest(
    val htmlLink: String,
    val modules: List<Module>,
) {
    internal val createdFiles: List<VersionedFile> by lazy {
        modules.flatMap(Module::files).filter { it.status == Status.Created }
    }

    internal val modifiedFiles: List<VersionedFile> by lazy {
        modules.flatMap(Module::files).filter { it.status == Status.Modified }
    }

    internal val deletedFiles: List<VersionedFile> by lazy {
        modules.flatMap(Module::files).filter { it.status == Status.Deleted }
    }
}
