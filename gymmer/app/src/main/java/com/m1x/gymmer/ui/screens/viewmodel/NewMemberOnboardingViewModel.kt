package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.NewMemberOnboardingUiState
import com.m1x.gymmer.ui.screens.state.OnboardingPlanState
import com.m1x.gymmer.ui.screens.state.OnboardingTrainerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewMemberOnboardingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        NewMemberOnboardingUiState(
            membershipPlans = listOf(
                OnboardingPlanState("MONTHLY", "FLEXIBLE ACCESS", "₹9,999", "/MO", false),
                OnboardingPlanState("QUARTERLY", "PHASED COMMITMENT", "₹24,999", "/QT", true, isSelected = true),
                OnboardingPlanState("YEARLY", "ELITE ENDURANCE", "₹79,999", "/YR", false)
            ),
            availableTrainers = listOf(
                OnboardingTrainerState("Arjun Sharma", "STRENGTH SPECIALIST", true),
                OnboardingTrainerState("Priya Patel", "METABOLIC CONDITIONING", false),
                OnboardingTrainerState("Rohan Malhotra", "POWER & OPTIMIZATION", false)
            )
        )
    )
    val uiState: StateFlow<NewMemberOnboardingUiState> = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(athleteName = name) }
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
}
