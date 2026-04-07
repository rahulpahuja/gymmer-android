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
                PostState("1", "John Doe", null, "Just hit a new PR on Squats! 140kg!", null, "2h ago", 24, 5, true),
                PostState("2", "Jane Smith", null, "Love the new HIIT session.", null, "5h ago", 12, 2)
            ),
            leaderboard = listOf(
                LeaderboardState(1, "Mike Ross", 1250),
                LeaderboardState(2, "Harvey Specter", 1100),
                LeaderboardState(3, "Rachel Zane", 950)
            )
        )
    )
    val uiState: StateFlow<CommunityFeedUiState> = _uiState.asStateFlow()

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }
}
