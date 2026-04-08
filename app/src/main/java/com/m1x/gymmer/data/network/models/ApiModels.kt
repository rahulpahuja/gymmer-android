package com.m1x.gymmer.data.network.models

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class UpdateProfileRequest(
    val name: String? = null,
    val phone: String? = null,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val goals: String? = null
)

data class User(
    val id: UUID? = null,
    val gymId: UUID? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val passwordHash: String? = null,
    val role: String? = null,
    val status: String? = null,
    val createdAt: String? = null
)

data class LogWorkoutRequest(
    val userId: UUID? = null,
    val workoutPlanId: UUID? = null,
    val durationMinutes: Int? = null,
    val notes: String? = null
)

data class WorkoutLog(
    val id: UUID? = null,
    val userId: UUID? = null,
    val workoutPlanId: UUID? = null,
    val durationMinutes: Int? = null,
    val notes: String? = null,
    val loggedAt: String? = null
)

data class CheckIn(
    val id: UUID? = null,
    val userId: UUID? = null,
    val gymId: UUID? = null,
    val checkedInAt: String? = null
)

data class AssignPlanRequest(
    val planType: String? = null,
    val planId: UUID? = null
)

data class TraineePlan(
    val id: UUID? = null,
    val traineeId: UUID? = null,
    val trainerId: UUID? = null,
    val planType: String? = null,
    val planId: UUID? = null,
    val assignedAt: String? = null
)

data class TrainerAssignment(
    val id: UUID? = null,
    val trainerId: UUID? = null,
    val traineeId: UUID? = null,
    val gymId: UUID? = null,
    val createdAt: String? = null
)

data class LogNutritionRequest(
    val userId: UUID? = null,
    val type: String? = null,
    val amount: Double? = null
)

data class NutritionLog(
    val id: UUID? = null,
    val userId: UUID? = null,
    val type: String? = null,
    val amount: Double? = null,
    val loggedAt: String? = null
)

data class CreatePostRequest(
    val authorId: UUID? = null,
    val content: String? = null,
    val imageUrl: String? = null
)

data class Post(
    val id: UUID? = null,
    val authorId: UUID? = null,
    val content: String? = null,
    val imageUrl: String? = null,
    val createdAt: String? = null
)

data class AddCommentRequest(
    val authorId: UUID? = null,
    val content: String? = null
)

data class PostComment(
    val id: UUID? = null,
    val postId: UUID? = null,
    val authorId: UUID? = null,
    val content: String? = null,
    val createdAt: String? = null
)

data class SendMessageRequest(
    val content: String? = null
)

data class Message(
    val id: UUID? = null,
    val senderId: UUID? = null,
    val receiverId: UUID? = null,
    val content: String? = null,
    val sentAt: String? = null
)

data class RegisterRequest(
    val gymId: UUID? = null,
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
    val id: UUID? = null,
    val name: String? = null,
    val description: String? = null,
    val difficulty: String? = null,
    val gymId: UUID? = null
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
    val id: UUID? = null,
    val userId: UUID? = null,
    val date: String? = null,
    val meals: String? = null,
    val totalCalories: Int? = null
)

data class Exercise(
    val id: UUID? = null,
    val name: String? = null,
    val category: String? = null,
    val difficulty: String? = null,
    val videoUrl: String? = null,
    val description: String? = null
)

data class LeaderboardEntry(
    val userId: UUID? = null,
    val name: String? = null,
    val checkInCount: Int? = null
)

data class ConversationSummary(
    val otherUserId: UUID? = null,
    val lastMessage: Message? = null
)

data class RevenueDataPoint(
    val month: String? = null,
    val revenue: Double? = null
)

data class GymPulse(
    val gymId: UUID? = null,
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
    val userId: UUID? = null,
    val name: String? = null,
    val email: String? = null,
    val amountDue: Double? = null
)
