package com.sebastmar.module.report.info

import java.security.MessageDigest

public data class VersionedFile(
    val name: String,
    val fullPath: String,
    val status: Status,
    val insertions: Int? = null,
    val deletions: Int? = null,
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

    /**
     * Represents the status of a file in a version control system.
     */
    public enum class Status {
        Created,
        Modified,
        Deleted,
    }
}

/**
 * Calculates the total number of inserted lines across all files in the list..
 */
internal fun List<VersionedFile>.getInsertedLines(): Int = sumOf { it.insertions ?: 0 }

/**
 * Calculates the total number of deleted lines across all files in the list..
 */
internal fun List<VersionedFile>.getDeletedLines(): Int = sumOf { it.deletions ?: 0 }
