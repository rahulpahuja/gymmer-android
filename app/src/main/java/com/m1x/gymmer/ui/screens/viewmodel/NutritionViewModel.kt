package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.FoodItemState
import com.m1x.gymmer.ui.screens.state.MealState
import com.m1x.gymmer.ui.screens.state.NutritionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NutritionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        NutritionUiState(
            meals = listOf(
                MealState(
                    1, "Breakfast", 450, 25f, 50f, 15f,
                    listOf(FoodItemState(1, "Oats", "1 cup", 300), FoodItemState(2, "Banana", "1 medium", 105))
                ),
                MealState(2, "Lunch", 600, 45f, 60f, 20f, emptyList())
            )
        )
    )
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()
}
