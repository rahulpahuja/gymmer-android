package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.MachineState
import com.m1x.gymmer.ui.screens.state.TrainerStudioUiState
import com.m1x.gymmer.ui.screens.state.UploadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TrainerStudioViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        TrainerStudioUiState(
            machineLibrary = listOf(
                MachineState("1", "Lat Pulldown", "3 LINKED VIDEOS"),
                MachineState("2", "Leg Press", "4 LINKED VIDEOS"),
                MachineState("3", "Chest Press", "5 LINKED VIDEOS")
            ),
            recentUploads = listOf(
                UploadState("1", "Back Squat Form Mastery", "Machine: Smith Machine", true),
                UploadState("2", "Single Arm Cable Row", "Machine: Cable Crossover", false),
                UploadState("3", "Optimal Foot Placement: Leg Press", "Machine: Leg Press", true)
            )
        )
    )
    val uiState: StateFlow<TrainerStudioUiState> = _uiState.asStateFlow()
}
