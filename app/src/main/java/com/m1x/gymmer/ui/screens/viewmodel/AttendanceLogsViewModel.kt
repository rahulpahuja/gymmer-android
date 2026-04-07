package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.AttendanceActivity
import com.m1x.gymmer.ui.screens.state.AttendanceLogsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AttendanceLogsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        AttendanceLogsUiState(
            recentActivity = listOf(
                AttendanceActivity("1", "Vikram Rathore", "Elite Annual • ID #88421", "11:42 AM", "ACTIVE"),
                AttendanceActivity("2", "Ananya Iyer", "Monthly Flex • ID #92015", "11:35 AM", "ACTIVE"),
                AttendanceActivity("3", "Siddharth Malhotra", "Elite Annual • ID #77129", "11:15 AM", "GRACE PERIOD", isUrgent = true)
            )
        )
    )
    val uiState: StateFlow<AttendanceLogsUiState> = _uiState.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }
}
