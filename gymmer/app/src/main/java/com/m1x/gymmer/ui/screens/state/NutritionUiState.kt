package com.m1x.gymmer.ui.screens.state

data class NutritionUiState(
    val dailyCaloriesTarget: Int = 2500,
    val consumedCalories: Int = 1850,
    val proteinTarget: Float = 180f,
    val consumedProtein: Float = 120f,
    val carbsTarget: Float = 300f,
    val consumedCarbs: Float = 210f,
    val fatTarget: Float = 80f,
    val consumedFat: Float = 55f,
    val meals: List<MealState> = emptyList(),
    val isLoading: Boolean = false
)

data class MealState(
    val id: Long,
    val name: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val items: List<FoodItemState> = emptyList()
)

data class FoodItemState(
    val id: Long,
    val name: String,
    val quantity: String,
    val calories: Int
)
