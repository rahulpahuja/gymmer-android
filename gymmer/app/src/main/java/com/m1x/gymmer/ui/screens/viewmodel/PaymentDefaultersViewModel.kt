package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.DefaulterState
import com.m1x.gymmer.ui.screens.state.PaymentDefaultersUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PaymentDefaultersViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        PaymentDefaultersUiState(
            defaulters = listOf(
                DefaulterState("1", "Amit Sharma", "ELITE ANNUAL", "8 DAYS LATE", 12499.00),
                DefaulterState("2", "Neha Gupta", "ELITE ANNUAL", "9 DAYS LATE", 7999.00),
                DefaulterState("3", "Rahul Verma", "ELITE ANNUAL", "10 DAYS LATE", 12499.00)
            )
        )
    )
    val uiState: StateFlow<PaymentDefaultersUiState> = _uiState.asStateFlow()

    fun onTabSelected(tab: String) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}
