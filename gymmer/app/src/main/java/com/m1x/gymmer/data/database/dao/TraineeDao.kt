package com.m1x.gymmer.data.database.dao

import androidx.room.*
import com.m1x.gymmer.data.database.entity.TraineeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TraineeDao {
    @Query("SELECT * FROM trainees")
    fun getAllTrainees(): Flow<List<TraineeEntity>>

    @Query("SELECT * FROM trainees WHERE id = :id")
    suspend fun getTraineeById(id: String): TraineeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrainees(trainees: List<TraineeEntity>)

    @Delete
    suspend fun deleteTrainee(trainee: TraineeEntity)

    @Query("DELETE FROM trainees")
    suspend fun deleteAll()
}
