package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.LoginUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val logManager = LogManager.getInstance(application, gymmerApp.coroutineManager)
    
    private val _uiState = MutableStateFlow(
        LoginUiState(
            email = "admin@gymmer.com",
            password = "password123"
        )
    )
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        logManager.info(LogManager.LogCategory.LOGIN, "Email changed: $email")
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun login(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        logManager.info(LogManager.LogCategory.LOGIN, "Login attempt for: ${currentState.email}")
        
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            logManager.info(LogManager.LogCategory.LOGIN, "Login failed: Missing fields")
            _uiState.update { it.copy(error = "Please fill in all fields") }
            return
        }

        // Validate credentials
        if (currentState.email != "admin@gymmer.com" || currentState.password != "password123") {
            logManager.info(LogManager.LogCategory.LOGIN, "Login failed: Invalid credentials")
            _uiState.update { it.copy(error = "Invalid email or password") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // Simulate network call
            delay(1500)
            _uiState.update { it.copy(isLoading = false) }
            logManager.info(LogManager.LogCategory.LOGIN, "Login successful for: ${currentState.email}")
            onSuccess()
        }
    }
}
