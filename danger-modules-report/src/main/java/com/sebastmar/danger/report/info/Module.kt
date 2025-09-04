package com.sebastmar.danger.report.info

import com.sebastmar.danger.report.info.VersionedFile.Status

/**
 * Represents a module that has been modified in the current Pull Request.
 *
 * This class encapsulates information about a specific module within a project,
 * detailing the files that have been changed.
 *
 * @property name The name of the module.
 * @property files A list of [VersionedFile] objects representing all modified files in this module.
 */
public data class Module(
    val name: String,
    val files: List<VersionedFile> = emptyList(),
) {
    internal val createdFiles: List<VersionedFile> by lazy {
        files.filter { it.status == Status.Created }
    }

    internal val modifiedFiles: List<VersionedFile> by lazy {
        files.filter { it.status == Status.Modified }
    }

    internal val deletedFiles: List<VersionedFile> by lazy {
        files.filter { it.status == Status.Deleted }
    }
}
