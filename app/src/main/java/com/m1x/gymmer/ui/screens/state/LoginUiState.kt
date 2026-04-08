package com.m1x.gymmer.ui.screens.state

enum class UserRole {
    TRAINEE, TRAINER, BUSINESS
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedRole: UserRole = UserRole.TRAINEE
)
