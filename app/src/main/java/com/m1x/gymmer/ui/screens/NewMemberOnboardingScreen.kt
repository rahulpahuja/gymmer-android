package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.m1x.gymmer.ui.components.*
import com.m1x.gymmer.ui.screens.state.NewMemberOnboardingUiState
import com.m1x.gymmer.ui.screens.state.OnboardingPlanState
import com.m1x.gymmer.ui.screens.state.OnboardingTrainerState
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun NewMemberOnboardingScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    uiState: NewMemberOnboardingUiState = NewMemberOnboardingUiState(
        membershipPlans = listOf(
            OnboardingPlanState("MONTHLY", "FLEXIBLE ACCESS", "₹9,999", "/MO", false),
            OnboardingPlanState("QUARTERLY", "PHASED COMMITMENT", "₹24,999", "/QT", true, isSelected = true),
            OnboardingPlanState("YEARLY", "ELITE ENDURANCE", "₹79,999", "/YR", false)
        ),
        availableTrainers = listOf(
            OnboardingTrainerState("Arjun Sharma", "STRENGTH SPECIALIST", true),
            OnboardingTrainerState("Priya Patel", "METABOLIC CONDITIONING", false),
            OnboardingTrainerState("Rohan Malhotra", "POWER & OPTIMIZATION", false)
        ),
        totalDueToday = "₹24,999"
    ),
    onNameChange: (String) -> Unit = {},
    onAgeChange: (String) -> Unit = {},
    onGoalToggled: (String) -> Unit = {},
    onPlanSelected: (String) -> Unit = {},
    onTrainerSelected: (String) -> Unit = {}
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            GymTopBar(title = "ONBOARDING", onMenuClick = onMenuClick)
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(Color.Black)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("TOTAL DUE TODAY", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(uiState.totalDueToday, style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black), color = Color.White)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("CONTRACT END", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(uiState.contractEndDate, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                GymButton(text = "COMPLETE ONBOARDING", onClick = { navController.popBackStack() })
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("RETURN TO DASHBOARD", style = MaterialTheme.typography.labelSmall, color = LimeGreen)
                    }
                    Text(
                        "NEW MEMBER\nONBOARDING",
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black, lineHeight = 32.sp),
                        color = Color.White
                    )
                    Text(
                        "Precision-guided enrollment for high-performance athletes. Log initial telemetry and establish training directives.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        GymSecondaryButton(text = "SAVE DRAFT", onClick = {}, modifier = Modifier.weight(1f).height(40.dp))
                        GymButton(text = "ACTIVATE PROFILE", onClick = {}, modifier = Modifier.weight(1f).height(40.dp))
                    }
                }
            }

            item {
                GymCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("CORE TELEMETRY", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    GymTextField(value = uiState.athleteName, onValueChange = onNameChange, label = "FULL IDENTITY", placeholder = "Athlete Full Name")
                    Spacer(modifier = Modifier.height(16.dp))
                    GymTextField(value = uiState.age, onValueChange = onAgeChange, label = "TEMPORAL AGE", placeholder = "24")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("PRIMARY DIRECTIVES (GOALS)", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Replaced FlowRow with a simple scrollable Row to avoid version mismatch crash
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.availableGoals.forEach { goal ->
                            GoalTag(
                                text = goal, 
                                isSelected = uiState.selectedGoals.contains(goal),
                                onClick = { onGoalToggled(goal) }
                            )
                        }
                    }
                }
            }

            item {
                GymCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ACCESS PROTOCOL", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    uiState.membershipPlans.forEach { plan ->
                        PlanOption(
                            title = plan.title,
                            subtitle = plan.subtitle,
                            price = plan.price,
                            unit = plan.unit,
                            isRecommended = plan.isRecommended,
                            isSelected = plan.isSelected,
                            onClick = { onPlanSelected(plan.title) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            item {
                GymCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("TRAINER ASSIGNMENT", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    uiState.availableTrainers.forEach { trainer ->
                        TrainerSelectOption(
                            name = trainer.name,
                            specialty = trainer.specialty,
                            isSelected = trainer.isSelected,
                            onSelect = { onTrainerSelected(trainer.name) }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("VIEW ALL STAFF", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
            
            item {
                GymCard(containerColor = Color.DarkGray.copy(alpha = 0.3f)) {
                    Text("NEXT STEP: VERIFICATION", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    Text(
                        "Once profile is activated, the athlete will receive an encrypted access key via mobile terminal. Biometric logging required upon first entry.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun GoalTag(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) LimeGreen.copy(alpha = 0.2f) else Color.DarkGray,
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, LimeGreen) else null,
        onClick = onClick
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) LimeGreen else Color.Gray
        )
    }
}

@Composable
fun PlanOption(
    title: String, 
    subtitle: String, 
    price: String, 
    unit: String, 
    isRecommended: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) LimeGreen else Color.DarkGray),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isRecommended) {
                Surface(color = LimeGreen, shape = RoundedCornerShape(4.dp)) {
                    Text("RECOMMENDED", modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Color.Black)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(price, style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black), color = Color.White)
                Text(unit, style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
            }
        }
    }
}

@Composable
fun TrainerSelectOption(name: String, specialty: String, isSelected: Boolean, onSelect: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        color = if (isSelected) LimeGreen.copy(alpha = 0.1f) else Color.DarkGray.copy(alpha = 0.3f),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, LimeGreen) else null,
        onClick = onSelect
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color.Gray, CircleShape))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text(specialty, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            if (isSelected) {
                RadioButton(selected = true, onClick = null, colors = RadioButtonDefaults.colors(selectedColor = LimeGreen))
            }
        }
    }
}

@Preview
@Composable
fun NewMemberOnboardingScreenPreview() {
    GymmerTheme {
        NewMemberOnboardingScreen(navController = rememberNavController())
    }
}
