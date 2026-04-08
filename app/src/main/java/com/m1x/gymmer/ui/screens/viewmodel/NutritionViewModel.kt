package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.FoodItemState
import com.m1x.gymmer.ui.screens.state.MealState
import com.m1x.gymmer.ui.screens.state.NutritionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class NutritionViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    // Mock User ID - in a real app, this would come from a SessionManager
    private val currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000000")

    private val _uiState = MutableStateFlow(NutritionUiState())
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()

    init {
        loadTodayNutrition()
    }

    private fun loadTodayNutrition() {
        viewModelScope.launch {
            try {
                val plan = repository.getTodayNutrition(currentUserId)
                _uiState.update { state ->
                    state.copy(
                        consumedCalories = plan.totalCalories ?: 0,
                        meals = listOf(
                            MealState(
                                id = plan.id?.hashCode()?.toLong() ?: 0L,
                                name = "Today's Plan",
                                calories = plan.totalCalories ?: 0,
                                protein = 0f,
                                carbs = 0f,
                                fat = 0f,
                                items = listOf(
                                    FoodItemState(
                                        id = 1L,
                                        name = plan.meals ?: "No meals planned",
                                        quantity = "Daily",
                                        calories = plan.totalCalories ?: 0
                                    )
                                )
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load nutrition: ${e.message}")
            }
        }
    }
}
