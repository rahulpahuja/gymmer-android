package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BusinessInsightsViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    private val _uiState = MutableStateFlow(BusinessInsightsUiState())
    val uiState: StateFlow<BusinessInsightsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val kinetics = repository.getRevenueKinetics()
                val pulse = repository.getPulse()
                val insights = repository.getBusinessInsights()

                _uiState.update { state ->
                    state.copy(
                        annualRevenue = state.annualRevenue.copy(
                            amount = "₹${insights.annualRevenue ?: 0.0}"
                        ),
                        retention = state.retention.copy(
                            percentage = "${insights.retentionRate ?: 0.0}%"
                        ),
                        growth = state.growth.copy(
                            activeTotal = "${insights.memberGrowth ?: 0}"
                        ),
                        revenueKinetics = state.revenueKinetics.copy(
                            chartData = kinetics.map { it.revenue?.toFloat() ?: 0f }
                        ),
                        gymPulse = pulse.map { 
                            PulseState(it.gymName ?: "Unknown Gym", "${it.currentCapacity}% CAPACITY")
                        }
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load business insights: ${e.message}")
            }
        }
    }

    fun onPeriodSelected(period: String) {
        _uiState.update { it.copy(revenueKinetics = it.revenueKinetics.copy(selectedPeriod = period)) }
    }
}
