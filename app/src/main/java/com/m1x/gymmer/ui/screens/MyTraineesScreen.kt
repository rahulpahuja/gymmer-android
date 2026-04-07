package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.m1x.gymmer.data.utils.AppConstants
import com.m1x.gymmer.ui.components.*
import com.m1x.gymmer.ui.navigation.Screen
import com.m1x.gymmer.ui.screens.state.MyTraineesUiState
import com.m1x.gymmer.ui.screens.viewmodel.MyTraineesViewModel
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun MyTraineesScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    viewModel: MyTraineesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    MyTraineesContent(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onMenuClick = onMenuClick,
        onViewProgress = { traineeName ->
            navController.navigate(Screen.AttendanceLogs.route)
        },
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
fun MyTraineesContent(
    uiState: MyTraineesUiState,
    onSearchQueryChange: (String) -> Unit,
    onMenuClick: () -> Unit = {},
    onViewProgress: (String) -> Unit = {},
    onNavigateToScreen: (Int) -> Unit = {}
) {
    val scrollState = rememberLazyListState()
    
    Scaffold(
        topBar = { GymTopBar(title = "My Trainees", onMenuClick = onMenuClick) },
        bottomBar = { 
            GymBottomNavigation(
                selectedItem = 0,
                onItemSelected = onNavigateToScreen
            ) 
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = LimeGreen,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Trainee")
            }
        },
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GymTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChange,
                    label = "",
                    placeholder = "Search trainees by name or goal...",
                    leadingIcon = Icons.Default.Search
                )
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color.Red.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
                        Text("URGENT", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Color.Red)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Needs\nAttention", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black, lineHeight = 32.sp), color = Color.White)
                        Text("${uiState.needsAttention.size} Trainees Inactive", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                }
            }
            items(uiState.needsAttention) { trainee ->
                NeedsAttentionCard(name = trainee.name, reason = trainee.reason)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Active Rosters", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black), color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Recent", style = MaterialTheme.typography.labelSmall, color = Color.White)
                        Text("A-Z", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text("Progress", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                }
            }
            items(uiState.activeRoster) { trainee ->
                ActiveRosterCard(
                    name = trainee.name,
                    plan = trainee.plan,
                    adherence = trainee.adherence,
                    lastActivity = trainee.lastActivity,
                    onViewProgress = { onViewProgress(trainee.name) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun NeedsAttentionCard(name: String, reason: String) {
    GymCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = AppConstants.SAMPLE_PROFILE_IMAGE_2,
                contentDescription = "Trainee Image",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text(reason, style = MaterialTheme.typography.labelSmall, color = Color.Red)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GymSecondaryButton(text = "POKE", onClick = {}, modifier = Modifier.weight(1f).height(44.dp))
            GymSecondaryButton(
                text = "MESSAGE",
                onClick = {},
                modifier = Modifier.weight(1f).height(44.dp)
            )
        }
    }
}

@Composable
fun ActiveRosterCard(name: String, plan: String, adherence: String, lastActivity: String, onViewProgress: () -> Unit = {}) {
    GymCard {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = AppConstants.SAMPLE_PROFILE_IMAGE_3,
                contentDescription = "Trainee Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = Color.White)
            Text(plan, style = MaterialTheme.typography.labelSmall, color = LimeGreen)
            Spacer(modifier = Modifier.height(8.dp))
            Text("LAST ACTIVITY", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(lastActivity, style = MaterialTheme.typography.bodySmall, color = Color.White)
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("WORKOUT ADHERENCE", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(adherence, style = MaterialTheme.typography.labelSmall, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(10) { index ->
                    val progressValue = adherence.removeSuffix("%").toIntOrNull() ?: 0
                    val filledThreshold = (progressValue / 10)
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .weight(1f)
                            .clip(CircleShape)
                            .background(if (index < filledThreshold) LimeGreen else Color.DarkGray)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                GymButton(text = "VIEW PROGRESS", onClick = onViewProgress, modifier = Modifier.weight(1f).height(48.dp))
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(48.dp).background(Color.DarkGray, RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@Preview
@Composable
fun MyTraineesScreenPreview() {
    GymmerTheme {
        MyTraineesContent(uiState = MyTraineesUiState(), onSearchQueryChange = {})
    }
}
