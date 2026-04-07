package com.m1x.gymmer.data.database.dao

import androidx.room.*
import com.m1x.gymmer.data.database.entity.ClassBookingEntity
import com.m1x.gymmer.data.database.entity.GymClassEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM gym_classes WHERE startTime >= :currentTime ORDER BY startTime ASC")
    fun getUpcomingClasses(currentTime: Long): Flow<List<GymClassEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(gymClass: GymClassEntity)

    @Query("SELECT * FROM class_bookings WHERE userId = :userId")
    fun getBookingsForUser(userId: String): Flow<List<ClassBookingEntity>>

    @Insert
    suspend fun bookClass(booking: ClassBookingEntity)

    @Update
    suspend fun updateBooking(booking: ClassBookingEntity)
}
