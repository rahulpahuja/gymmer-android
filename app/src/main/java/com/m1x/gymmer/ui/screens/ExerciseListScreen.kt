package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.m1x.gymmer.ui.components.GymCard
import com.m1x.gymmer.ui.components.GymTopBar
import com.m1x.gymmer.ui.navigation.Screen
import com.m1x.gymmer.ui.theme.LimeGreen
import coil.compose.AsyncImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class ExerciseItem(
    val id: String,
    val title: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val duration: String
)

@Composable
fun ExerciseListScreen(
    navController: NavController,
    category: String,
    onMenuClick: () -> Unit
) {
    val sampleVideos = listOf(
        "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4" to "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.png",
        "https://storage.googleapis.com/exoplayer-test-media-0/Jazz_Concert_720.mp4" to "https://storage.googleapis.com/exoplayer-test-media-0/Jazz_Concert_720.png",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4" to "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4" to "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4" to "https://storage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerFun.jpg"
    )

    val exercises = List(20) { index ->
        val (videoUrl, thumbnailUrl) = sampleVideos[index % sampleVideos.size]
        ExerciseItem(
            id = index.toString(),
            title = "$category Exercise ${index + 1}",
            videoUrl = videoUrl,
            thumbnailUrl = thumbnailUrl,
            duration = "${(2..5).random()} min"
        )
    }

    Scaffold(
        topBar = { GymTopBar(title = category, onMenuClick = onMenuClick) },
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(exercises) { exercise ->
                ExerciseVideoCard(
                    exercise = exercise,
                    onClick = {
                        val encodedUrl = URLEncoder.encode(exercise.videoUrl, StandardCharsets.UTF_8.toString())
                        val route = Screen.VideoPlayer.createRoute(exercise.title, encodedUrl)
                        navController.navigate(route)
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun ExerciseVideoCard(exercise: ExerciseItem, onClick: () -> Unit) {
    GymCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(100.dp, 60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = exercise.thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = LimeGreen)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(exercise.title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text(exercise.duration, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun GymCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        onClick = { onClick?.invoke() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}
