package com.m1x.gymmer.data.utils

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlowManagerTest {

    private val flowManager = FlowManager()

    @Test
    fun `emitEvent sends event to flow`() = runTest {
        val event = AppEvent.UserLoggedOut
        
        flowManager.appEvents.test {
            flowManager.emitEvent(event)
            assertEquals(event, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `tryEmitEvent sends event to flow`() = runTest {
        val event = AppEvent.ShowToast("Hello")
        
        flowManager.appEvents.test {
            val result = flowManager.tryEmitEvent(event)
            assertTrue(result)
            assertEquals(event, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `multiple events are received in order`() = runTest {
        val events = listOf(
            AppEvent.UserLoggedOut,
            AppEvent.SessionExpired,
            AppEvent.ShowToast("Error")
        )
        
        flowManager.appEvents.test {
            events.forEach { flowManager.emitEvent(it) }
            
            assertEquals(events[0], awaitItem())
            assertEquals(events[1], awaitItem())
            assertEquals(events[2], awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
