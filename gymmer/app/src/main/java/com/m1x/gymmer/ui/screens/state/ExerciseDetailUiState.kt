package com.m1x.gymmer.ui.screens.state

data class ExerciseDetailUiState(
    val name: String = "LAT PULLDOWN MACHINE",
    val status: String = "AVAILABLE",
    val category: String = "UPPER BODY • STRENGTH",
    val videoUrl: String? = null,
    val correctForm: List<String> = listOf(
        "Pull bar down to upper chest level.",
        "Keep chest out and shoulders back.",
        "Squeeze shoulder blades together."
    ),
    val commonMistakes: List<String> = listOf(
        "Leaning back excessively to pull.",
        "Pulling the bar behind the neck."
    ),
    val trainerAssignment: TrainerAssignmentState = TrainerAssignmentState()
)

data class TrainerAssignmentState(
    val setsReps: String = "3x12",
    val weightRange: String = "45 - 55 KG",
    val rest: String = "60 SEC"
)
