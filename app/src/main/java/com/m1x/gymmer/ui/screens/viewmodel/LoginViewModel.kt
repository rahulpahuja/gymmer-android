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
        val defaultEmail = if (role == UserRole.TRAINEE) "trainee@gymmer.com" else "trainer@gymmer.com"
        val defaultPassword = if (role == UserRole.TRAINEE) "password123" else "trainer456"
        _uiState.update { it.copy(selectedRole = role, email = defaultEmail, password = defaultPassword, error = null) }
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

        // Validate credentials based on role
        val isValid = if (currentState.selectedRole == UserRole.TRAINEE) {
            currentState.email == "trainee@gymmer.com" && currentState.password == "password123"
        } else {
            currentState.email == "trainer@gymmer.com" && currentState.password == "trainer456"
        }

        if (!isValid) {
            _uiState.update { it.copy(error = "Invalid ${currentState.selectedRole.name.lowercase()} credentials") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = repository.login(LoginRequest(currentState.email, currentState.password))
                logManager.info(LogManager.LogCategory.LOGIN, "Login successful for ${user.email}")
                _uiState.update { it.copy(isLoading = false) }
                onSuccess(currentState.selectedRole)
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Login failed: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = "Login failed: ${e.message}") }
            }
        }
    }
}
