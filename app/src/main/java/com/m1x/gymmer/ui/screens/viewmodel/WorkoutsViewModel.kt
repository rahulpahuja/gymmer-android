package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.TraineeSummary
import com.m1x.gymmer.ui.screens.state.WorkoutExerciseState
import com.m1x.gymmer.ui.screens.state.WorkoutsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class WorkoutsViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    // Mock Trainer ID - in a real app, this would come from a SessionManager
    private val trainerId = UUID.fromString("11111111-1111-1111-1111-111111111111")

    private val _uiState = MutableStateFlow(WorkoutsUiState())
    val uiState: StateFlow<WorkoutsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                // Fetch Trainees
                val trainees = repository.getTrainees(trainerId)
                _uiState.update { state ->
                    state.copy(
                        trainees = trainees.map { user ->
                            TraineeSummary(
                                id = user.id.toString(),
                                name = user.name ?: "Unknown",
                                level = "LEVEL: ${user.status ?: "ACTIVE"}",
                                isSelected = false
                            )
                        }
                    )
                }

                // Fetch Exercises for Library
                val exercises = repository.listExercises()
                _uiState.update { state ->
                    state.copy(
                        libraryExercises = exercises.map { it.name ?: "Unnamed Exercise" }
                    )
                }

                // Fetch Workout Plans
                val plans = repository.listWorkouts()
                // For now, let's just use the first plan's ID if available
                if (plans.isNotEmpty()) {
                    val fullPlan = repository.getWorkout(plans.first().id ?: UUID.randomUUID())
                    // Mapping to WorkoutExerciseState - assuming some defaults as Backend is simple
                    _uiState.update { state ->
                        state.copy(
                            sessionExercises = listOf(
                                WorkoutExerciseState(
                                    id = fullPlan.id.toString(),
                                    name = fullPlan.name?.uppercase() ?: "WORKOUT",
                                    target = fullPlan.description ?: "GENERAL",
                                    sets = "3",
                                    reps = "10",
                                    tempo = "3-0-1-0",
                                    rest = "90s"
                                )
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load workouts data: ${e.message}")
            }
        }
    }

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
