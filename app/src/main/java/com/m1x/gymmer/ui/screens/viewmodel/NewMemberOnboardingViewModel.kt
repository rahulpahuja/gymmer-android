package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.network.models.RegisterRequest
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.NewMemberOnboardingUiState
import com.m1x.gymmer.ui.screens.state.OnboardingPlanState
import com.m1x.gymmer.ui.screens.state.OnboardingTrainerState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewMemberOnboardingViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    private val _uiState = MutableStateFlow(
        NewMemberOnboardingUiState(
            membershipPlans = listOf(
                OnboardingPlanState("MONTHLY", "FLEXIBLE ACCESS", "₹9,999", "/MO", false),
                OnboardingPlanState("QUARTERLY", "PHASED COMMITMENT", "₹24,999", "/QT", true, isSelected = true),
                OnboardingPlanState("YEARLY", "ELITE ENDURANCE", "₹79,999", "/YR", false)
            )
        )
    )
    val uiState: StateFlow<NewMemberOnboardingUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent: SharedFlow<String> = _navigationEvent.asSharedFlow()

    init {
        loadTrainers()
    }

    private fun loadTrainers() {
        viewModelScope.launch {
            try {
                val trainers = repository.getAllTrainers()
                _uiState.update { state ->
                    state.copy(
                        availableTrainers = trainers.map { trainer ->
                            OnboardingTrainerState(
                                name = trainer.name ?: "Unknown",
                                specialty = trainer.role ?: "TRAINER",
                                isSelected = false
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load trainers: ${e.message}")
            }
        }
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(athleteName = name) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onAgeChanged(age: String) {
        _uiState.update { it.copy(age = age) }
    }

    fun toggleGoal(goal: String) {
        _uiState.update { state ->
            val newGoals = state.selectedGoals.toMutableSet()
            if (newGoals.contains(goal)) {
                newGoals.remove(goal)
            } else {
                newGoals.add(goal)
            }
            state.copy(selectedGoals = newGoals)
        }
    }

    fun selectPlan(planTitle: String) {
        _uiState.update { state ->
            state.copy(
                membershipPlans = state.membershipPlans.map { it.copy(isSelected = it.title == planTitle) }
            )
        }
    }

    fun selectTrainer(trainerName: String) {
        _uiState.update { state ->
            state.copy(
                availableTrainers = state.availableTrainers.map { it.copy(isSelected = it.name == trainerName) }
            )
        }
    }

    fun completeOnboarding(email: String, phone: String, password: String) {
        viewModelScope.launch {
            try {
                val trimmedEmail = email.trim()
                val trimmedPassword = password.trim()
                
                _uiState.update { it.copy(email = trimmedEmail, password = trimmedPassword, isLoading = true) }
                
                val request = RegisterRequest(
                    gymId = "00000000-0000-0000-0000-000000000000", // Default Gym
                    name = uiState.value.athleteName.trim(),
                    email = trimmedEmail,
                    phone = phone.trim(),
                    password = trimmedPassword,
                    role = "TRAINEE"
                )
                
                val user = repository.register(request)
                logManager.info(LogManager.LogCategory.AUTH, "Registration successful for: ${user.name}")
                
                _uiState.update { it.copy(isLoading = false) }
                // Notify UI that registration is complete and we can navigate
                _navigationEvent.emit("dashboard")
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Onboarding failed: ${e.message}")
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
