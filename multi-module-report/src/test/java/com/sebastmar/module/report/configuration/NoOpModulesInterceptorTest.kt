package com.sebastmar.module.report.configuration

import com.sebastmar.module.report.info.Module
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.test.assertSame

internal class NoOpModulesInterceptorTest {

    private val interceptor: ModulesInterceptor = NoOpModulesInterceptor

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify it returns the same list instance without modifications`() {
        val givenModules = listOf<Module>(mockk())

        val result = interceptor.intercept(givenModules)

        assertSame(givenModules, result)
    }
}
