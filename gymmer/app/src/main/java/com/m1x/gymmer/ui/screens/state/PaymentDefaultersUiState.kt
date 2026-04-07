package com.m1x.gymmer.ui.screens.state

data class PaymentDefaultersUiState(
    val revenueRisk: Double = 1240.0,
    val complianceCount: Int = 12,
    val selectedTab: String = "CRITICAL (>7 DAYS)",
    val defaulters: List<DefaulterState> = emptyList(),
    val isAutomatedRecoveryActive: Boolean = true
)

data class DefaulterState(
    val id: String,
    val name: String,
    val plan: String,
    val daysLate: String,
    val amount: Double
)
