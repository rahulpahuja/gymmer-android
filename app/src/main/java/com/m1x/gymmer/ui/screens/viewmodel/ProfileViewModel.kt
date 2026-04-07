package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.HistoryItemState
import com.m1x.gymmer.ui.screens.state.MetricState
import com.m1x.gymmer.ui.screens.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        ProfileUiState(
            goalMetrics = listOf(
                MetricState("WEIGHT REDUCTION", 0.82f, "82%"),
                MetricState("LEAN MUSCLE MASS", 0.65f, "65%"),
                MetricState("DEADLIFT PR (240KG)", 0.94f, "94%")
            ),
            kineticHistory = listOf(
                HistoryItemState("ISO-LATERAL_CHEST_PRESS", "120_KG", "SESSION_04 • 14:20 PM"),
                HistoryItemState("CURVED_TREADMILL_PRO", "4.2_KM", "STATION_22 • 12:45 PM")
            )
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
}
