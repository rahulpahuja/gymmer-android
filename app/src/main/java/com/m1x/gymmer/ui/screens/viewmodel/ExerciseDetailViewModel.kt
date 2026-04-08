package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.ExerciseDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ExerciseDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    private val _uiState = MutableStateFlow(ExerciseDetailUiState())
    val uiState: StateFlow<ExerciseDetailUiState> = _uiState.asStateFlow()

    fun loadExercise(exerciseId: UUID) {
        viewModelScope.launch {
            try {
                val exercise = repository.getExercise(exerciseId)
                _uiState.update { state ->
                    state.copy(
                        name = exercise.name ?: "Unknown Exercise",
                        category = exercise.category ?: "General",
                        status = exercise.difficulty ?: "AVAILABLE", // Using difficulty as status for now
                        videoUrl = exercise.videoUrl ?: ""
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load exercise: ${e.message}")
            }
        }
    }
}
