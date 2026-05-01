package com.m1x.gymmer.data.repository

import com.m1x.gymmer.data.network.models.*

interface IGymmerRepository {
    suspend fun login(loginRequest: LoginRequest): User
    suspend fun register(registerRequest: RegisterRequest): User
    suspend fun refresh(refreshRequest: RefreshRequest): Map<String, String>

    suspend fun getProfile(userId: String): User
    suspend fun updateProfile(userId: String, request: UpdateProfileRequest): User
    suspend fun checkIn(userId: String): CheckIn
    suspend fun getDashboard(userId: String): DashboardData

    suspend fun logWorkout(request: LogWorkoutRequest): WorkoutLog
    suspend fun listWorkouts(): List<WorkoutPlan>
    suspend fun getWorkout(workoutId: String): WorkoutPlan
    suspend fun listExercises(category: String? = null, difficulty: String? = null): List<Exercise>
    suspend fun getExercise(exerciseId: String): Exercise

    suspend fun assignPlan(trainerId: String, traineeId: String, request: AssignPlanRequest): TraineePlan
    suspend fun assignTrainer(gymId: String, trainerId: String, traineeId: String): TrainerAssignment
    suspend fun getAssignments(trainerId: String): List<TrainerAssignment>
    suspend fun getTrainees(trainerId: String): List<User>
    suspend fun getTraineeProgress(traineeId: String): TraineeProgress
    suspend fun getTrainersByGym(gymId: String): List<User>
    suspend fun getTrainerDashboard(trainerId: String): TrainerDashboardData
    suspend fun getAllTrainers(): List<User>
    suspend fun deleteAssignment(assignmentId: String)

    suspend fun logNutrition(request: LogNutritionRequest): NutritionLog
    suspend fun getTodayNutrition(userId: String): MealPlan

    suspend fun createPost(request: CreatePostRequest): Post
    suspend fun toggleLike(postId: String, userId: String): Map<String, Any>
    suspend fun getComments(postId: String): List<PostComment>
    suspend fun addComment(postId: String, request: AddCommentRequest): PostComment
    suspend fun getLeaderboard(): List<LeaderboardEntry>
    suspend fun getFeed(): List<Post>

    suspend fun getMessages(senderId: String, receiverId: String): List<Message>
    suspend fun sendMessage(senderId: String, receiverId: String, content: String): Message
    suspend fun getConversations(userId: String): List<ConversationSummary>

    suspend fun getRevenueKinetics(): List<RevenueDataPoint>
    suspend fun getPulse(): List<GymPulse>
    suspend fun getBusinessInsights(): BusinessInsights
    suspend fun getDefaulters(): List<Defaulter>

    suspend fun hello(): Map<String, String>
    suspend fun greet(name: String): Map<String, String>
}
