package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.m1x.gymmer.data.utils.AppConstants
import com.m1x.gymmer.data.utils.CurrencyManager
import com.m1x.gymmer.ui.components.*
import com.m1x.gymmer.ui.navigation.Screen
import com.m1x.gymmer.ui.screens.state.DefaulterState
import com.m1x.gymmer.ui.screens.state.PaymentDefaultersUiState
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun PaymentDefaultersScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    uiState: PaymentDefaultersUiState = PaymentDefaultersUiState(
        defaulters = listOf(
            DefaulterState("1", "Marcus Thorne", "ELITE ANNUAL", "8 DAYS LATE", 149.00),
            DefaulterState("2", "Elena Rodriguez", "ELITE ANNUAL", "9 DAYS LATE", 89.00),
            DefaulterState("3", "Jason Vane", "ELITE ANNUAL", "10 DAYS LATE", 149.00)
        )
    ),
    onTabSelected: (String) -> Unit = {}
) {
    PaymentDefaultersContent(
        uiState = uiState,
        onTabSelected = onTabSelected,
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
fun PaymentDefaultersContent(
    uiState: PaymentDefaultersUiState,
    onTabSelected: (String) -> Unit,
    onMenuClick: () -> Unit = {},
    onNavigateToScreen: (Int) -> Unit = {}
) {
    Scaffold(
        topBar = { GymTopBar(title = "DEFAULTERS", onMenuClick = onMenuClick) },
        bottomBar = { 
            GymBottomNavigation(
                selectedItem = 3,
                onItemSelected = onNavigateToScreen
            ) 
        },
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                RevenueRiskCard(amount = uiState.revenueRisk)
            }
            item {
                ComplianceCard(count = uiState.complianceCount)
            }
            item {
                DefaulterTabs(selectedTab = uiState.selectedTab, onTabSelected = onTabSelected)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Active Defaulters", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                    Text("SHOWING ${uiState.defaulters.size} ENTRIES", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }
            items(uiState.defaulters) { defaulter ->
                DefaulterItem(
                    name = defaulter.name,
                    plan = defaulter.plan,
                    daysLate = defaulter.daysLate,
                    amount = defaulter.amount,
                    imageUrl = when(defaulter.id) {
                        "1" -> AppConstants.SAMPLE_PROFILE_IMAGE_3
                        "2" -> AppConstants.SAMPLE_PROFILE_IMAGE_4
                        else -> AppConstants.SAMPLE_PROFILE_IMAGE_5
                    }
                )
            }
            if (uiState.isAutomatedRecoveryActive) {
                item {
                    AutomatedRecoveryCard()
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun RevenueRiskCard(amount: Double) {
    GymCard {
        Text("REVENUE RISK", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            CurrencyManager.formatAmount(amount),
            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
            color = Color.White
        )
        Text("Total Outstanding Balance", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("+12% VS LAST MONTH", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color.Red)
        }
    }
}

@Composable
fun ComplianceCard(count: Int) {
    GymCard(
        containerColor = Color.Transparent,
        modifier = Modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(LimeGreen.copy(alpha = 0.8f), LimeGreen.copy(alpha = 0.2f))
            ),
            shape = RoundedCornerShape(16.dp)
        )
    ) {
        Text("COMPLIANCE", style = MaterialTheme.typography.labelSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            count.toString(),
            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
            color = Color.Black
        )
        Text("Active Defaulters", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = { 0.4f },
            modifier = Modifier.fillMaxWidth().height(4.dp),
            color = Color.Black,
            trackColor = Color.Black.copy(alpha = 0.1f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("ACTION REQUIRED IMMEDIATELY", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color.Black)
    }
}

@Composable
fun DefaulterTabs(selectedTab: String, onTabSelected: (String) -> Unit) {
    val tabs = listOf("CRITICAL (>7 DAYS)", "OVERDUE", "UPCOMING")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab
            Surface(
                color = if (isSelected) LimeGreen.copy(alpha = 0.1f) else Color.DarkGray,
                shape = RoundedCornerShape(20.dp),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, LimeGreen) else null,
                onClick = { onTabSelected(tab) }
            ) {
                Text(tab, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.labelSmall, color = if (isSelected) LimeGreen else Color.Gray)
            }
        }
    }
}

@Composable
fun DefaulterItem(name: String, plan: String, daysLate: String, amount: Double, imageUrl: String) {
    GymCard(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Row {
                    Text(plan, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(daysLate, style = MaterialTheme.typography.labelSmall, color = Color.Red)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("LAST PAID", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(CurrencyManager.formatAmount(amount), style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GymButton(text = "SEND REMINDER", onClick = {}, modifier = Modifier.weight(1f).height(44.dp))
            GymSecondaryButton(text = "MANAGE ACCESS", onClick = {}, modifier = Modifier.weight(1f).height(44.dp))
        }
    }
}

@Composable
fun AutomatedRecoveryCard() {
    GymCard(containerColor = Color(0xFF1A1A2E)) {
        Text("AUTOMATED RECOVERY IS ACTIVE", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Next batch of automated reminders will be triggered in 4 hours for all members marked as 'Critical'.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("CONFIGURE SETTINGS ->", style = MaterialTheme.typography.labelSmall, color = Color.White)
    }
}

@Preview
@Composable
fun PaymentDefaultersScreenPreview() {
    GymmerTheme {
        PaymentDefaultersContent(uiState = PaymentDefaultersUiState(), onTabSelected = {})
    }
}
