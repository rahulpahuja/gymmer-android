package com.m1x.gymmer.data.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineManagerTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    
    private val coroutineManager = CoroutineManager(
        main = testDispatcher,
        io = testDispatcher,
        default = testDispatcher,
        unconfined = testDispatcher
    )

    @Test
    fun `io executes block on io dispatcher`() = testScope.runTest {
        var result = ""
        coroutineManager.io {
            result = "executed"
        }
        assertEquals("executed", result)
    }

    @Test
    fun `main executes block on main dispatcher`() = testScope.runTest {
        var result = ""
        coroutineManager.main {
            result = "executed"
        }
        assertEquals("executed", result)
    }

    @Test
    fun `default executes block on default dispatcher`() = testScope.runTest {
        var result = ""
        coroutineManager.default {
            result = "executed"
        }
        assertEquals("executed", result)
    }
}
