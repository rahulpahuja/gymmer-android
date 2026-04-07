package com.m1x.gymmer.ui.screens.state

data class ScanUiState(
    val isFlashlightOn: Boolean = false,
    val scanningStatus: ScanningStatus = ScanningStatus.IDLE,
    val recentActivity: String = "Logged into Main Gym at 6:00 PM"
)

enum class ScanningStatus {
    IDLE, SCANNING, SUCCESS, ERROR
}
