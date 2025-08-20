@file:Repository("file:../../build/local-maven-repository")
@file:DependsOn("com.sebast.mar:danger-modules-report:0.0.1-SNAPSHOT")

import com.sebast.mar.danger.report.github.githubModuleReport
import com.sebast.mar.danger.report.info.Module
import com.sebast.mar.danger.report.info.VersionedFile
import com.sebast.mar.danger.report.interceptor.ModulesInterceptor
import systems.danger.kotlin.danger

danger(args) {

    // Generate first a report without fixtures
    githubModuleReport()

    // Then generate a fixture report
    githubModuleReport {
        topSection = """
            # Fixture data
            The following report intentionally includes some fixture to showcase the reporting capabilities.
            - Any code changes made in your current PR will be reflected here.
            - Please do not treat these as real findings or required actions.
            
            Reviewers: use this report to evaluate the table formatting alongside the code changes.
        """.trimIndent()

        modulesInterceptor = ModulesInterceptor {
            buildList {
                add(
                    Module(
                        name = "fixture",
                        files = listOf(
                            VersionedFile("File1", "path", VersionedFile.Status.Created, 42),
                            VersionedFile("File2", "path", VersionedFile.Status.Created, 42),
                        )
                    ),
                )

                add(
                    Module(
                        name = "fixture:data",
                        files = listOf(
                            VersionedFile("Repository", "path", VersionedFile.Status.Created, 10),
                            VersionedFile("DataSource", "path", VersionedFile.Status.Modified, 20, 5),
                            VersionedFile("EntityMapper", "path", VersionedFile.Status.Deleted, deletions = 42),
                        )
                    )
                )

                add(
                    Module(
                        name = "fixture:domain",
                        files = listOf(
                            VersionedFile("UseCase", "path", VersionedFile.Status.Modified, 30, 5),
                            VersionedFile("Interactor", "path", VersionedFile.Status.Modified, 40, 10),
                        )
                    )
                )

                add(
                    Module(
                        name = "fixture:ui",
                        files = listOf(
                            VersionedFile("ViewModel", "path", VersionedFile.Status.Deleted, deletions = 50),
                            VersionedFile("Presenter", "path", VersionedFile.Status.Deleted, deletions = 60),
                        )
                    )
                )
            }
        }
    }
}


