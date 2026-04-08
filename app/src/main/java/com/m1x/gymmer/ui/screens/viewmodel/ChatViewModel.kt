package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.ChatUiState
import com.m1x.gymmer.ui.screens.state.MessageState
import com.m1x.gymmer.ui.screens.state.MessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    // Mock User IDs - in a real app, these would come from a SessionManager/Navigation args
    private val currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000000")
    private val trainerId = UUID.fromString("11111111-1111-1111-1111-111111111111")

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            try {
                val messages = repository.getMessages(trainerId, currentUserId)
                _uiState.update { state ->
                    state.copy(
                        messages = messages.map { msg ->
                            MessageState(
                                id = msg.id?.hashCode()?.toLong() ?: 0L,
                                content = msg.content ?: "",
                                timestamp = msg.sentAt?.split("T")?.lastOrNull()?.take(5) ?: "??:??",
                                isFromMe = msg.senderId == currentUserId,
                                isRead = true, // Backend doesn't have isRead yet
                                type = MessageType.TEXT
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load messages: ${e.message}")
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            try {
                val sentMsg = repository.sendMessage(trainerId, currentUserId, text)
                val newMessage = MessageState(
                    id = sentMsg.id?.hashCode()?.toLong() ?: System.currentTimeMillis(),
                    content = sentMsg.content ?: text,
                    timestamp = "Just now",
                    isFromMe = true,
                    isRead = false,
                    type = MessageType.TEXT
                )
                _uiState.update { it.copy(messages = it.messages + newMessage) }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to send message: ${e.message}")
            }
        }
    }

    fun sendVoiceNote(duration: String) {
        // API doesn't support voice notes yet, so we'll mock it locally for UI completeness
        val newMessage = MessageState(
            id = System.currentTimeMillis(),
            content = "Voice Note",
            timestamp = "Just now",
            isFromMe = true,
            isRead = false,
            type = MessageType.VOICE,
            duration = duration
        )
        _uiState.update { it.copy(messages = it.messages + newMessage) }
    }
}
