package com.m1x.gymmer.ui.screens.state

data class WorkoutsUiState(
    val selectedTraineeId: String = "1",
    val trainees: List<TraineeSummary> = emptyList(),
    val exerciseSearchQuery: String = "",
    val libraryExercises: List<String> = emptyList(),
    val currentSession: WorkoutSessionState = WorkoutSessionState(),
    val sessionExercises: List<WorkoutExerciseState> = emptyList()
)

data class TraineeSummary(
    val id: String,
    val name: String,
    val level: String,
    val isSelected: Boolean = false
)

data class WorkoutSessionState(
    val name: String = "HYPERTROPHY_A",
    val target: String = "Targeting: Posterior Chain & Lats • Est. 65min"
)

data class WorkoutExerciseState(
    val id: String,
    val name: String,
    val target: String,
    val sets: String,
    val reps: String,
    val tempo: String,
    val rest: String
)
