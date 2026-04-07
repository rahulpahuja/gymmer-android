package com.m1x.gymmer.ui.screens

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import com.m1x.gymmer.R
import com.m1x.gymmer.ui.components.GymButton
import com.m1x.gymmer.ui.components.GymTopBar

@Composable
fun TestScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = { GymTopBar(title = "Test Diagnostics", onMenuClick = onBackClick) },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GymButton(
                text = "Show Toast",
                onClick = {
                    Toast.makeText(context, "Test Toast Message", Toast.LENGTH_SHORT).show()
                }
            )

            GymButton(
                text = "Trigger Notification",
                onClick = {
                    showNotification(context)
                }
            )

            GymButton(
                text = "Trigger Firebase Crash",
                onClick = {
                    throw RuntimeException("Test Crash for Firebase Crashlytics")
                }
            )
        }
    }
}

private fun showNotification(context: Context) {
    val channelId = "test_channel"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, "Test Channel", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Test Notification")
        .setContentText("This is a diagnostic test notification.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    notificationManager.notify(1, notification)
}
