package com.m1x.gymmer.ui.screens.state

data class AttendanceLogsUiState(
    val searchQuery: String = "",
    val selectedTab: Int = 0,
    val liveNowCount: Int = 42,
    val peakTime: String = "18:00",
    val totalCheckIns: Int = 128,
    val checkInTrend: String = "+12% from yesterday",
    val recentActivity: List<AttendanceActivity> = emptyList()
)

data class AttendanceActivity(
    val id: String,
    val name: String,
    val plan: String,
    val time: String,
    val status: String,
    val isUrgent: Boolean = false
)
