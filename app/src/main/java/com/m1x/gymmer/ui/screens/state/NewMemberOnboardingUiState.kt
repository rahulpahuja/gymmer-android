package com.m1x.gymmer.ui.screens.state

data class NewMemberOnboardingUiState(
    val athleteName: String = "",
    val age: String = "24",
    val selectedGoals: Set<String> = setOf("HYPERTROPHY"),
    val availableGoals: List<String> = listOf("HYPERTROPHY", "FAT REDUCTION", "ENDURANCE ENGINE", "POWERLIFTING"),
    val membershipPlans: List<OnboardingPlanState> = emptyList(),
    val availableTrainers: List<OnboardingTrainerState> = emptyList(),
    val totalDueToday: String = "₹24,999",
    val contractEndDate: String = "AUG 12, 2024"
)

data class OnboardingPlanState(
    val title: String,
    val subtitle: String,
    val price: String,
    val unit: String,
    val isRecommended: Boolean,
    val isSelected: Boolean = false
)

data class OnboardingTrainerState(
    val name: String,
    val specialty: String,
    val isSelected: Boolean = false,
    val imageUrl: String? = null
)
