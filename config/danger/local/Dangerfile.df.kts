@file:Repository("file:../../../build/local-maven-repository")
@file:DependsOn("com.sebastmar:multi-module-report-danger:[0,)")

import com.sebastmar.multi.module.report.danger.githubModuleReport
import systems.danger.kotlin.danger

danger(args) {
    githubModuleReport()
}
