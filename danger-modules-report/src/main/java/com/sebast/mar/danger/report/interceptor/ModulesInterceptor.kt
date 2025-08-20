package com.sebast.mar.danger.report.interceptor

import com.sebast.mar.danger.report.info.Module

/**
 * Interface for intercepting and potentially modifying a list of [Module]s.
 * This can be used to add, remove, or alter modules before they are processed further.
 */
public fun interface ModulesInterceptor {
    /**
     * Intercepts a list of modules and returns a list of modules.
     * This method can be used to filter out modules that are not relevant for the current report.
     *
     * @param modules The list of modules to intercept.
     * @return The list of modules that are relevant for the current report.
     */
    public fun intercept(modules: List<Module>): List<Module>
}

/**
 * A [ModulesInterceptor] that doesn't make any changes to the modules.
 */
internal object NoOpModulesInterceptor : ModulesInterceptor {
    override fun intercept(modules: List<Module>) = modules
}
