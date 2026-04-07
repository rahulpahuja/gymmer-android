package com.m1x.gymmer.ui.screens.state

data class MyTraineesUiState(
    val searchQuery: String = "",
    val needsAttention: List<AttentionTrainee> = emptyList(),
    val activeRoster: List<RosterTrainee> = emptyList()
)

data class AttentionTrainee(
    val id: String,
    val name: String,
    val reason: String
)

data class RosterTrainee(
    val id: String,
    val name: String,
    val plan: String,
    val adherence: String,
    val lastActivity: String
)
