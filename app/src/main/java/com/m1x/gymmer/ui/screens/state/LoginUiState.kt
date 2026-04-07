package com.m1x.gymmer.ui.screens.state

enum class UserRole {
    TRAINEE, TRAINER
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val selectedRole: UserRole = UserRole.TRAINEE,
    val isLoading: Boolean = false,
    val error: String? = null
)
