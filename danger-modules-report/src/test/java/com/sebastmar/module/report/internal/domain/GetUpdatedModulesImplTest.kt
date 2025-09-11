package com.sebastmar.module.report.internal.domain

import com.sebastmar.module.report.configuration.ModulesInterceptor
import com.sebastmar.module.report.configuration.NoOpModulesInterceptor
import com.sebastmar.module.report.info.Module
import com.sebastmar.module.report.info.ModuleType
import com.sebastmar.module.report.info.VersionedFile
import com.sebastmar.module.report.info.VersionedFile.Status
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.createTempDirectory

class GetUpdatedModulesImplTest {

    private val getAllVersionedFiles: GetAllVersionedFiles = mockk(relaxed = true)
    private val getProjectRoot: GetProjectRoot = mockk()

    private val getUpdatedModules: GetUpdatedModules = GetUpdatedModulesImpl(
        getAllVersionedFiles = getAllVersionedFiles,
        getProjectRoot = getProjectRoot,
        modulesInterceptor = NoOpModulesInterceptor,
    )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify it returns an empty list when there are no updated files`() {
        val givenRoot = createProjectRootDir()

        every { getProjectRoot() } returns givenRoot

        val result = getUpdatedModules()

        val expected: List<Module> = emptyList()

        assertEquals(expected, result)
    }

    @Test
    fun `verify it creates the Standard modules and group their files accordingly`() {
        val givenRoot = createProjectRootDir(useSettingsKts = true)
        val givenModuleA = givenRoot.resolve("moduleA").createDirectories()
        val givenModuleB = givenRoot.resolve("moduleB").createDirectories()

        givenModuleA.resolve("build.gradle.kts").createFile()
        givenModuleB.resolve("build.gradle").createFile()

        val givenVersionedA = VersionedFile("FileA.kt", "moduleA/src/FileA.kt", Status.Modified)
        val givenVersionedB = VersionedFile("FileB.kt", "moduleA/src/FileB.kt", Status.Modified)
        val givenVersionedC = VersionedFile("FileC.kt", "moduleB/src/FileC.kt", Status.Modified)

        every { getProjectRoot() } returns givenRoot
        every { getAllVersionedFiles() } returns listOf(givenVersionedA, givenVersionedB, givenVersionedC)

        val result = getUpdatedModules()

        val expected = listOf(
            Module(name = "moduleA", type = ModuleType.STANDARD, files = listOf(givenVersionedA, givenVersionedB)),
            Module(name = "moduleB", type = ModuleType.STANDARD, files = listOf(givenVersionedC)),
        )

        assertEquals(expected, result)
    }

    @Test
    fun `verify it returns the ProjectRoot Module and group its files accordingly`() {
        val givenRoot = createProjectRootDir()
        val givenVersionedFiles = listOf(
            VersionedFile("README.md", "README.md", Status.Modified),
            VersionedFile("LICENCE.md", "LICENCE.md", Status.Modified),
        )

        every { getProjectRoot() } returns givenRoot
        every { getAllVersionedFiles() } returns givenVersionedFiles

        val result = getUpdatedModules()

        val expected = Module(
            name = "Project's Root",
            type = ModuleType.PROJECT_ROOT,
            files = givenVersionedFiles,
        )

        assertEquals(expected, result.first())
    }

    @Test
    fun `verify it returns the Unknown module when its file doesn't belong to any known modules`() {
        val givenRoot = createProjectRootDir(useSettingsKts = true)

        val givenVersionedDoc = VersionedFile("Doc.md", "docs/Guide.md", Status.Deleted)

        every { getProjectRoot() } returns givenRoot
        every { getAllVersionedFiles() } returns listOf(givenVersionedDoc)

        val result = getUpdatedModules()

        val expected = Module("Others", ModuleType.NOT_KNOWN, listOf(givenVersionedDoc))

        assertEquals(expected, result.first())
    }

    @Test
    fun `verify it includes created, modified, and deleted files in a Standard module`() {
        val givenRoot = createProjectRootDir()
        val givenModule = givenRoot.resolve("feature").createDirectories()

        givenModule.resolve("build.gradle.kts").createFile()

        val givenVersionedFiles = listOf(
            VersionedFile("FileCreated.kt", "feature/new/FileCreated.kt", Status.Created),
            VersionedFile("FileModified.kt", "feature/src/FileModified.kt", Status.Modified),
            VersionedFile("FileDeleted.kt", "feature/old/FileDeleted.kt", Status.Deleted),
        )

        every { getProjectRoot() } returns givenRoot
        every { getAllVersionedFiles() } returns givenVersionedFiles

        val result = getUpdatedModules()

        val expected = Module("feature", ModuleType.STANDARD, givenVersionedFiles)

        assertEquals(expected, result.first())
    }

    @Test
    fun `verify it call the module interceptor and returns an intercepted list of modules`() {
        val givenRoot = createProjectRootDir()
        val givenModuleA = givenRoot.resolve("moduleA").createDirectories()
        val givenInterceptor = mockk<ModulesInterceptor>()
        val givenVersionedFiles = listOf(VersionedFile("FileA.kt", "moduleA/src/FileA.kt", Status.Modified))

        val givenModule = Module("moduleA", ModuleType.STANDARD, givenVersionedFiles)
        val interceptedModules = listOf<Module>(mockk())

        givenModuleA.resolve("build.gradle.kts").createFile()

        every { getProjectRoot() } returns givenRoot
        every { getAllVersionedFiles() } returns givenVersionedFiles
        every { givenInterceptor.intercept(listOf(givenModule)) } returns interceptedModules

        val getUpdatedModules = GetUpdatedModulesImpl(
            getAllVersionedFiles = getAllVersionedFiles,
            getProjectRoot = getProjectRoot,
            modulesInterceptor = givenInterceptor,
        )

        val result = getUpdatedModules()

        assertEquals(interceptedModules, result)
    }

    /**
     * Creates a temporary "root project directory" for testing purposes.
     * It create in this directory a settings.gradle file which mimics a basic Gradle project structure.
     */
    private fun createProjectRootDir(useSettingsKts: Boolean = false): Path {
        val settingsFile = if (useSettingsKts) "settings.gradle.kts" else "settings.gradle"

        return createTempDirectory("root")
            .toAbsolutePath()
            .also { it.resolve(settingsFile).createFile() }
    }
}
