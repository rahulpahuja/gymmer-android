package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.TraineeSummary
import com.m1x.gymmer.ui.screens.state.WorkoutExerciseState
import com.m1x.gymmer.ui.screens.state.WorkoutsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WorkoutsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        WorkoutsUiState(
            trainees = listOf(
                TraineeSummary("1", "Aravind Kumar", "LEVEL: ADVANCED", true),
                TraineeSummary("2", "Sneha Rao", "LEVEL: INTERMEDIATE", false)
            ),
            libraryExercises = listOf("Lat Pulldown", "Barbell Squats", "Deadlifts", "Lunges", "Pull Ups"),
            sessionExercises = listOf(
                WorkoutExerciseState("1", "LAT PULLDOWN", "LATISSIMUS DORSI", "4", "10-12", "3-0-1-0", "90s"),
                WorkoutExerciseState("2", "BARBELL SQUATS", "QUADRICEPS & GLUTES", "3", "8", "8.5", "3m")
            )
        )
    )
    val uiState: StateFlow<WorkoutsUiState> = _uiState.asStateFlow()

    fun onTraineeSelected(traineeId: String) {
        _uiState.update { state ->
            state.copy(
                selectedTraineeId = traineeId,
                trainees = state.trainees.map { it.copy(isSelected = it.id == traineeId) }
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(exerciseSearchQuery = query) }
    }
}
