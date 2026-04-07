package com.m1x.gymmer.data.database.dao

import androidx.room.*
import com.m1x.gymmer.data.database.entity.FeedbackEntity
import com.m1x.gymmer.data.database.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunicationDao {
    @Query("SELECT * FROM messages WHERE (senderId = :userId AND receiverId = :otherId) OR (senderId = :otherId AND receiverId = :userId) ORDER BY timestamp ASC")
    fun getChatHistory(userId: String, otherId: String): Flow<List<MessageEntity>>

    @Insert
    suspend fun sendMessage(message: MessageEntity)

    @Query("SELECT * FROM workout_feedback WHERE workoutLogId = :workoutLogId")
    fun getFeedbackForWorkout(workoutLogId: Long): Flow<List<FeedbackEntity>>

    @Insert
    suspend fun insertFeedback(feedback: FeedbackEntity)
}
