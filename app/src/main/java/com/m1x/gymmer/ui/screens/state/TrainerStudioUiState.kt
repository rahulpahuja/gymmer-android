package com.m1x.gymmer.ui.screens.state

data class TrainerStudioUiState(
    val machineLibrary: List<MachineState> = emptyList(),
    val recentUploads: List<UploadState> = emptyList()
)

data class MachineState(
    val id: String,
    val name: String,
    val videoCount: String,
    val imageUrl: String? = null
)

data class UploadState(
    val id: String,
    val title: String,
    val machineName: String,
    val isActive: Boolean,
    val imageUrl: String? = null
)
