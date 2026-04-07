package com.m1x.gymmer.ui.screens.state

data class DashboardUiState(
    val lastActivity: String = "Yesterday, 18:45",
    val checkInRequired: Boolean = true,
    val premiumPlan: PremiumPlanState = PremiumPlanState(),
    val todaysTarget: TodaysTargetState = TodaysTargetState(),
    val activeSessions: List<ActiveSession> = emptyList()
)

data class PremiumPlanState(
    val name: String = "Premium Plan",
    val expiresDays: Int = 12,
    val progress: Float = 0.8f,
    val isPro: Boolean = true
)

data class TodaysTargetState(
    val title: String = "Shoulders & Triceps",
    val exercises: List<String> = listOf("Overhead Press", "Lateral Raises", "Cable Pushdowns")
)

data class ActiveSession(
    val id: String,
    val name: String,
    val duration: String,
    val calories: String,
    val iconRes: Int? = null
)
