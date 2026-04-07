package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.CommunityFeedUiState
import com.m1x.gymmer.ui.screens.state.LeaderboardState
import com.m1x.gymmer.ui.screens.state.PostState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CommunityFeedViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        CommunityFeedUiState(
            posts = listOf(
                PostState(
                    id = "1",
                    userName = "John Doe",
                    userAvatar = "https://i.pravatar.cc/150?u=john",
                    content = "Just hit a new PR on Squats! 140kg!",
                    imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=1000&auto=format&fit=crop",
                    timeAgo = "2h ago",
                    likesCount = 24,
                    commentsCount = 5,
                    isLiked = true
                ),
                PostState(
                    id = "2",
                    userName = "Jane Smith",
                    userAvatar = "https://i.pravatar.cc/150?u=jane",
                    content = "Love the new HIIT session. Highly recommend it to everyone starting their journey!",
                    imageUrl = "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?q=80&w=1000&auto=format&fit=crop",
                    timeAgo = "5h ago",
                    likesCount = 12,
                    commentsCount = 2
                )
            ),
            leaderboard = listOf(
                LeaderboardState(1, "Mike Ross", 1250, "https://i.pravatar.cc/150?u=mike"),
                LeaderboardState(2, "Harvey Specter", 1100, "https://i.pravatar.cc/150?u=harvey"),
                LeaderboardState(3, "Rachel Zane", 950, "https://i.pravatar.cc/150?u=rachel")
            )
        )
    )
    val uiState: StateFlow<CommunityFeedUiState> = _uiState.asStateFlow()

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }
}
