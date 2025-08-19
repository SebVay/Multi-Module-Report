package com.sebast.mar.danger.report.interceptor

import com.sebast.mar.danger.report.Module

public fun interface ModuleInterceptor {
    public fun intercept(module: Module): Module?
}

/**
 * A [ModuleInterceptor] that doesn't make any changes to a [Module].
 */
internal object NoOpModuleInterceptor : ModuleInterceptor {
    override fun intercept(module: Module) = module
}
