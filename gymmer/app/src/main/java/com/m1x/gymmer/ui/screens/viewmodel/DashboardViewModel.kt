package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.ActiveSession
import com.m1x.gymmer.ui.screens.state.DashboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        DashboardUiState(
            activeSessions = listOf(
                ActiveSession("1", "Precision Tricep Blast", "45m", "320kcal")
            )
        )
    )
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun onCheckInClicked() {
        _uiState.update { it.copy(checkInRequired = false) }
    }
}
