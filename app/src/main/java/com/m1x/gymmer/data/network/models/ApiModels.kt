package com.m1x.gymmer.data.network.models

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    val name: String? = null,
    val phone: String? = null,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val goals: String? = null
)

data class User(
    val id: String? = null,
    val gymId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val passwordHash: String? = null,
    val role: String? = null,
    val status: String? = null,
    val createdAt: String? = null
)

data class LogWorkoutRequest(
    val userId: String? = null,
    val workoutPlanId: String? = null,
    val durationMinutes: Int? = null,
    val notes: String? = null
)

data class WorkoutLog(
    val id: String? = null,
    val userId: String? = null,
    val workoutPlanId: String? = null,
    val durationMinutes: Int? = null,
    val notes: String? = null,
    val loggedAt: String? = null
)

data class CheckIn(
    val id: String? = null,
    val userId: String? = null,
    val gymId: String? = null,
    val checkedInAt: String? = null
)

data class AssignPlanRequest(
    val planType: String? = null,
    val planId: String? = null
)

data class TraineePlan(
    val id: String? = null,
    val traineeId: String? = null,
    val trainerId: String? = null,
    val planType: String? = null,
    val planId: String? = null,
    val assignedAt: String? = null
)

data class TrainerAssignment(
    val id: String? = null,
    val trainerId: String? = null,
    val traineeId: String? = null,
    val gymId: String? = null,
    val createdAt: String? = null
)

data class LogNutritionRequest(
    val userId: String? = null,
    val type: String? = null,
    val amount: Double? = null
)

data class NutritionLog(
    val id: String? = null,
    val userId: String? = null,
    val type: String? = null,
    val amount: Double? = null,
    val loggedAt: String? = null
)

data class CreatePostRequest(
    val authorId: String? = null,
    val content: String? = null,
    val imageUrl: String? = null
)

data class Post(
    val id: String? = null,
    val authorId: String? = null,
    val content: String? = null,
    val imageUrl: String? = null,
    val createdAt: String? = null
)

data class AddCommentRequest(
    val authorId: String? = null,
    val content: String? = null
)

data class PostComment(
    val id: String? = null,
    val postId: String? = null,
    val authorId: String? = null,
    val content: String? = null,
    val createdAt: String? = null
)

data class SendMessageRequest(
    val content: String? = null
)

data class Message(
    val id: String? = null,
    val senderId: String? = null,
    val receiverId: String? = null,
    val content: String? = null,
    val sentAt: String? = null
)

data class RegisterRequest(
    val gymId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val password: String? = null,
    val role: String? = null
)

data class RefreshRequest(
    val refreshToken: String? = null
)

data class LoginRequest(
    val email: String? = null,
    val password: String? = null
)

data class WorkoutPlan(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val difficulty: String? = null,
    val gymId: String? = null
)

data class DashboardData(
    val user: User? = null,
    val todayCheckIn: CheckIn? = null,
    val activeSessionCount: Int? = null
)

data class TraineeProgress(
    val trainee: User? = null,
    val checkIns: List<CheckIn>? = null,
    val workoutLogs: List<WorkoutLog>? = null
)

data class TrainerDashboardData(
    val trainer: User? = null,
    val activeTraineeCount: Int? = null,
    val trainees: List<User>? = null
)

data class MealPlan(
    val id: String? = null,
    val userId: String? = null,
    val date: String? = null,
    val meals: String? = null,
    val totalCalories: Int? = null
)

data class Exercise(
    val id: String? = null,
    val name: String? = null,
    val category: String? = null,
    val difficulty: String? = null,
    val videoUrl: String? = null,
    val description: String? = null
)

data class LeaderboardEntry(
    val userId: String? = null,
    val name: String? = null,
    val checkInCount: Int? = null
)

data class ConversationSummary(
    val otherUserId: String? = null,
    val lastMessage: Message? = null
)

data class RevenueDataPoint(
    val month: String? = null,
    val revenue: Double? = null
)

data class GymPulse(
    val gymId: String? = null,
    val gymName: String? = null,
    val currentCapacity: Int? = null,
    val activeSessions: Int? = null
)

data class BusinessInsights(
    val annualRevenue: Double? = null,
    val retentionRate: Double? = null,
    val memberGrowth: Int? = null
)

data class Defaulter(
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val amountDue: Double? = null
)
