package com.m1x.gymmer.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m1x.gymmer.ui.theme.GymmerTheme
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun GymButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = LimeGreen,
    contentColor: Color = Color.Black,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(28.dp),
        enabled = enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            )
            if (icon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun GymSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
        enabled = enabled
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
    }
}

@Composable
fun GymTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    enabled: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label.isNotEmpty()) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null, tint = Color.Gray) } },
            trailingIcon = trailingIcon,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            enabled = enabled
        )
    }
}

@Composable
fun GymCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    padding: PaddingValues = PaddingValues(16.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        onClick = { onClick?.invoke() },
        enabled = onClick != null
    ) {
        Column(
            modifier = Modifier.padding(padding),
            content = content
        )
    }
}

@Composable
fun GymStatItem(
    label: String,
    value: String,
    unit: String = "",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = LimeGreen
                )
            )
            if (unit.isNotEmpty()) {
                Text(
                    text = " $unit",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}

@Composable
fun GymChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) LimeGreen else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (isSelected) Color.Black else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun GymSectionHeader(
    title: String,
    onSeeAllClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            ),
            color = Color.White
        )
        if (onSeeAllClick != null) {
            TextButton(onClick = onSeeAllClick) {
                Text(
                    text = "SEE ALL",
                    style = MaterialTheme.typography.labelSmall,
                    color = LimeGreen
                )
            }
        }
    }
}

@Composable
fun GymBadge(
    text: String,
    containerColor: Color = LimeGreen.copy(alpha = 0.2f),
    contentColor: Color = LimeGreen,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = containerColor,
        contentColor = contentColor
    ) {
        Text(
            text = text.uppercase(),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold
            )
        )
    }
}

@Composable
fun GymTopBar(
    title: String,
    onMenuClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
        }
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            ),
            color = Color.White
        )
        IconButton(onClick = onNotificationClick) {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = LimeGreen)
        }
    }
}

@Composable
fun GymBottomNavigation(selectedItem: Int, onItemSelected: (Int) -> Unit = {}) {
    NavigationBar(
        containerColor = Color.Black.copy(alpha = 0.9f)
    ) {
        val items = listOf("DASH", "TRAIN", "SCAN", "WALLET", "PROFILE")
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                icon = {
                    if (index == 2) { // SCAN button is special
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .offset(y = (-10).dp)
                                .background(LimeGreen, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                        }
                    } else {
                        Icon(Icons.Default.Person, contentDescription = null)
                    }
                },
                label = { Text(item, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LimeGreen,
                    selectedTextColor = LimeGreen,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun GymDrawerContent(
    selectedRoute: String,
    onRouteSelected: (String) -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("LOGOUT", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("CANCEL", color = Color.Gray)
                }
            },
            containerColor = Color.DarkGray,
            titleContentColor = Color.White,
            textContentColor = Color.LightGray
        )
    }

    ModalDrawerSheet(
        drawerContainerColor = Color.Black,
        drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Text(
            "GYMOPS",
            modifier = Modifier.padding(horizontal = 28.dp),
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black),
            color = Color.White
        )
        Text(
            "MANAGEMENT SYSTEM",
            modifier = Modifier.padding(horizontal = 28.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(48.dp))
        
        val menuItems = listOf(
            "Dashboard" to "dashboard",
            "Nutrition" to "nutrition",
            "Community" to "community",
            "Chat" to "chat",
            "Attendance Logs" to "attendance_logs",
            "Trainer Studio" to "trainer_studio",
            "Business Insights" to "business_insights",
            "Payment Defaulters" to "payment_defaulters",
            "New Member Onboarding" to "onboarding",
            "My Trainees" to "my_trainees"
        )

        menuItems.forEach { (label, route) ->
            NavigationDrawerItem(
                label = { Text(label) },
                selected = selectedRoute == route,
                onClick = { onRouteSelected(route) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = LimeGreen,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.White,
                    unselectedContainerColor = Color.Transparent
                ),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        NavigationDrawerItem(
            label = { Text("Logout") },
            selected = false,
            onClick = { showLogoutDialog = true },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedTextColor = Color.Red,
                unselectedContainerColor = Color.Transparent
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 24.dp)
        )
    }
}

@Preview
@Composable
fun ComponentsPreview() {
    GymmerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            GymSectionHeader(title = "Core Actions", onSeeAllClick = {})
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                GymButton(text = "Initiate", onClick = {}, modifier = Modifier.weight(1f))
                GymSecondaryButton(text = "Manage", onClick = {}, modifier = Modifier.weight(1f))
            }
            
            GymTextField(
                value = "", 
                onValueChange = {}, 
                label = "Trainee Identifier", 
                placeholder = "trainee@kinetic.com",
                leadingIcon = Icons.Default.Search
            )
            
            GymSectionHeader(title = "Performance Stats")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                GymStatItem(label = "Sessions", value = "24")
                GymStatItem(label = "Efficiency", value = "92", unit = "%")
                GymStatItem(label = "Active", value = "12")
            }
            
            GymSectionHeader(title = "Categories")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GymChip(text = "Strength", isSelected = true, onClick = {})
                GymChip(text = "Cardio", isSelected = false, onClick = {})
                GymChip(text = "HIIT", isSelected = false, onClick = {})
            }
            
            GymCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Morning Blast Session", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        Text("08:00 AM - 09:30 AM", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    GymBadge(text = "Active")
                }
            }
        }
    }
}
