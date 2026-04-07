package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.m1x.gymmer.ui.components.*
import com.m1x.gymmer.ui.navigation.Screen
import com.m1x.gymmer.ui.screens.state.*
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun BusinessInsightsScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    uiState: BusinessInsightsUiState = BusinessInsightsUiState(
        gymPulse = listOf(
            PulseState("Downtown Powerhouse", "84% CAPACITY"),
            PulseState("East Side HIIT Lab", "42% CAPACITY"),
            PulseState("The Foundry (North)", "76% CAPACITY (PEAK)")
        ),
        managementTasks = listOf(
            ManagementTask("Membership Plans", "Adjust pricing tiers, seasonal discounts, and contract terms."),
            ManagementTask("Gym House Rules", "Update safety protocols, guest policies, and behavior guidelines."),
            ManagementTask("Access Control", "Manage RFID permissions, staff overrides, and emergency locks.")
        )
    )
) {
    BusinessInsightsContent(
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
fun BusinessInsightsContent(
    uiState: BusinessInsightsUiState,
    onMenuClick: () -> Unit = {},
    onNavigateToScreen: (Int) -> Unit = {}
) {
    Scaffold(
        topBar = { GymTopBar(title = "GYMOPS", onMenuClick = onMenuClick) },
        bottomBar = { 
            GymBottomNavigation(
                selectedItem = 0,
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
                AnnualRevenueCard(revenueState = uiState.annualRevenue)
            }
            item {
                RetentionEngineCard(retentionState = uiState.retention)
            }
            item {
                MemberGrowthCard(growthState = uiState.growth)
            }
            item {
                GymPulseSection(pulseList = uiState.gymPulse)
            }
            item {
                SystemManagementSection(tasks = uiState.managementTasks)
            }
            item {
                RevenueKineticsSection(kineticsState = uiState.revenueKinetics)
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AnnualRevenueCard(revenueState: RevenueState) {
    GymCard {
        Text("ANNUAL REVENUE RUN-RATE", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            revenueState.amount,
            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(revenueState.trend, style = MaterialTheme.typography.titleMedium, color = LimeGreen)
                Text("VS LAST MONTH", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(revenueState.profitMargin, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text("PROFIT MARGIN", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun RetentionEngineCard(retentionState: RetentionState) {
    GymCard(containerColor = LimeGreen.copy(alpha = 0.9f)) {
        Text("RETENTION ENGINE", style = MaterialTheme.typography.labelSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            retentionState.percentage,
            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            retentionState.description,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        GymButton(
            text = "VIEW LOYALTY BREAKDOWN",
            onClick = {},
            containerColor = Color.Black.copy(alpha = 0.8f),
            contentColor = LimeGreen,
            modifier = Modifier.height(44.dp)
        )
    }
}

@Composable
fun MemberGrowthCard(growthState: GrowthState) {
    GymCard {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("MEMBER GROWTH", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text("NET GAIN THIS QUARTER", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Icon(Icons.Default.Info, contentDescription = null, tint = LimeGreen)
        }
        Spacer(modifier = Modifier.height(24.dp))
        // Bar Chart Placeholder
        Row(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            repeat(10) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(if (index == 9) 100.dp else (40..80).random().dp)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(if (index == 9) LimeGreen else Color.DarkGray)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(growthState.activeTotal, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text("ACTIVE TOTAL", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(growthState.quarterlyGain, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = LimeGreen)
                Text("NEW SIGNUPS", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun GymPulseSection(pulseList: List<PulseState>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("GYM PULSE", style = MaterialTheme.typography.titleLarge, color = Color.White)
            Surface(color = Color.DarkGray, shape = RoundedCornerShape(12.dp)) {
                Text("LIVE ACTIVITY", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = LimeGreen)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        pulseList.forEach { pulse ->
            PulseItem(pulse.name, pulse.capacity)
        }
    }
}

@Composable
fun PulseItem(name: String, capacity: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).background(LimeGreen, CircleShape))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.bodyLarge, color = Color.White)
                Text(capacity, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun SystemManagementSection(tasks: List<ManagementTask>) {
    Column {
        Text("SYSTEM MANAGEMENT", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black), color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        tasks.forEach { task ->
            ManagementCard(task.title, task.description)
        }
    }
}

@Composable
fun ManagementCard(title: String, description: String) {
    GymCard(modifier = Modifier.padding(bottom = 12.dp)) {
        Box(modifier = Modifier.size(32.dp).background(LimeGreen, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Settings, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = Color.White)
        Text(description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("EDIT CONFIGURATION >", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = LimeGreen)
    }
}

@Composable
fun RevenueKineticsSection(kineticsState: RevenueKineticsState) {
    Column {
        Text("REVENUE KINETICS", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black), color = Color.White)
        Text("PERFORMANCE TRACKING: OCT 2023 - PRESENT", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            kineticsState.periods.forEach { period ->
                val isSelected = period == kineticsState.selectedPeriod
                Surface(
                    color = if (isSelected) LimeGreen else Color.DarkGray,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        period, 
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), 
                        style = MaterialTheme.typography.labelSmall, 
                        color = if (isSelected) Color.Black else Color.Gray
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        // Modern Bar Chart
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.DarkGray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            kineticsState.chartData.forEach { value ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(value)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (value > 0.8f) LimeGreen else Color.Gray)
                )
            }
        }
    }
}

@Preview
@Composable
fun BusinessInsightsScreenPreview() {
    GymmerTheme {
        BusinessInsightsContent(uiState = BusinessInsightsUiState())
    }
}
