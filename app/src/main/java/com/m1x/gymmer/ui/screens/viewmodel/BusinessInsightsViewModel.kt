package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BusinessInsightsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        BusinessInsightsUiState(
            gymPulse = listOf(
                PulseState("Downtown Powerhouse", "84% CAPACITY"),
                PulseState("East Side HIIT Lab", "42% CAPACITY"),
                PulseState("The Foundry (North)", "76% CAPACITY (PEAK)")
            ),
            managementTasks = listOf(
                ManagementTask("Membership Plans", "Adjust pricing tiers, seasonal discounts, and contract terms."),
                ManagementTask("Gym House Rules", "Update safety protocols, guest policies, and behavior guidelines."),
                ManagementTask("Access Control", "Manage RFID permissions, staff overrides, and emergency locks.")
            )
        )
    )
    val uiState: StateFlow<BusinessInsightsUiState> = _uiState.asStateFlow()

    fun onPeriodSelected(period: String) {
        _uiState.update { it.copy(revenueKinetics = it.revenueKinetics.copy(selectedPeriod = period)) }
    }
}
