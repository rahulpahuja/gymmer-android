package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m1x.gymmer.ui.components.*
import com.m1x.gymmer.ui.navigation.Screen
import com.m1x.gymmer.ui.screens.state.AttendanceActivity
import com.m1x.gymmer.ui.screens.state.AttendanceLogsUiState
import com.m1x.gymmer.ui.screens.viewmodel.AttendanceLogsViewModel
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun AttendanceLogsScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    viewModel: AttendanceLogsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    AttendanceLogsContent(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onTabSelected = viewModel::onTabSelected,
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
fun AttendanceLogsContent(
    uiState: AttendanceLogsUiState,
    onSearchQueryChange: (String) -> Unit,
    onTabSelected: (Int) -> Unit,
    onMenuClick: () -> Unit = {},
    onNavigateToScreen: (Int) -> Unit = {}
) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.Black)) {
                GymTopBar(title = "Attendance Logs", onMenuClick = onMenuClick)
                GymTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChange,
                    label = "",
                    placeholder = "Search member, ID, or phone...",
                    leadingIcon = Icons.Default.Search,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                TabRow(
                    selectedTabIndex = uiState.selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = LimeGreen,
                    indicator = {},
                    divider = {},
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    val tabs = listOf("Today", "Week", "Month")
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = uiState.selectedTab == index,
                            onClick = { onTabSelected(index) },
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (uiState.selectedTab == index) LimeGreen else Color.DarkGray.copy(alpha = 0.3f))
                                .height(40.dp)
                        ) {
                            Text(
                                text = title,
                                color = if (uiState.selectedTab == index) Color.Black else Color.White,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        },
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
                LiveNowCard(count = uiState.liveNowCount)
            }
            item {
                PeakTimeCard(time = uiState.peakTime)
            }
            item {
                TotalCheckInsCard(count = uiState.totalCheckIns, trend = uiState.checkInTrend)
            }
            item {
                RecentActivitySection(activities = uiState.recentActivity)
            }
            item {
                GymSecondaryButton(text = "LOAD HISTORY", onClick = {}, modifier = Modifier.padding(vertical = 16.dp))
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun LiveNowCard(count: Int) {
    GymCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).background(Color.White, CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text("LIVE NOW", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
            color = Color.White
        )
        Text("Members", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Text("82% of peak capacity reached", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
fun PeakTimeCard(time: String) {
    GymCard {
        Text("PEAK TIME TODAY", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                time,
                style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
                color = Color.White
            )
            Text("PM", style = MaterialTheme.typography.labelLarge, color = Color.White, modifier = Modifier.padding(bottom = 8.dp, start = 4.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(6) { index ->
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .weight(1f)
                        .clip(CircleShape)
                        .background(if (index < 4) LimeGreen else Color.DarkGray)
                )
            }
        }
    }
}

@Composable
fun TotalCheckInsCard(count: Int, trend: String) {
    GymCard(containerColor = LimeGreen) {
        Text("TOTAL CHECK-INS", style = MaterialTheme.typography.labelSmall, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            count.toString(),
            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black, color = Color.Black)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(trend, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color.Black)
        }
    }
}

@Composable
fun RecentActivitySection(activities: List<AttendanceActivity>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Recent Activity", style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Text("View All >", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        activities.forEach { activity ->
            ActivityItem(
                name = activity.name,
                id = activity.plan,
                time = activity.time,
                status = activity.status,
                isUrgent = activity.isUrgent
            )
        }
    }
}

@Composable
fun ActivityItem(name: String, id: String, time: String, status: String, isUrgent: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(Color.DarkGray, CircleShape))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text(id, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(time, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text("CHECK-IN", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Surface(
                color = if (isUrgent) Color.Red.copy(alpha = 0.2f) else Color.DarkGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    status,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isUrgent) Color.Red else LimeGreen
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f))
    }
}

@Preview
@Composable
fun AttendanceLogsScreenPreview() {
    GymmerTheme {
        AttendanceLogsContent(
            uiState = AttendanceLogsUiState(),
            onSearchQueryChange = {},
            onTabSelected = {},
            onMenuClick = {},
            onNavigateToScreen = {}
        )
    }
}
