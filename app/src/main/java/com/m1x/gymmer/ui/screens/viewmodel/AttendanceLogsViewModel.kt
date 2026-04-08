package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.AttendanceActivity
import com.m1x.gymmer.ui.screens.state.AttendanceLogsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class AttendanceLogsViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    // Mock Trainer ID
    private val trainerId = UUID.fromString("11111111-1111-1111-1111-111111111111")

    private val _uiState = MutableStateFlow(AttendanceLogsUiState())
    val uiState: StateFlow<AttendanceLogsUiState> = _uiState.asStateFlow()

    init {
        loadAttendance()
    }

    private fun loadAttendance() {
        viewModelScope.launch {
            try {
                val trainees = repository.getTrainees(trainerId)
                // For a more realistic "Attendance Log", we'd ideally have an API that returns 
                // recent check-ins for the gym. Using trainees' last activity as a proxy for now.
                _uiState.update { state ->
                    state.copy(
                        recentActivity = trainees.map { user ->
                            AttendanceActivity(
                                id = user.id.toString(),
                                name = user.name ?: "Unknown",
                                plan = "${user.role ?: "MEMBER"} • ${user.email ?: "N/A"}",
                                time = user.createdAt?.split("T")?.lastOrNull()?.take(5) ?: "10:00",
                                status = user.status ?: "ACTIVE",
                                isUrgent = user.status == "GRACE PERIOD"
                            )
                        },
                        liveNowCount = trainees.size,
                        totalCheckIns = trainees.size * 2 // Mocking trend
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load attendance logs: ${e.message}")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }
}
