package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m1x.gymmer.ui.components.GymCard
import com.m1x.gymmer.ui.components.GymStatItem
import com.m1x.gymmer.ui.components.GymTopBar
import com.m1x.gymmer.ui.screens.state.MealState
import com.m1x.gymmer.ui.screens.state.NutritionUiState
import com.m1x.gymmer.ui.screens.viewmodel.NutritionViewModel
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun NutritionScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    viewModel: NutritionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { GymTopBar(title = "NUTRITION", onMenuClick = onMenuClick) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add meal logic */ },
                containerColor = LimeGreen,
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Meal")
            }
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
                CalorieOverviewCard(uiState)
            }
            item {
                MacroSection(uiState)
            }
            item {
                Text(
                    "TODAY'S MEALS",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color.White
                )
            }
            items(uiState.meals) { meal ->
                MealCard(meal)
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun CalorieOverviewCard(uiState: NutritionUiState) {
    GymCard {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "CALORIES REMAINING",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            val remaining = uiState.dailyCaloriesTarget - uiState.consumedCalories
            Text(
                remaining.toString(),
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black),
                color = LimeGreen
            )
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { uiState.consumedCalories.toFloat() / uiState.dailyCaloriesTarget },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = LimeGreen,
                trackColor = Color.DarkGray
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Consumed: ${uiState.consumedCalories}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text("Target: ${uiState.dailyCaloriesTarget}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun MacroSection(uiState: NutritionUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GymStatItem(
            label = "Protein",
            value = "${uiState.consumedProtein.toInt()}",
            unit = "/${uiState.proteinTarget.toInt()}g",
            modifier = Modifier.weight(1f)
        )
        GymStatItem(
            label = "Carbs",
            value = "${uiState.consumedCarbs.toInt()}",
            unit = "/${uiState.carbsTarget.toInt()}g",
            modifier = Modifier.weight(1f)
        )
        GymStatItem(
            label = "Fats",
            value = "${uiState.consumedFat.toInt()}",
            unit = "/${uiState.fatTarget.toInt()}g",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun MealCard(meal: MealState) {
    GymCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(meal.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text("${meal.calories} kcal", style = MaterialTheme.typography.labelMedium, color = LimeGreen)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("P: ${meal.protein.toInt()}g", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text("C: ${meal.carbs.toInt()}g", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text("F: ${meal.fat.toInt()}g", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
        if (meal.items.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.DarkGray)
            meal.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.name, style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                    Text("${item.calories} kcal", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}
