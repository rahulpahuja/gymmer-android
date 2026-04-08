package com.m1x.gymmer.data.database.dao

import androidx.room.*
import com.m1x.gymmer.data.database.entity.TrainerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainerDao {
    @Query("SELECT * FROM trainers")
    fun getAllTrainers(): Flow<List<TrainerEntity>>

    @Query("SELECT * FROM trainers WHERE id = :id")
    suspend fun getTrainerById(id: String): TrainerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrainers(trainers: List<TrainerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrainer(trainer: TrainerEntity)

    @Delete
    suspend fun deleteTrainer(trainer: TrainerEntity)

    @Query("DELETE FROM trainers")
    suspend fun deleteAll()

    @Query("UPDATE trainers SET isSelected = :isSelected WHERE id = :trainerId")
    suspend fun updateSelection(trainerId: String, isSelected: Boolean)

    @Query("UPDATE trainers SET isSelected = 0")
    suspend fun clearAllSelections()
}
