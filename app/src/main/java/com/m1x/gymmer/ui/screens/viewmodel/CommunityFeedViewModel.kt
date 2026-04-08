package com.m1x.gymmer.ui.screens.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.ui.screens.state.CommunityFeedUiState
import com.m1x.gymmer.ui.screens.state.LeaderboardState
import com.m1x.gymmer.ui.screens.state.PostState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityFeedViewModel(application: Application) : AndroidViewModel(application) {
    private val gymmerApp = application as GymmerApplication
    private val repository = gymmerApp.repository
    private val logManager = gymmerApp.logManager

    private val _uiState = MutableStateFlow(CommunityFeedUiState())
    val uiState: StateFlow<CommunityFeedUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val posts = repository.getFeed().map { apiPost ->
                    PostState(
                        id = apiPost.id.toString(),
                        userName = "User ${apiPost.authorId.toString().take(4)}", // Mock name
                        userAvatar = null,
                        content = apiPost.content ?: "",
                        imageUrl = apiPost.imageUrl,
                        timeAgo = "Just now", // Mock time
                        likesCount = 0,
                        commentsCount = 0,
                        isLiked = false
                    )
                }
                
                val leaderboard = repository.getLeaderboard().map { apiEntry ->
                    LeaderboardState(
                        rank = 0, // Will be updated by rank index
                        userName = apiEntry.name ?: "Unknown",
                        points = apiEntry.checkInCount ?: 0,
                        avatarUrl = null
                    )
                }.mapIndexed { index, state -> state.copy(rank = index + 1) }

                _uiState.update { 
                    it.copy(
                        posts = posts,
                        leaderboard = leaderboard,
                        isLoading = false
                    ) 
                }
            } catch (e: Exception) {
                logManager.info(LogManager.LogCategory.ERRORS, "Failed to load community data: ${e.message}")
                _uiState.update { it.copy(isLoading = false) }
                // Fallback to mock data if API fails (optional, but good for demo)
                loadMockData()
            }
        }
    }

    private fun loadMockData() {
        _uiState.update {
            it.copy(
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
                    )
                ),
                leaderboard = listOf(
                    LeaderboardState(1, "Mike Ross", 1250, "https://i.pravatar.cc/150?u=mike")
                )
            )
        }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }
}
