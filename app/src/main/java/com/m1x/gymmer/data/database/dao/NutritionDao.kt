package com.m1x.gymmer.data.database.dao

import androidx.room.*
import com.m1x.gymmer.data.database.entity.FoodItemEntity
import com.m1x.gymmer.data.database.entity.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NutritionDao {
    @Query("SELECT * FROM meals WHERE date >= :startOfDay AND date <= :endOfDay")
    fun getMealsForDay(startOfDay: Long, endOfDay: Long): Flow<List<MealEntity>>

    @Insert
    suspend fun insertMeal(meal: MealEntity): Long

    @Query("SELECT * FROM food_items WHERE mealId = :mealId")
    fun getFoodItemsForMeal(mealId: Long): Flow<List<FoodItemEntity>>

    @Insert
    suspend fun insertFoodItem(foodItem: FoodItemEntity)

    @Delete
    suspend fun deleteMeal(meal: MealEntity)
}
