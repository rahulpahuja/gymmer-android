package com.m1x.gymmer.ui.screens.state

data class ChatUiState(
    val messages: List<MessageState> = emptyList(),
    val currentUserId: String = "",
    val otherUserName: String = "Trainer",
    val isLoading: Boolean = false,
    val isSending: Boolean = false
)

data class MessageState(
    val id: Long,
    val content: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val isRead: Boolean
)
