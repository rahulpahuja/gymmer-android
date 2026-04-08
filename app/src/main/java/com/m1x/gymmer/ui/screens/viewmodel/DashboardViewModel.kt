package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    // Mock User ID for now - in a real app, this would come from a SessionManager/DataStore
    private val currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000000")

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            try {
                val data = repository.getDashboard(currentUserId)
                _uiState.update { state ->
                    state.copy(
                        lastActivity = data.todayCheckIn?.checkedInAt ?: "No activity today",
                        checkInRequired = data.todayCheckIn == null,
                        activeSessions = listOf(
                            ActiveSession("1", "Active Sessions: ${data.activeSessionCount}", "N/A", "N/A")
                        )
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load dashboard: ${e.message}")
                // Fallback to initial/mock state on error
            }
        }
    }

    fun onCheckInClicked() {
        viewModelScope.launch {
            try {
                repository.getProfile(currentUserId) // Just a placeholder for actual check-in logic if needed
                // In a real app, we'd call repository.checkIn(currentUserId)
                _uiState.update { it.copy(checkInRequired = false) }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Check-in failed: ${e.message}")
            }
        }
    }
}
