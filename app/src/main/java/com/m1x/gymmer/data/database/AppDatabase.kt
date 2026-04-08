package com.m1x.gymmer.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.m1x.gymmer.data.database.dao.*
import com.m1x.gymmer.data.database.entity.*

@Database(
    entities = [
        TraineeEntity::class,
        ExerciseEntity::class,
        WorkoutLogEntity::class,
        UserSessionEntity::class,
        MealEntity::class,
        FoodItemEntity::class,
        PostEntity::class,
        LeaderboardEntryEntity::class,
        MessageEntity::class,
        FeedbackEntity::class,
        GymClassEntity::class,
        ClassBookingEntity::class,
        TrainerEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun traineeDao(): TraineeDao
    abstract fun trainerDao(): TrainerDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun nutritionDao(): NutritionDao
    abstract fun socialDao(): SocialDao
    abstract fun communicationDao(): CommunicationDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gymmer_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
