package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.m1x.gymmer.data.utils.AppConstants
import com.m1x.gymmer.ui.components.*
import com.m1x.gymmer.ui.navigation.Screen
import com.m1x.gymmer.ui.screens.state.HistoryItemState
import com.m1x.gymmer.ui.screens.state.MetricState
import com.m1x.gymmer.ui.screens.state.ProfileUiState
import com.m1x.gymmer.ui.screens.viewmodel.ProfileViewModel
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun ProfileScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    ProfileContent(
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
fun ProfileContent(
    uiState: ProfileUiState,
    onMenuClick: () -> Unit = {},
    onNavigateToScreen: (Int) -> Unit = {}
) {
    val scrollState = rememberLazyListState()
    
    Scaffold(
        topBar = { GymTopBar(title = "KINETIC", onMenuClick = onMenuClick) },
        bottomBar = { 
            GymBottomNavigation(
                selectedItem = 4,
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
            item {
                ProfileHeader(
                    name = uiState.name,
                    status = uiState.status,
                    focus = uiState.focus
                )
            }
            item {
                AssignedTrainerCard(
                    name = uiState.assignedTrainer.name,
                    specialty = uiState.assignedTrainer.specialty
                )
            }
            item {
                GoalMetricsSection(metrics = uiState.goalMetrics)
            }
            item {
                KineticHistorySection(history = uiState.kineticHistory)
            }
            item {
                MembershipStatusCard(
                    status = uiState.membershipStatus.status,
                    until = uiState.membershipStatus.until,
                    description = uiState.membershipStatus.description
                )
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfileHeader(name: String, status: String, focus: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = AppConstants.SAMPLE_PROFILE_IMAGE_1,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Surface(color = Color.DarkGray, shape = RoundedCornerShape(12.dp)) {
            Text(
                status,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = LimeGreen
            )
        }
        Text(
            name,
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black),
            color = Color.White
        )
        Text(
            focus,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun AssignedTrainerCard(name: String, specialty: String) {
    GymCard {
        Text("ASSIGNED_TRAINER", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = AppConstants.SAMPLE_PROFILE_IMAGE_5,
                contentDescription = "Trainer Image",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(specialty, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        GymSecondaryButton(text = "CONTACT_COACH", onClick = {}, modifier = Modifier.height(44.dp))
    }
}

@Composable
fun GoalMetricsSection(metrics: List<MetricState>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("GOAL_METRICS", style = MaterialTheme.typography.titleMedium, color = Color.White)
            Icon(Icons.Default.Info, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        metrics.forEach { metric ->
            MetricBar(metric.label, metric.progress, metric.value)
        }
    }
}

@Composable
fun MetricBar(label: String, progress: Float, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White)
            Text(value, style = MaterialTheme.typography.labelSmall, color = LimeGreen)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(4.dp),
            color = LimeGreen,
            trackColor = Color.DarkGray
        )
    }
}

@Composable
fun KineticHistorySection(history: List<HistoryItemState>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("KINETIC_HISTORY", style = MaterialTheme.typography.titleMedium, color = Color.White)
            Text("VIEW_ALL", style = MaterialTheme.typography.labelSmall, color = LimeGreen)
        }
        Spacer(modifier = Modifier.height(16.dp))
        history.forEach { item ->
            HistoryItem(item.title, item.value, item.subtitle)
        }
    }
}

@Composable
fun HistoryItem(title: String, value: String, subtitle: String) {
    GymCard(modifier = Modifier.padding(bottom = 8.dp), padding = PaddingValues(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color.DarkGray, RoundedCornerShape(8.dp)))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text(subtitle, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Text(value, style = MaterialTheme.typography.titleMedium, color = LimeGreen)
        }
    }
}

@Composable
fun MembershipStatusCard(status: String, until: String, description: String) {
    GymCard {
        Text("MEMBERSHIP_STATUS", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text(status, style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black), color = Color.White)
        Text(until, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            description,
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray
        )
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    GymmerTheme {
        ProfileContent(uiState = ProfileUiState())
    }
}
