@file:Repository("file:../../build/local-maven-repository")
@file:DependsOn("com.sebast.mar:danger-modules-report:0.0.1-SNAPSHOT")

import com.sebast.mar.danger.report.github.githubModuleReport
import com.sebast.mar.danger.report.info.Module
import com.sebast.mar.danger.report.info.VersionedFile
import com.sebast.mar.danger.report.interceptor.ModulesInterceptor
import systems.danger.kotlin.danger

danger(args) {

    githubModuleReport {
        topSection = """
            # Heads up: Fixture data included
            This Danger report intentionally includes some fixture to showcase the current changes in the reporting capabilities.
            Please do not treat these as real findings or required actions.
            
            Reviewers: use this report to evaluate the table formatting alongside the code changes.
        """.trimIndent()

        modulesInterceptor = ModulesInterceptor {
            // Only add fixture data to the report
            buildList {
                add(
                    Module(
                        name = "fixture",
                        files = listOf(
                            VersionedFile("File1", "path", VersionedFile.Status.Created, 42),
                            VersionedFile("File2", "path", VersionedFile.Status.Modified, 42, 42),
                            VersionedFile("File3", "path", VersionedFile.Status.Deleted, null, 42),
                        )
                    ),
                )

                add(
                    Module(
                        name = "fixture:data",
                        files = listOf(
                            VersionedFile("Repository", "path", VersionedFile.Status.Created, 10),
                            VersionedFile("DataSource", "path", VersionedFile.Status.Created, 20),
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


