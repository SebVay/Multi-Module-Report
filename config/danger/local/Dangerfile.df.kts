@file:Repository("file:../../../build/local-maven-repository")
@file:DependsOn("com.sebastmar:danger-modules-report:[0,)")

import com.sebastmar.module.report.githubModuleReport
import systems.danger.kotlin.danger

danger(args) {
    githubModuleReport()
}
