package com.m1x.gymmer.ui.screens.state

data class BusinessInsightsUiState(
    val annualRevenue: RevenueState = RevenueState(),
    val retention: RetentionState = RetentionState(),
    val growth: GrowthState = GrowthState(),
    val gymPulse: List<PulseState> = emptyList(),
    val managementTasks: List<ManagementTask> = emptyList(),
    val revenueKinetics: RevenueKineticsState = RevenueKineticsState()
)

data class RevenueState(
    val amount: String = "₹2,84,00,000",
    val trend: String = "+12.4%",
    val profitMargin: String = "89%"
)

data class RetentionState(
    val percentage: String = "94.2%",
    val description: String = "Your member loyalty is at an all-time high across all branches."
)

data class GrowthState(
    val quarterlyGain: String = "+450",
    val activeTotal: String = "12,402",
    val chartData: List<Float> = emptyList()
)

data class PulseState(
    val name: String,
    val capacity: String
)

data class ManagementTask(
    val title: String,
    val description: String
)

data class RevenueKineticsState(
    val selectedPeriod: String = "MONTHLY",
    val periods: List<String> = listOf("DAILY", "MONTHLY", "QUARTERLY"),
    val chartData: List<Float> = listOf(0.4f, 0.6f, 0.5f, 0.8f, 0.7f, 0.9f, 0.75f, 0.85f, 0.95f, 0.8f, 0.9f, 1.0f)
)
