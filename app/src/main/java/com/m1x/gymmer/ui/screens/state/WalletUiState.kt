package com.m1x.gymmer.ui.screens.state

data class WalletUiState(
    val urgentAction: UrgentActionState? = UrgentActionState("Oct 15th"),
    val membership: ActiveMembershipState = ActiveMembershipState(),
    val paymentHistory: List<TransactionState> = emptyList(),
    val remindersHistory: List<ReminderState> = emptyList()
)

data class UrgentActionState(
    val nextPaymentDate: String
)

data class ActiveMembershipState(
    val type: String = "V.I.P",
    val plan: String = "ELITE ANNUAL",
    val memberId: String = "KNTC - 9982 - X01",
    val validThru: String = "AUG 2025",
    val memberSince: String = "SEP 2023"
)

data class TransactionState(
    val title: String,
    val dateAndMethod: String,
    val amount: Double,
    val status: String,
    val isCompleted: Boolean = false
)

data class ReminderState(
    val title: String,
    val subtitle: String
)
