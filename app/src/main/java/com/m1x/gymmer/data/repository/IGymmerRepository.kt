package com.m1x.gymmer.data.repository

import com.m1x.gymmer.data.network.models.*
import java.util.UUID

interface IGymmerRepository {
    suspend fun login(loginRequest: LoginRequest): User
    suspend fun register(registerRequest: RegisterRequest): User
    suspend fun refresh(refreshRequest: RefreshRequest): Map<String, String>

    suspend fun getProfile(userId: UUID): User
    suspend fun updateProfile(userId: UUID, request: UpdateProfileRequest): User
    suspend fun checkIn(userId: UUID): CheckIn
    suspend fun getDashboard(userId: UUID): DashboardData

    suspend fun logWorkout(request: LogWorkoutRequest): WorkoutLog
    suspend fun listWorkouts(): List<WorkoutPlan>
    suspend fun getWorkout(workoutId: UUID): WorkoutPlan
    suspend fun listExercises(category: String? = null, difficulty: String? = null): List<Exercise>
    suspend fun getExercise(exerciseId: UUID): Exercise

    suspend fun assignPlan(trainerId: UUID, traineeId: UUID, request: AssignPlanRequest): TraineePlan
    suspend fun assignTrainer(gymId: UUID, trainerId: UUID, traineeId: UUID): TrainerAssignment
    suspend fun getAssignments(trainerId: UUID): List<TrainerAssignment>
    suspend fun getTrainees(trainerId: UUID): List<User>
    suspend fun getTraineeProgress(traineeId: UUID): TraineeProgress
    suspend fun getTrainersByGym(gymId: UUID): List<User>
    suspend fun getTrainerDashboard(trainerId: UUID): TrainerDashboardData
    suspend fun getAllTrainers(): List<User>
    suspend fun deleteAssignment(assignmentId: UUID)

    suspend fun logNutrition(request: LogNutritionRequest): NutritionLog
    suspend fun getTodayNutrition(userId: UUID): MealPlan

    suspend fun createPost(request: CreatePostRequest): Post
    suspend fun toggleLike(postId: UUID, userId: UUID): Map<String, Any>
    suspend fun getComments(postId: UUID): List<PostComment>
    suspend fun addComment(postId: UUID, request: AddCommentRequest): PostComment
    suspend fun getLeaderboard(): List<LeaderboardEntry>
    suspend fun getFeed(): List<Post>

    suspend fun getMessages(senderId: UUID, receiverId: UUID): List<Message>
    suspend fun sendMessage(senderId: UUID, receiverId: UUID, content: String): Message
    suspend fun getConversations(userId: UUID): List<ConversationSummary>

    suspend fun getRevenueKinetics(): List<RevenueDataPoint>
    suspend fun getPulse(): List<GymPulse>
    suspend fun getBusinessInsights(): BusinessInsights
    suspend fun getDefaulters(): List<Defaulter>

    suspend fun hello(): Map<String, String>
    suspend fun greet(name: String): Map<String, String>
}
