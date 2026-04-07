package com.m1x.gymmer.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long,
    val isRead: Boolean = false
)

@Entity(tableName = "workout_feedback")
data class FeedbackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutLogId: Long,
    val trainerId: String,
    val feedbackText: String,
    val timestamp: Long
)
