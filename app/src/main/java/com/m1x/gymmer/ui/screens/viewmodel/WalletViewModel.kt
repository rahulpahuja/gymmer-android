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
import java.util.UUID

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    // Mock User ID
    private val currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000000")

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        loadWalletData()
    }

    private fun loadWalletData() {
        viewModelScope.launch {
            try {
                val user = repository.getProfile(currentUserId)
                _uiState.update { state ->
                    state.copy(
                        membership = ActiveMembershipState(
                            type = user.role ?: "MEMBER",
                            plan = if (user.role == "ELITE") "ELITE ANNUAL" else "STANDARD MONTHLY",
                            memberId = "KNTC-${user.id.toString().take(4)}-X01",
                            validThru = "AUG 2025",
                            memberSince = user.createdAt?.take(10) ?: "SEP 2023"
                        ),
                        // Mock history for now as there's no transaction API in Swagger
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
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load wallet data: ${e.message}")
            }
        }
    }
}
