package com.m1x.gymmer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m1x.gymmer.ui.components.GymBadge
import com.m1x.gymmer.ui.components.GymCard
import com.m1x.gymmer.ui.components.GymTopBar
import com.m1x.gymmer.ui.screens.state.CommunityFeedUiState
import com.m1x.gymmer.ui.screens.state.LeaderboardState
import com.m1x.gymmer.ui.screens.state.PostState
import com.m1x.gymmer.ui.screens.viewmodel.CommunityFeedViewModel
import com.m1x.gymmer.ui.theme.LimeGreen

@Composable
fun CommunityFeedScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {},
    viewModel: CommunityFeedViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { GymTopBar(title = "COMMUNITY", onMenuClick = onMenuClick) },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = uiState.selectedTab,
                containerColor = Color.Black,
                contentColor = LimeGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                        color = LimeGreen
                    )
                }
            ) {
                Tab(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.onTabSelected(0) },
                    text = { Text("FEED", style = MaterialTheme.typography.labelLarge) }
                )
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.onTabSelected(1) },
                    text = { Text("LEADERBOARD", style = MaterialTheme.typography.labelLarge) }
                )
            }

            if (uiState.selectedTab == 0) {
                FeedList(uiState.posts)
            } else {
                LeaderboardList(uiState.leaderboard)
            }
        }
    }
}

@Composable
fun FeedList(posts: List<PostState>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(posts) { post ->
            PostItem(post)
        }
    }
}

@Composable
fun PostItem(post: PostState) {
    GymCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(post.userName, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                Text(post.timeAgo, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(post.content, style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)
        
        if (post.imageUrl != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.DarkGray)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = if (post.isLiked) Color.Red else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Text(
                " ${post.likesCount}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.padding(end = 16.dp)
            )
            Icon(
                Icons.Default.Share,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Text(
                " ${post.commentsCount}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun LeaderboardList(leaderboard: List<LeaderboardState>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(leaderboard) { entry ->
            LeaderboardItem(entry)
        }
    }
}

@Composable
fun LeaderboardItem(entry: LeaderboardState) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (entry.isCurrentUser) LimeGreen.copy(alpha = 0.1f) else Color.Transparent,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${entry.rank}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                color = if (entry.rank <= 3) LimeGreen else Color.Gray,
                modifier = Modifier.width(40.dp)
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                entry.userName,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Text(
                "${entry.points} pts",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = LimeGreen
            )
        }
    }
}
