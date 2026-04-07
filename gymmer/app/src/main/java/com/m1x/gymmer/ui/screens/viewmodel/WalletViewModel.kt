package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.ReminderState
import com.m1x.gymmer.ui.screens.state.TransactionState
import com.m1x.gymmer.ui.screens.state.WalletUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WalletViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        WalletUiState(
            paymentHistory = listOf(
                TransactionState("Membership Renewal", "Sep 15, 2024 • UPI - HDFC Bank", 9999.00, "PAID"),
                TransactionState("Protein Shop", "Sep 08, 2024 • In-App Wallet", 3500.50, "PAID"),
                TransactionState("Personal Training", "Aug 15, 2024 • GPay", 5500.00, "COMPLETED", isCompleted = true)
            ),
            remindersHistory = listOf(
                ReminderState("Monthly Invoice ready", "Sent Sep 14, 2024 via Email & App"),
                ReminderState("Payment method update", "Sent Aug 28, 2024 • Resolved"),
                ReminderState("Welcome to Kinetic Elite", "Sent Sep 01, 2023 • Onboarding")
            )
        )
    )
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()
}
