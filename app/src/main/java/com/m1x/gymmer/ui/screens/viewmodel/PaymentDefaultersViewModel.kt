package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.DefaulterState
import com.m1x.gymmer.ui.screens.state.PaymentDefaultersUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentDefaultersViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    private val _uiState = MutableStateFlow(PaymentDefaultersUiState())
    val uiState: StateFlow<PaymentDefaultersUiState> = _uiState.asStateFlow()

    init {
        loadDefaulters()
    }

    private fun loadDefaulters() {
        viewModelScope.launch {
            try {
                val defaulters = repository.getDefaulters()
                _uiState.update { state ->
                    state.copy(
                        defaulters = defaulters.map { apiDefaulter ->
                            DefaulterState(
                                id = apiDefaulter.userId.toString(),
                                name = apiDefaulter.name ?: "Unknown",
                                plan = "ELITE ANNUAL", // Mocking plan as API doesn't provide it
                                daysLate = "Pending", // Mocking as API doesn't provide it
                                amount = apiDefaulter.amountDue ?: 0.0
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load defaulters: ${e.message}")
            }
        }
    }

    fun onTabSelected(tab: String) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}
