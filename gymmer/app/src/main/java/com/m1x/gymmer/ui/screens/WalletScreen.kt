package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m1x.gymmer.ui.components.*
import com.m1x.gymmer.ui.navigation.Screen
import com.m1x.gymmer.ui.screens.state.ReminderState
import com.m1x.gymmer.ui.screens.state.TransactionState
import com.m1x.gymmer.ui.screens.state.WalletUiState
import com.m1x.gymmer.ui.screens.viewmodel.WalletViewModel
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun WalletScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    viewModel: WalletViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    WalletContent(
        uiState = uiState,
        onMenuClick = onMenuClick,
        onNavigateToScreen = { index ->
            val route = when (index) {
                0 -> Screen.Dashboard.route
                1 -> Screen.Workouts.route
                2 -> Screen.Scan.route
                3 -> Screen.Wallet.route
                4 -> Screen.Profile.route
                else -> Screen.Dashboard.route
            }
            navController.navigate(route) {
                popUpTo(Screen.Dashboard.route) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

@Composable
fun WalletContent(
    uiState: WalletUiState,
    onMenuClick: () -> Unit = {},
    onNavigateToScreen: (Int) -> Unit = {}
) {
    val scrollState = rememberLazyListState()
    
    Scaffold(
        topBar = { GymTopBar(title = "KINETIC", onMenuClick = onMenuClick) },
        bottomBar = { 
            GymBottomNavigation(
                selectedItem = 3,
                onItemSelected = onNavigateToScreen
            ) 
        },
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            uiState.urgentAction?.let { action ->
                item {
                    UrgentActionCard(nextPaymentDate = action.nextPaymentDate)
                }
            }
            item {
                ActiveMembershipCard(
                    type = uiState.membership.type,
                    plan = uiState.membership.plan,
                    memberId = uiState.membership.memberId,
                    validThru = uiState.membership.validThru,
                    memberSince = uiState.membership.memberSince
                )
            }
            item {
                PaymentHistorySection(transactions = uiState.paymentHistory)
            }
            item {
                RemindersHistorySection(reminders = uiState.remindersHistory)
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun UrgentActionCard(nextPaymentDate: String) {
    GymCard(
        containerColor = Color.Transparent,
        modifier = Modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(LimeGreen.copy(alpha = 0.8f), LimeGreen.copy(alpha = 0.4f))
            ),
            shape = RoundedCornerShape(16.dp)
        )
    ) {
        Text("URGENT ACTION", style = MaterialTheme.typography.labelSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Next Payment: $nextPaymentDate",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        GymButton(
            text = "Pay Now",
            onClick = {},
            containerColor = Color.Black,
            contentColor = LimeGreen,
            modifier = Modifier.height(48.dp)
        )
    }
}

@Composable
fun ActiveMembershipCard(
    type: String,
    plan: String,
    memberId: String,
    validThru: String,
    memberSince: String
) {
    GymCard {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("ACTIVE MEMBERSHIP", style = MaterialTheme.typography.labelSmall, color = LimeGreen)
            Surface(color = Color.DarkGray, shape = RoundedCornerShape(4.dp)) {
                Text(type, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = LimeGreen)
            }
        }
        Text(plan, style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black), color = Color.White)
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Barcode placeholder
                Box(modifier = Modifier.width(150.dp).height(40.dp).background(Color.Black))
                Spacer(modifier = Modifier.height(12.dp))
                Text(memberId, style = MaterialTheme.typography.labelLarge, color = Color.Black, letterSpacing = 4.sp)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text("VALID THRU", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(validThru, style = MaterialTheme.typography.titleLarge, color = Color.White)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("MEMBER SINCE", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(memberSince, style = MaterialTheme.typography.titleLarge, color = Color.White)
            }
        }
    }
}

@Composable
fun PaymentHistorySection(transactions: List<TransactionState>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(4.dp, 16.dp).background(LimeGreen))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Payment History", style = MaterialTheme.typography.titleLarge, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        transactions.forEach { transaction ->
            TransactionItem(
                title = transaction.title,
                subtitle = transaction.dateAndMethod,
                amount = transaction.amount.toString(),
                status = transaction.status,
                isCompleted = transaction.isCompleted
            )
        }
    }
}

@Composable
fun TransactionItem(title: String, subtitle: String, amount: String, status: String, isCompleted: Boolean = false) {
    GymCard(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color.DarkGray, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text(subtitle, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(amount, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Surface(color = if (isCompleted) Color.Gray.copy(alpha = 0.2f) else LimeGreen.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                    Text(status, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = if (isCompleted) Color.Gray else LimeGreen)
                }
            }
        }
    }
}

@Composable
fun RemindersHistorySection(reminders: List<ReminderState>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(4.dp, 16.dp).background(Color.Gray))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reminders History", style = MaterialTheme.typography.titleLarge, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        reminders.forEach { reminder ->
            ReminderItem(title = reminder.title, subtitle = reminder.subtitle)
        }
    }
}

@Composable
fun ReminderItem(title: String, subtitle: String) {
    Row(modifier = Modifier.padding(vertical = 12.dp), verticalAlignment = Alignment.Top) {
        Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Preview
@Composable
fun WalletScreenPreview() {
    GymmerTheme {
        WalletContent(uiState = WalletUiState())
    }
}
