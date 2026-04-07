package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.ChatUiState
import com.m1x.gymmer.ui.screens.state.MessageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        ChatUiState(
            messages = listOf(
                MessageState(1, "Hi trainer, I'm feeling a bit sore today.", "10:00 AM", true, true),
                MessageState(2, "That's normal. Make sure to stretch well.", "10:05 AM", false, true),
                MessageState(3, "Got it, will do!", "10:10 AM", true, false)
            )
        )
    )
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(text: String) {
        // Implementation for sending message
    }
}
