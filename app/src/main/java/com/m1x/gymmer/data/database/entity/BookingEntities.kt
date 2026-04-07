package com.m1x.gymmer.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gym_classes")
data class GymClassEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val instructorName: String,
    val startTime: Long,
    val durationMinutes: Int,
    val capacity: Int,
    val bookedCount: Int,
    val category: String // Yoga, HIIT, etc.
)

@Entity(tableName = "class_bookings")
data class ClassBookingEntity(
    @PrimaryKey(autoGenerate = true) val bookingId: Long = 0,
    val classId: String,
    val userId: String,
    val bookingTime: Long,
    val status: String // CONFIRMED, CANCELLED, WAITLIST
)
