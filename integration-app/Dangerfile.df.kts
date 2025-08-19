@file:Repository("file:///../build/local-maven-repository")
@file:DependsOn("com.sebast.mar:danger-modules-report:0.0.1-SNAPSHOT")

import com.sebast.mar.danger.report.github.githubModuleReport
import systems.danger.kotlin.danger

danger(args) {
    githubModuleReport {
    }
}


