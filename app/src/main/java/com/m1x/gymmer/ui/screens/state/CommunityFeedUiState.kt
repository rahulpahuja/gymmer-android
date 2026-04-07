package com.m1x.gymmer.ui.screens.state

data class CommunityFeedUiState(
    val posts: List<PostState> = emptyList(),
    val leaderboard: List<LeaderboardState> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTab: Int = 0 // 0 for Feed, 1 for Leaderboard
)

data class PostState(
    val id: String,
    val userName: String,
    val userAvatar: String?,
    val content: String,
    val imageUrl: String?,
    val timeAgo: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean = false
)

data class LeaderboardState(
    val rank: Int,
    val userName: String,
    val points: Int,
    val avatarUrl: String? = null,
    val isCurrentUser: Boolean = false
)
