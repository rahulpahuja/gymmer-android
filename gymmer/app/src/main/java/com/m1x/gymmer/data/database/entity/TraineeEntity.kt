package com.m1x.gymmer.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trainees")
data class TraineeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val level: String,
    val plan: String,
    val adherence: String,
    val lastActivity: String,
    val isFavorite: Boolean = false
)
