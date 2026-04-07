package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.compose.rememberNavController
import com.m1x.gymmer.ui.components.*
import com.m1x.gymmer.ui.navigation.Screen
import com.m1x.gymmer.ui.screens.state.TraineeSummary
import com.m1x.gymmer.ui.screens.state.WorkoutExerciseState
import com.m1x.gymmer.ui.screens.state.WorkoutsUiState
import com.m1x.gymmer.ui.screens.viewmodel.WorkoutsViewModel
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun WorkoutsScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    viewModel: WorkoutsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    WorkoutsContent(
        uiState = uiState,
        onTraineeSelected = viewModel::onTraineeSelected,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
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
fun WorkoutsContent(
    uiState: WorkoutsUiState,
    onTraineeSelected: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onMenuClick: () -> Unit = {},
    onNavigateToScreen: (Int) -> Unit = {}
) {
    val scrollState = rememberLazyListState()
    
    Scaffold(
        topBar = { GymTopBar(title = "KINETIC", onMenuClick = onMenuClick) },
        bottomBar = { 
            GymBottomNavigation(
                selectedItem = 1,
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
                SelectTraineeSection(trainees = uiState.trainees, onTraineeSelected = onTraineeSelected)
            }
            item {
                ExerciseLibrarySection(
                    searchQuery = uiState.exerciseSearchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                    exercises = uiState.libraryExercises
                )
            }
            item {
                BuildSessionSection(
                    name = uiState.currentSession.name,
                    target = uiState.currentSession.target
                )
            }
            items(uiState.sessionExercises) { exercise ->
                WorkoutExerciseCard(
                    name = exercise.name,
                    target = exercise.target,
                    sets = exercise.sets,
                    reps = exercise.reps,
                    tempo = exercise.tempo,
                    rest = exercise.rest
                )
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SelectTraineeSection(trainees: List<TraineeSummary>, onTraineeSelected: (String) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Add, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("SELECT_TRAINEE", style = MaterialTheme.typography.labelSmall, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        GymCard {
            trainees.forEachIndexed { index, trainee ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).background(Color.Gray, CircleShape))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(trainee.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = if (trainee.isSelected) FontWeight.Bold else FontWeight.Normal), color = if (trainee.isSelected) Color.White else Color.Gray)
                        Text(trainee.level, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    RadioButton(
                        selected = trainee.isSelected,
                        onClick = { onTraineeSelected(trainee.id) },
                        colors = RadioButtonDefaults.colors(selectedColor = LimeGreen)
                    )
                }
                if (index < trainees.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.DarkGray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ExerciseLibrarySection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    exercises: List<String>
) {
    Column {
        Text("EXERCISE_LIBRARY", style = MaterialTheme.typography.labelSmall, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        GymTextField(value = searchQuery, onValueChange = onSearchQueryChange, label = "", placeholder = "Search movements...", leadingIcon = Icons.Default.Search)
        Spacer(modifier = Modifier.height(16.dp))
        exercises.forEach { exercise ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(24.dp).background(Color.DarkGray, RoundedCornerShape(4.dp)))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(exercise, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun BuildSessionSection(name: String, target: String) {
    Column {
        Surface(color = Color.DarkGray, shape = RoundedCornerShape(8.dp)) {
            Text("BUILD SESSION", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(name, style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black), color = Color.White)
        Text(target, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GymSecondaryButton(text = "SAVE_DRAFT", onClick = {}, modifier = Modifier.weight(1f).height(48.dp))
            GymButton(text = "ASSIGN_ROUTINE", onClick = {}, modifier = Modifier.weight(1f).height(48.dp))
        }
    }
}

@Composable
fun WorkoutExerciseCard(name: String, target: String, sets: String, reps: String, tempo: String, rest: String) {
    GymCard(modifier = Modifier.padding(bottom = 16.dp)) {
        Row(verticalAlignment = Alignment.Top) {
            Box(modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black), color = Color.White)
                        Text("TARGET: $target", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatBox("SETS", sets, Modifier.weight(1f))
                    StatBox("REPS", reps, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatBox("TEMPO", tempo, Modifier.weight(1f))
                    StatBox("REST", rest, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Focus on the squeeze at the bottom of the movement...", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.3f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
        }
    }
}

@Preview
@Composable
fun WorkoutsScreenPreview() {
    GymmerTheme {
        WorkoutsContent(
            uiState = WorkoutsUiState(),
            onTraineeSelected = {},
            onSearchQueryChange = {},
            onMenuClick = {},
            onNavigateToScreen = {}
        )
    }
}
