package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.ChatUiState
import com.m1x.gymmer.ui.screens.state.MessageState
import com.m1x.gymmer.ui.screens.state.MessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        ChatUiState(
            messages = listOf(
                MessageState(1, "Hi trainer, I'm feeling a bit sore today.", "10:00 AM", true, true),
                MessageState(2, "That's normal. Make sure to stretch well.", "10:05 AM", false, true),
                MessageState(3, "Got it, will do!", "10:10 AM", true, false),
                MessageState(4, "Voice Note", "10:12 AM", false, true, MessageType.VOICE, "0:15")
            )
        )
    )
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(text: String) {
        val newMessage = MessageState(
            id = (uiState.value.messages.maxOfOrNull { it.id } ?: 0) + 1,
            content = text,
            timestamp = "Just now",
            isFromMe = true,
            isRead = false,
            type = MessageType.TEXT
        )
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + newMessage
        )
    }

    fun sendVoiceNote(duration: String) {
        val newMessage = MessageState(
            id = (uiState.value.messages.maxOfOrNull { it.id } ?: 0) + 1,
            content = "Voice Note",
            timestamp = "Just now",
            isFromMe = true,
            isRead = false,
            type = MessageType.VOICE,
            duration = duration
        )
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + newMessage
        )
    }
}
