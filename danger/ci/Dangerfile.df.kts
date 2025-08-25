@file:Repository("file:../../build/local-maven-repository")
@file:DependsOn("com.sebastienmartin:danger-modules-report:[0,)")

import com.sebastienmartin.danger.report.github.githubModuleReport
import com.sebastienmartin.danger.report.info.Module
import com.sebastienmartin.danger.report.info.VersionedFile
import com.sebastienmartin.danger.report.info.VersionedFile.Status
import com.sebastienmartin.danger.report.interceptor.ModulesInterceptor
import systems.danger.kotlin.danger

danger(args) {

    // First generate a report of what really changed in the current PR
    githubModuleReport()

    // Then generate a fixture report to cover the capabilities
    githubModuleReport {
        topSection = """
            # Fixture data
            The following report intentionally includes some fixture to showcase the reporting capabilities.
            - Any code changes made in your current PR will be reflected here.
            - Please do not treat these as real findings or required actions.
            
            Reviewers: use this report to evaluate the table formatting alongside the code changes.
        """.trimIndent()

        bottomSection = null

        modulesInterceptor = ModulesInterceptor {
            fixtureModules()
        }
    }
}

private fun fixtureModules() = listOf(
    Module(
        name = "fixture",
        files = listOf(
            VersionedFile("File1", "path", Status.Created, 42),
            VersionedFile("File2", "path", Status.Created, 42),
        )
    ),
    Module(
        name = "fixture:data",
        files = listOf(
            VersionedFile("Repository", "path", Status.Created, 10),
            VersionedFile("DataSource", "path", Status.Modified, 20, 5),
            VersionedFile("EntityMapper", "path", Status.Deleted, deletions = 42),
        )
    ),
    Module(
        name = "fixture:domain",
        files = listOf(
            VersionedFile("UseCase", "path", Status.Modified, 30, 5),
            VersionedFile("Interactor", "path", Status.Modified, 40, 10),
        )
    ),
    Module(
        name = "fixture:ui",
        files = listOf(
            VersionedFile("ViewModel", "path", Status.Deleted, deletions = 50),
            VersionedFile("Presenter", "path", Status.Deleted, deletions = 60),
        )
    )
)