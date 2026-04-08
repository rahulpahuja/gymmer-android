package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.AttentionTrainee
import com.m1x.gymmer.ui.screens.state.MyTraineesUiState
import com.m1x.gymmer.ui.screens.state.RosterTrainee
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class MyTraineesViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    // Mock Trainer ID - in a real app, this would come from a SessionManager
    private val trainerId = UUID.fromString("11111111-1111-1111-1111-111111111111")

    private val _uiState = MutableStateFlow(MyTraineesUiState())
    val uiState: StateFlow<MyTraineesUiState> = _uiState.asStateFlow()

    init {
        loadTrainees()
    }

    private fun loadTrainees() {
        viewModelScope.launch {
            try {
                val trainees = repository.getTrainees(trainerId)
                _uiState.update { state ->
                    state.copy(
                        activeRoster = trainees.map { user ->
                            RosterTrainee(
                                id = user.id.toString(),
                                name = user.name ?: "Unknown",
                                plan = "${user.role ?: "MEMBER"} • ACTIVE",
                                adherence = "100%", // Mock adherence
                                lastActivity = "Joined: ${user.createdAt?.take(10) ?: "N/A"}"
                            )
                        },
                        // Mock needs attention for now
                        needsAttention = listOf(
                            AttentionTrainee("1", "Isha Gupta", "INACTIVE: 4 DAYS"),
                            AttentionTrainee("2", "Karan Malhotra", "MISSED: MAX PR TEST")
                        )
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load trainees: ${e.message}")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}
