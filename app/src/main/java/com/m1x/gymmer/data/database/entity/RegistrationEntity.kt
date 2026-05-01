package com.m1x.gymmer.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_registrations")
data class RegistrationEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val gymId: String?,
    val name: String?,
    val email: String?,
    val phone: String?,
    val password: String?,
    val role: String?,
    val timestamp: Long = System.currentTimeMillis()
)
