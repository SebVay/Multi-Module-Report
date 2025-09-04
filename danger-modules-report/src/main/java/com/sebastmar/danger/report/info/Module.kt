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
    val type: ModuleType = ModuleType.STANDARD,
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

/**
 * Represents the type of a module.
 *
 * This enum is used to categorize modules based on their role or structure within a project.
 *
 * @property PROJECT_ROOT Indicates that the module is the root of the project.
 * @property STANDARD Indicates a standard module within the project.
 * @property NOT_KNOWN Indicates that the type could not be determined.
 */
public enum class ModuleType {
    PROJECT_ROOT,
    STANDARD,
    NOT_KNOWN,
}
