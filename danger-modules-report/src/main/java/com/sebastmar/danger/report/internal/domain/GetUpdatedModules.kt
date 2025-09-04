package com.sebastmar.danger.report.internal.domain

import com.sebastmar.danger.report.info.Module
import com.sebastmar.danger.report.info.ModuleType
import com.sebastmar.danger.report.info.VersionedFile
import com.sebastmar.danger.report.info.VersionedFile.Status.Created
import com.sebastmar.danger.report.info.VersionedFile.Status.Deleted
import com.sebastmar.danger.report.info.VersionedFile.Status.Modified
import com.sebastmar.danger.report.interceptor.ModulesInterceptor
import com.sebastmar.danger.report.internal.helper.DangerWrapper
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.relativeTo

/**
 * Interface for retrieving a list of modules that have been changed in the current pull request.
 */
internal interface GetUpdatedModules {
    operator fun invoke(): List<Module>
}

/**
 * Interacts with Danger to get lists of created, modified, and deleted files,
 * then groups them into modules.
 *
 * It processes the files to identify the modules they belong to.
 * The `ModuleInterceptor` can be used to filter or modify the resulting list of modules that'll be displayed by danger.
 */
internal class GetUpdatedModulesImpl(
    private val danger: DangerWrapper,
    private val getFiles: GetFiles,
    private val getProjectRoot: GetProjectRoot,
    private val modulesInterceptor: ModulesInterceptor,
) : GetUpdatedModules {

    private val projectRoot: Path by lazy { getProjectRoot() }

    override fun invoke(): List<Module> {
        return getUpdatedFiles()
            .groupBy(::findCorrectModule)
            .map { (module, files) -> module.copy(files = files) }
            .let(modulesInterceptor::intercept)
    }

    /**
     * Retrieves a list of all files that have been updated (created, modified, or deleted)
     * in the current pull request.
     *
     * It uses the `danger` instance to get lists of created, modified, and deleted files,
     * then delegates to the `getFiles` function to process each list and convert them
     * into a unified list of `VersionedFile` objects.
     *
     * @return A list of `VersionedFile` objects representing all updated files.
     */
    private fun getUpdatedFiles(): List<VersionedFile> = buildList {
        addAll(getFiles(danger.createdFiles(), Created))
        addAll(getFiles(danger.modifiedFiles(), Modified))
        addAll(getFiles(danger.deletedFiles(), Deleted))
    }

    /**
     * Finds the appropriate module for a specified versioned file within the project structure.
     *
     * The method traverses the directory hierarchy starting from the file's location,
     * checking whether each directory is a recognized module (either a root Gradle module or
     * a standard Gradle module). If a matching module is found, it is returned.
     *
     * @param versionedFile The versioned file for which the containing module needs to be identified.
     * @return The module containing the provided file, or an "unknown module" if no match is found.
     */
    private fun findCorrectModule(versionedFile: VersionedFile): Module {
        val startingPath = projectRoot.resolve(versionedFile.fullPath).parent

        return generateSequence(startingPath, Path::getParent).firstNotNullOfOrNull { path ->
            when {
                path.isRootModule() && startingPath == path -> PROJECT_ROOT
                path.isStandardModule() -> Module(name = path.relativeTo(projectRoot).pathString.replace("/", ":"))
                else -> null
            }
        } ?: UNKNOWN_MODULE
    }

    /**
     * Checks if the current path represents a standard Gradle module.
     *
     * A directory is considered a standard module if it contains either a `build.gradle.kts`
     * or a `build.gradle` file.
     */
    private fun Path.isStandardModule(): Boolean {
        return hasBuildGradle() && !hasSettingsGradle()
    }

    /**
     * Checks if the current path represents the root Gradle module.
     *
     * A directory is considered the root module if it contains either a `settings.gradle.kts`
     * or a `settings.gradle` file.
     */
    private fun Path.isRootModule(): Boolean {
        return hasSettingsGradle()
    }

    private fun Path.hasBuildGradle(): Boolean {
        return resolve("build.gradle.kts").exists() || resolve("build.gradle").exists()
    }

    private fun Path.hasSettingsGradle(): Boolean {
        return resolve("settings.gradle.kts").exists() || resolve("settings.gradle").exists()
    }

    private companion object {
        val PROJECT_ROOT = Module(name = "Project's Root", type = ModuleType.PROJECT_ROOT)
        val UNKNOWN_MODULE = Module(name = "Others", type = ModuleType.NOT_KNOWN)
    }
}
