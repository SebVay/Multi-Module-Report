package com.sebast.mar.danger.report.info

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
