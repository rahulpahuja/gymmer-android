package com.m1x.gymmer.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val description: String,
    val videoUrl: String?,
    val muscleGroup: String
)

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseId: String,
    val date: Long,
    val sets: Int,
    val reps: Int,
    val weight: Float,
    val durationMillis: Long,
    val notes: String?
)

@Entity(tableName = "user_sessions")
data class UserSessionEntity(
    @PrimaryKey val sessionId: String,
    val startTime: Long,
    val endTime: Long?,
    val caloriesBurned: Int?,
    val mood: String?
)
