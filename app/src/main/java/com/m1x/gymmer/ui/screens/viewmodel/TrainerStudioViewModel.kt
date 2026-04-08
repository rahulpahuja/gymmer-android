package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.MachineState
import com.m1x.gymmer.ui.screens.state.TrainerStudioUiState
import com.m1x.gymmer.ui.screens.state.UploadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class TrainerStudioViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    // Mock Trainer ID
    private val trainerId = UUID.fromString("11111111-1111-1111-1111-111111111111")

    private val _uiState = MutableStateFlow(TrainerStudioUiState())
    val uiState: StateFlow<TrainerStudioUiState> = _uiState.asStateFlow()

    init {
        loadStudioData()
    }

    private fun loadStudioData() {
        viewModelScope.launch {
            try {
                // Fetch Trainer Dashboard data if needed or just exercises
                val exercises = repository.listExercises()
                val dashboard = repository.getTrainerDashboard(trainerId)

                _uiState.update { state ->
                    state.copy(
                        machineLibrary = exercises.map { 
                            MachineState(it.id.toString(), it.name ?: "Unknown", "AVAILABLE")
                        },
                        // Mock uploads since there's no specific "uploads" API
                        recentUploads = listOf(
                            UploadState("1", "Trainer: ${dashboard.trainer?.name ?: "Me"}", "Active Trainees: ${dashboard.activeTraineeCount}", true)
                        )
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load studio data: ${e.message}")
            }
        }
    }
}
