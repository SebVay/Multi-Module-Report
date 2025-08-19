package com.sebast.mar.danger.report

import java.security.MessageDigest

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
    val isFallback: Boolean,
)

public data class VersionedFile(
    val name: String,
    val fullPath: String,
    val status: Status,
    val insertions: Int?,
    val deletions: Int?,
) {
    /**
     * Calculates the SHA-256 hash of the file's full path.
     *
     * This hash is used by GitHub to generate hyperlinks to specific file diffs.
     *
     * @return A string representing the SHA-256 hash of the `fullPath` in hexadecimal format.
     */
    val sha256Path: String
        get() =
            MessageDigest
                .getInstance("SHA-256")
                .digest(fullPath.toByteArray())
                .fold("") { str, byte -> str + "%02x".format(byte) }
}

/**
 * Represents the status of a file in a version control system.
 */
public enum class Status {
    Created,
    Modified,
    Deleted,
}
