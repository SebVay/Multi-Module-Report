package com.sebastmar.danger.report.info

import com.sebastmar.danger.report.info.VersionedFile.Status

/**
 * Represents a module that has been modified in the current Pull Request.
 *
 * @property name The name of the module.
 * @property files A list of [VersionedFile] objects representing the modified files in this module.
 * @property isFallback A boolean indicating whether this module is a fallback module.
 *                      Fallback module is used when a file cannot be matched to a module.
 */
public data class Module(
    val name: String,
    val files: List<VersionedFile>,
    val isFallback: Boolean = false,
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
