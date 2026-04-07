package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.m1x.gymmer.ui.screens.state.ScanUiState
import com.m1x.gymmer.ui.screens.viewmodel.ScanViewModel
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun ScanScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    viewModel: ScanViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    ScanContent(
        uiState = uiState,
        onFlashlightToggle = viewModel::toggleFlashlight,
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
fun ScanContent(
    uiState: ScanUiState,
    onFlashlightToggle: () -> Unit,
    onMenuClick: () -> Unit = {},
    onNavigateToScreen: (Int) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Camera Preview Placeholder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray.copy(alpha = 0.5f))
        ) {
            // Background Image/Video would go here
        }

        Column(modifier = Modifier.fillMaxSize()) {
            GymTopBar(title = "KINETIC", onMenuClick = onMenuClick)
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Scanner Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                // Scanning corners
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .border(2.dp, Color.Transparent)
                ) {
                    val cornerSize = 40.dp
                    val thickness = 4.dp
                    
                    // Top Left
                    Box(modifier = Modifier.size(cornerSize).align(Alignment.TopStart)) {
                        Box(modifier = Modifier.fillMaxWidth().height(thickness).background(LimeGreen))
                        Box(modifier = Modifier.fillMaxHeight().width(thickness).background(LimeGreen))
                    }
                    // Top Right
                    Box(modifier = Modifier.size(cornerSize).align(Alignment.TopEnd)) {
                        Box(modifier = Modifier.fillMaxWidth().height(thickness).background(LimeGreen))
                        Box(modifier = Modifier.fillMaxHeight().width(thickness).align(Alignment.TopEnd).background(LimeGreen))
                    }
                    // Bottom Left
                    Box(modifier = Modifier.size(cornerSize).align(Alignment.BottomStart)) {
                        Box(modifier = Modifier.fillMaxWidth().height(thickness).align(Alignment.BottomStart).background(LimeGreen))
                        Box(modifier = Modifier.fillMaxHeight().width(thickness).background(LimeGreen))
                    }
                    // Bottom Right
                    Box(modifier = Modifier.size(cornerSize).align(Alignment.BottomEnd)) {
                        Box(modifier = Modifier.fillMaxWidth().height(thickness).align(Alignment.BottomEnd).background(LimeGreen))
                        Box(modifier = Modifier.fillMaxHeight().width(thickness).align(Alignment.TopEnd).align(Alignment.BottomEnd).background(LimeGreen))
                    }
                    
                    // Scanning line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(LimeGreen.copy(alpha = 0.5f))
                            .align(Alignment.Center)
                    )
                }
            }
            
            Text(
                text = "POINT AT GYM ENTRY QR OR MACHINE QR",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp),
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.weight(0.5f))

            // Recent Activity in Scan Screen
            GymCard(
                modifier = Modifier.padding(16.dp),
                containerColor = Color.Black.copy(alpha = 0.8f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(LimeGreen, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("RECENT ACTIVITY", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(uiState.recentActivity, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    }
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.White)
                }
            }
            
            GymBottomNavigation(
                selectedItem = 2,
                onItemSelected = onNavigateToScreen
            )
        }
        
        // Flashlight button
        IconButton(
            onClick = onFlashlightToggle,
            modifier = Modifier
                .padding(top = 100.dp, end = 16.dp)
                .size(48.dp)
                .background(if (uiState.isFlashlightOn) LimeGreen.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f), CircleShape)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                Icons.Default.Info, 
                contentDescription = "Flashlight", 
                tint = if (uiState.isFlashlightOn) Color.Black else Color.White
            )
        }
    }
}

@Preview
@Composable
fun ScanScreenPreview() {
    GymmerTheme {
        ScanContent(
            uiState = ScanUiState(),
            onFlashlightToggle = {}
        )
    }
}
