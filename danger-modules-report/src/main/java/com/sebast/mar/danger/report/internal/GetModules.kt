package com.sebast.mar.danger.report.internal

import com.sebast.mar.danger.report.info.Module
import com.sebast.mar.danger.report.info.VersionedFile
import com.sebast.mar.danger.report.info.VersionedFile.Status.Created
import com.sebast.mar.danger.report.info.VersionedFile.Status.Deleted
import com.sebast.mar.danger.report.info.VersionedFile.Status.Modified
import com.sebast.mar.danger.report.interceptor.ModulesInterceptor
import com.sebast.mar.danger.report.internal.helper.DangerWrapper

/**
 * Interface for retrieving a list of modules that have been changed in the current pull request.
 */
internal interface GetModules {
    operator fun invoke(): List<Module>
}

/**
 * Interacts with Danger to get lists of created, modified, and deleted files,
 * then groups them into modules.
 *
 * It processes the files to identify the modules they belong to.
 * The `ModuleInterceptor` can be used to filter or modify the resulting list of modules that'll be displayed by danger.
 */
internal class GetModulesImpl(
    private val danger: DangerWrapper,
    private val getFiles: GetFiles,
    private val modulesInterceptor: ModulesInterceptor,
) : GetModules {
    override fun invoke(): List<Module> {
        val createdFiles = getFiles(danger.createdFiles(), Created)
        val modifiedFiles = getFiles(danger.modifiedFiles(), Modified)
        val deletedFiles = getFiles(danger.deletedFiles(), Deleted)

        return createModulesFromFiles(createdFiles + modifiedFiles + deletedFiles)
            .let(modulesInterceptor::intercept)
    }

    private fun createModulesFromFiles(files: List<VersionedFile>): List<Module> {
        return files
            .groupBy { file ->
                file.fullPath
                    .takeIf { path -> path.contains("/src/") }
                    ?.substringBefore("/src/")
                    ?.replace("/", ":")
            }
            .map { (name, files) ->
                Module(
                    name = name ?: "Others",
                    files = files,
                    isFallback = name != null,
                )
            }
            .sortedBy { !it.isFallback }
    }
}
