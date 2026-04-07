package com.m1x.gymmer.ui.screens.state

data class ProfileUiState(
    val name: String = "AKASH_VERMA",
    val status: String = "ELITE_MEMBER",
    val focus: String = "Hypertrophy & Conditioning Focus",
    val assignedTrainer: TrainerInfo = TrainerInfo(),
    val goalMetrics: List<MetricState> = emptyList(),
    val kineticHistory: List<HistoryItemState> = emptyList(),
    val membershipStatus: MembershipStatus = MembershipStatus()
)

data class TrainerInfo(
    val name: String = "VIKRAM_SINGH",
    val specialty: String = "Strength & Biomechanics Specialist"
)

data class MetricState(
    val label: String,
    val progress: Float,
    val value: String
)

data class HistoryItemState(
    val title: String,
    val value: String,
    val subtitle: String
)

data class MembershipStatus(
    val status: String = "ACTIVE",
    val until: String = "UNTIL_DEC_2024",
    val description: String = "Access to all premium zones including the High-Performance Recovery Suite."
)
