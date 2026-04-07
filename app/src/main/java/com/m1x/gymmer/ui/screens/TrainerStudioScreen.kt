package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.m1x.gymmer.data.utils.AppConstants
import com.m1x.gymmer.ui.components.*
import com.m1x.gymmer.ui.navigation.Screen
import com.m1x.gymmer.ui.screens.state.MachineState
import com.m1x.gymmer.ui.screens.state.TrainerStudioUiState
import com.m1x.gymmer.ui.screens.state.UploadState
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun TrainerStudioScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    uiState: TrainerStudioUiState = TrainerStudioUiState(
        machineLibrary = listOf(
            MachineState("1", "Lat Pulldown", "3 LINKED VIDEOS"),
            MachineState("2", "Leg Press", "4 LINKED VIDEOS"),
            MachineState("3", "Chest Press", "5 LINKED VIDEOS")
        ),
        recentUploads = listOf(
            UploadState("1", "Back Squat Form Mastery", "Machine: Smith Machine", true),
            UploadState("2", "Single Arm Cable Row", "Machine: Cable Crossover", false),
            UploadState("3", "Optimal Foot Placement: Leg Press", "Machine: Leg Press", true)
        )
    )
) {
    TrainerStudioContent(
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
fun TrainerStudioContent(
    uiState: TrainerStudioUiState,
    onMenuClick: () -> Unit = {},
    onNavigateToScreen: (Int) -> Unit = {}
) {
    Scaffold(
        topBar = { GymTopBar(title = "Trainer Studio", onMenuClick = onMenuClick) },
        bottomBar = { 
            GymBottomNavigation(
                selectedItem = 1,
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                ContentReelBanner()
            }
            item {
                MachineLibrarySection(machines = uiState.machineLibrary)
            }
            item {
                RecentUploadsSection(uploads = uiState.recentUploads)
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ContentReelBanner() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "LEVEL UP YOUR\nCONTENT REEL",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Black,
                lineHeight = 36.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            ),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Upload high-performance exercise tutorials and link them directly to gym floor hardware.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        GymButton(
            text = "Upload New Exercise",
            onClick = {},
            icon = Icons.Default.Add,
            modifier = Modifier.width(240.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.DarkGray)
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?q=80&w=2070&auto=format&fit=crop",
                contentDescription = "Studio Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun MachineLibrarySection(machines: List<MachineState>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("HARDWARE CATALOG", style = MaterialTheme.typography.labelSmall, color = LimeGreen)
                Text("Machine Library", style = MaterialTheme.typography.headlineSmall, color = Color.White)
            }
            Text("VIEW ALL >", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(machines) { machine ->
                MachineCard(
                    name = machine.name,
                    videoCount = machine.videoCount
                )
            }
        }
    }
}

@Composable
fun MachineCard(name: String, videoCount: String) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(150.dp).background(Color.Gray)) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1540497077202-7c8a3999166f?q=80&w=2070&auto=format&fit=crop",
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(videoCount, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun RecentUploadsSection(uploads: List<UploadState>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("STUDIO ACTIVITY", style = MaterialTheme.typography.labelSmall, color = LimeGreen)
                Text("Recent Uploads", style = MaterialTheme.typography.headlineSmall, color = Color.White)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(color = Color.DarkGray, shape = RoundedCornerShape(8.dp)) {
                    Text("All", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
                Surface(color = LimeGreen.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
                    Text("Drafts", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = LimeGreen)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        uploads.forEach { upload ->
            UploadItem(
                title = upload.title,
                subtitle = upload.machineName,
                isActive = upload.isActive
            )
        }
    }
}

@Composable
fun UploadItem(title: String, subtitle: String, isActive: Boolean) {
    GymCard(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray)) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1574680096145-d05b474e2155?q=80&w=2069&auto=format&fit=crop",
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.White, modifier = Modifier.weight(1f))
                    if (isActive) {
                        Surface(color = LimeGreen.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                            Text("ACTIVE", modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = LimeGreen)
                        }
                    } else {
                        Surface(color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                            Text("DRAFT", modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                    }
                }
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("EDIT", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PREVIEW", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("DELETE", style = MaterialTheme.typography.labelSmall, color = Color.Red.copy(alpha = 0.6f))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TrainerStudioScreenPreview() {
    GymmerTheme {
        TrainerStudioContent(uiState = TrainerStudioUiState())
    }
}
