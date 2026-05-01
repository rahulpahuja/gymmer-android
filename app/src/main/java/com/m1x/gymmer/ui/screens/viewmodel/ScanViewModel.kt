package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.ScanUiState
import com.m1x.gymmer.ui.screens.state.ScanningStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun toggleFlashlight() {
        _uiState.update { it.copy(isFlashlightOn = !it.isFlashlightOn) }
    }

    fun onQrCodeScanned(qrContent: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(scanningStatus = ScanningStatus.SCANNING) }
            try {
                // Assuming QR content is the User ID
                val checkIn = repository.checkIn(qrContent)
                
                logManager.info(LogManager.LogCategory.SCAN, "Check-in successful for user: $qrContent at ${checkIn.checkedInAt}")
                
                _uiState.update { 
                    it.copy(
                        scanningStatus = ScanningStatus.SUCCESS,
                        recentActivity = "Last Check-in: ${checkIn.checkedInAt ?: "Just now"}"
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Check-in failed: ${e.message}")
                _uiState.update { 
                    it.copy(
                        scanningStatus = ScanningStatus.ERROR
                    )
                }
            }
        }
    }
    
    fun resetStatus() {
        _uiState.update { it.copy(scanningStatus = ScanningStatus.IDLE) }
    }
}
