package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.info.VersionedFile
import com.sebastmar.module.report.info.VersionedFile.Status.Created
import com.sebastmar.module.report.info.VersionedFile.Status.Deleted
import com.sebastmar.module.report.info.VersionedFile.Status.Modified
import com.sebastmar.module.report.internal.system.SystemWrapper

/**
 * Retrieves a list of all files that have been updated (created, modified, or deleted)
 * in the current pull request.
 */
internal interface GetAllVersionedFiles {
    operator fun invoke(): List<VersionedFile>
}

/**
 * Combines the results of created, modified, and deleted files obtained from [GetVersionedFiles]
 * to produce an exhaustive list of all versioned files.
 */
internal class GetAllVersionedFilesImpl(
    private val systemWrapper: SystemWrapper,
    private val getVersionedFiles: GetVersionedFiles,
) : GetAllVersionedFiles {

    override fun invoke(): List<VersionedFile> {
        return buildList {
            addAll(getVersionedFiles(systemWrapper.createdFiles(), Created))
            addAll(getVersionedFiles(systemWrapper.modifiedFiles(), Modified))
            addAll(getVersionedFiles(systemWrapper.deletedFiles(), Deleted))
        }
    }
}
