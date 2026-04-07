package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m1x.gymmer.R
import com.m1x.gymmer.ui.components.*
import com.m1x.gymmer.ui.navigation.Screen
import com.m1x.gymmer.ui.screens.state.ActiveSession
import com.m1x.gymmer.ui.screens.state.DashboardUiState
import com.m1x.gymmer.ui.screens.viewmodel.DashboardViewModel
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun DashboardScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    DashboardContent(
        uiState = uiState,
        onCheckInClick = viewModel::onCheckInClicked,
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
        },
        onExerciseClick = { category ->
            navController.navigate(Screen.ExerciseList.createRoute(category))
        },
        onViewAllSessionsClick = {
            navController.navigate(Screen.Workouts.route)
        }
    )
}

@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onCheckInClick: () -> Unit,
    onMenuClick: () -> Unit = {},
    onNavigateToScreen: (Int) -> Unit = {},
    onExerciseClick: (String) -> Unit = {},
    onViewAllSessionsClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { GymTopBar(title = stringResource(R.string.dashboard_title), onMenuClick = onMenuClick) },
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
                CheckInCard(
                    status = if (uiState.checkInRequired) stringResource(R.string.check_in_required) else stringResource(R.string.checked_in),
                    lastActivity = uiState.lastActivity,
                    onCheckInClick = onCheckInClick
                )
            }
            item {
                PremiumPlanCard(
                    name = uiState.premiumPlan.name,
                    expiresDays = uiState.premiumPlan.expiresDays,
                    progress = uiState.premiumPlan.progress,
                    isPro = uiState.premiumPlan.isPro
                )
            }
            item {
                TodaysTargetCard(
                    title = uiState.todaysTarget.title,
                    exercises = uiState.todaysTarget.exercises,
                    onClick = { onExerciseClick(uiState.todaysTarget.title) }
                )
            }
            item {
                ActiveSessionsSection(
                    sessions = uiState.activeSessions,
                    onSessionClick = { onExerciseClick(it) },
                    onViewAllClick = onViewAllSessionsClick
                )
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun CheckInCard(status: String, lastActivity: String, onCheckInClick: () -> Unit) {
    GymCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.status_report),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Black,
                        lineHeight = 32.sp
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(if (status == stringResource(R.string.check_in_required)) Color.Red else LimeGreen, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.last_activity, lastActivity),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            IconButton(
                onClick = onCheckInClick,
                modifier = Modifier
                    .size(56.dp)
                    .background(LimeGreen, CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.Black)
            }
        }
    }
}

@Composable
fun PremiumPlanCard(name: String, expiresDays: Int, progress: Float, isPro: Boolean) {
    GymCard {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(name, style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
            if (isPro) {
                Surface(
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        stringResource(R.string.pro_badge),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = LimeGreen
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.expires_in_days, expiresDays), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = LimeGreen,
            trackColor = Color.DarkGray
        )
    }
}

@Composable
fun TodaysTargetCard(title: String, exercises: List<String>, onClick: () -> Unit = {}) {
    GymCard(padding = PaddingValues(0.dp), modifier = Modifier.clickable(onClick = onClick)) {
        Box(modifier = Modifier.height(200.dp)) {
            // Placeholder for background image
            Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray))
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(stringResource(R.string.todays_target), style = MaterialTheme.typography.labelSmall, color = LimeGreen)
                Text(
                    title,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                exercises.forEach { ExerciseListItem(it) }
            }
        }
    }
}

@Composable
fun ExerciseListItem(name: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(2.dp, 12.dp).background(LimeGreen))
        Spacer(modifier = Modifier.width(8.dp))
        Text(name, style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
    }
}

@Composable
fun ActiveSessionsSection(
    sessions: List<ActiveSession>,
    onSessionClick: (String) -> Unit = {},
    onViewAllClick: () -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.active_sessions), style = MaterialTheme.typography.titleLarge, color = Color.White)
            Text(
                text = stringResource(R.string.view_all),
                style = MaterialTheme.typography.labelMedium,
                color = LimeGreen,
                modifier = Modifier.clickable(onClick = onViewAllClick)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        sessions.forEach { session ->
            GymCard(modifier = Modifier.padding(bottom = 12.dp).clickable { onSessionClick(session.name) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).background(Color.Gray, RoundedCornerShape(8.dp)))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(session.name, style = MaterialTheme.typography.bodyLarge, color = Color.White)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(session.duration, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(session.calories, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                    }
                    IconButton(onClick = { onSessionClick(session.name) }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    GymmerTheme {
        DashboardContent(
            uiState = DashboardUiState(),
            onCheckInClick = {},
            onMenuClick = {},
            onNavigateToScreen = {},
            onExerciseClick = {}
        )
    }
}
