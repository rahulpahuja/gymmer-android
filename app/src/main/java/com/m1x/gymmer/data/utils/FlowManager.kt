package com.m1x.gymmer.data.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Manager to handle application-wide event streams (Event Bus).
 * Allows components to emit and collect events without direct coupling.
 */
class FlowManager {

    private val _appEvents = MutableSharedFlow<AppEvent>(extraBufferCapacity = 10)
    val appEvents = _appEvents.asSharedFlow()

    /**
     * Emits a new application event.
     */
    suspend fun emitEvent(event: AppEvent) {
        _appEvents.emit(event)
    }

    /**
     * Tries to emit an event without suspending.
     */
    fun tryEmitEvent(event: AppEvent): Boolean {
        return _appEvents.tryEmit(event)
    }
}

/**
 * Sealed class representing possible application-level events.
 */
sealed class AppEvent {
    data object UserLoggedOut : AppEvent()
    data class ShowToast(val message: String) : AppEvent()
    data class NotificationReceived(val title: String, val body: String) : AppEvent()
    data object SessionExpired : AppEvent()
}
