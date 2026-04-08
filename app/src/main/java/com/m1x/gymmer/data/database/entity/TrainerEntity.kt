package com.m1x.gymmer.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trainers")
data class TrainerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val specialty: String,
    val isSelected: Boolean = false,
    val avatarUrl: String? = null,
    val rating: Double = 0.0,
    val experienceYears: Int = 0
)
