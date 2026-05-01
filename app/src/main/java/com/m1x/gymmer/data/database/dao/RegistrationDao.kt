package com.m1x.gymmer.data.database.dao

import androidx.room.*
import com.m1x.gymmer.data.database.entity.RegistrationEntity

@Dao
interface RegistrationDao {
    @Query("SELECT * FROM pending_registrations ORDER BY timestamp ASC")
    suspend fun getPendingRegistrations(): List<RegistrationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: RegistrationEntity)

    @Delete
    suspend fun deleteRegistration(registration: RegistrationEntity)

    @Query("DELETE FROM pending_registrations")
    suspend fun deleteAll()
}
