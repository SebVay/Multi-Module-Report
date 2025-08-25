@file:Repository("file:../../build/local-maven-repository")
@file:DependsOn("com.sebastienmartin:danger-modules-report:[0,)")

import com.sebastienmartin.danger.report.github.githubModuleReport
import systems.danger.kotlin.danger

danger(args) {
    githubModuleReport()
}


