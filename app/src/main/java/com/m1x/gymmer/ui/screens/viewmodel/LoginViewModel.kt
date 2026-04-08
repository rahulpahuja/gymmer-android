package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.network.models.LoginRequest
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.LoginUiState
import com.m1x.gymmer.ui.screens.state.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val logManager = gymmerApp.logManager
    private val repository = gymmerApp.repository
    
    private val _uiState = MutableStateFlow(
        LoginUiState(
            email = "trainee@gymmer.com",
            password = "password123",
            selectedRole = UserRole.TRAINEE
        )
    )
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onRoleChanged(role: UserRole) {
        _uiState.update { it.copy(selectedRole = role, error = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun login(onSuccess: (UserRole) -> Unit) {
        val currentState = _uiState.value
        
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(error = "Please fill in all fields") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = repository.login(LoginRequest(currentState.email, currentState.password))
                logManager.info(LogManager.LogCategory.LOGIN, "Login successful for ${user.email}")
                
                _uiState.update { it.copy(isLoading = false) }
                
                val finalRole = when(user.role?.uppercase()) {
                    "TRAINER" -> UserRole.TRAINER
                    "BUSINESS" -> UserRole.BUSINESS
                    "SUPER_USER" -> UserRole.SUPER_USER
                    else -> UserRole.TRAINEE
                }
                onSuccess(finalRole)
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Login failed: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = "Login failed: ${e.message}") }
            }
        }
    }
}
