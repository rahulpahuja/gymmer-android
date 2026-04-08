package com.m1x.gymmer.data.repository

import com.m1x.gymmer.data.network.ApiService
import com.m1x.gymmer.data.network.models.*
import java.util.UUID

class GymmerRepository(private val apiService: ApiService) {

    // Auth
    suspend fun login(request: LoginRequest): User = apiService.login(request)
    suspend fun register(request: RegisterRequest): User = apiService.register(request)

    // User
    suspend fun getProfile(userId: UUID): User = apiService.getProfile(userId)
    suspend fun getDashboard(userId: UUID): DashboardData = apiService.getDashboard(userId)

    // Community
    suspend fun getFeed(): List<Post> = apiService.getFeed()
    suspend fun getLeaderboard(): List<LeaderboardEntry> = apiService.getLeaderboard()
    suspend fun toggleLike(postId: UUID, userId: UUID) = apiService.toggleLike(postId, userId)

    // Chat
    suspend fun getConversations(userId: UUID): List<ConversationSummary> = apiService.getConversations(userId)
    suspend fun getMessages(otherUserId: UUID, currentUserId: UUID): List<Message> = apiService.getMessages(otherUserId, currentUserId)
    suspend fun sendMessage(receiverId: UUID, senderId: UUID, content: String): Message {
        return apiService.sendMessage(receiverId, senderId, SendMessageRequest(content))
    }

    // Workouts
    suspend fun listWorkouts(): List<WorkoutPlan> = apiService.listWorkouts()
    suspend fun listExercises(category: String? = null, difficulty: String? = null): List<Exercise> = apiService.listExercises(category, difficulty)
}
