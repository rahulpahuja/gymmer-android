package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.HistoryItemState
import com.m1x.gymmer.ui.screens.state.MetricState
import com.m1x.gymmer.ui.screens.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    // Mock User ID - in a real app, this would come from a SessionManager
    private val currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000000")

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val user = repository.getProfile(currentUserId)
                _uiState.update { state ->
                    state.copy(
                        name = user.name ?: "Unknown",
                        status = user.role ?: "MEMBER",
                        focus = user.status ?: "ACTIVE",
                        // Keep mock metrics for now as API doesn't have detailed goals yet
                        goalMetrics = listOf(
                            MetricState("WEIGHT REDUCTION", 0.82f, "82%"),
                            MetricState("LEAN MUSCLE MASS", 0.65f, "65%"),
                            MetricState("DEADLIFT PR (240KG)", 0.94f, "94%")
                        ),
                        kineticHistory = listOf(
                            HistoryItemState("JOINED_GYM", user.createdAt?.take(10) ?: "N/A", "MEMBERSHIP START")
                        )
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load profile: ${e.message}")
            }
        }
    }
}
