package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
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
import com.m1x.gymmer.ui.screens.state.ExerciseDetailUiState
import com.m1x.gymmer.ui.screens.viewmodel.ExerciseDetailViewModel
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun ExerciseDetailScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    viewModel: ExerciseDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    ExerciseDetailContent(
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
fun ExerciseDetailContent(
    uiState: ExerciseDetailUiState,
    onMenuClick: () -> Unit = {},
    onNavigateToScreen: (Int) -> Unit = {}
) {
    val scrollState = rememberLazyListState()
    
    Scaffold(
        topBar = { GymTopBar(title = "KINETIC", onMenuClick = onMenuClick) },
        bottomBar = { 
            GymBottomNavigation(
                selectedItem = 2,
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
                Column {
                    Surface(color = LimeGreen.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
                        Text(uiState.status, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = LimeGreen)
                    }
                    Text(uiState.category, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(
                        uiState.name,
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black, lineHeight = 32.sp),
                        color = Color.White
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    // Play button overlay
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(LimeGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(32.dp))
                    }
                    
                    Text(
                        "TECHNIQUE PREVIEW",
                        modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }

            item {
                GymCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("CORRECT FORM", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    uiState.correctForm.forEach { BulletPoint(it) }
                }
            }

            item {
                GymCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color.Red, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("COMMON MISTAKES", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    uiState.commonMistakes.forEach { BulletPoint(it, color = Color.Red.copy(alpha = 0.7f)) }
                }
            }

            item {
                GymCard(
                    containerColor = Color.Transparent,
                    modifier = Modifier.background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.DarkGray.copy(alpha = 0.5f), Color.Black)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text("TRAINER ASSIGNMENT", style = MaterialTheme.typography.labelSmall, color = LimeGreen)
                        Text(
                            uiState.trainerAssignment.setsReps,
                            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                            color = Color.White
                        )
                        Text("Sets of Reps", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("WEIGHT RANGE", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                Text(uiState.trainerAssignment.weightRange, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("REST", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                Text(uiState.trainerAssignment.rest, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            }
                        }
                    }
                }
            }

            item {
                GymButton(
                    text = "START TRAINING",
                    onClick = {},
                    icon = Icons.Default.PlayArrow
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun BulletPoint(text: String, color: Color = LimeGreen) {
    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
        Box(modifier = Modifier.padding(top = 8.dp).size(4.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)
    }
}

@Preview
@Composable
fun ExerciseDetailScreenPreview() {
    GymmerTheme {
        ExerciseDetailContent(uiState = ExerciseDetailUiState())
    }
}
