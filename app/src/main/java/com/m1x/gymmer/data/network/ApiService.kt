package com.m1x.gymmer.data.network

import com.m1x.gymmer.data.network.models.*
import retrofit2.http.*
import java.util.UUID

interface ApiService {

    // Auth
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): User

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): User

    @POST("api/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): Map<String, String>

    // User
    @GET("api/user/profile")
    suspend fun getProfile(@Query("userId") userId: UUID): User

    @PUT("api/user/profile")
    suspend fun updateProfile(@Query("userId") userId: UUID, @Body request: UpdateProfileRequest): User

    @POST("api/user/check-in")
    suspend fun checkIn(@Query("userId") userId: UUID): CheckIn

    @GET("api/user/dashboard")
    suspend fun getDashboard(@Query("userId") userId: UUID): DashboardData

    // Workouts
    @POST("api/workouts/log")
    suspend fun logWorkout(@Body request: LogWorkoutRequest): WorkoutLog

    @GET("api/workouts")
    suspend fun listWorkouts(): List<WorkoutPlan>

    @GET("api/workouts/{id}")
    suspend fun getWorkout(@Path("id") id: UUID): WorkoutPlan

    @GET("api/exercises")
    suspend fun listExercises(
        @Query("category") category: String? = null,
        @Query("difficulty") difficulty: String? = null
    ): List<Exercise>

    @GET("api/exercises/{id}")
    suspend fun getExercise(@Path("id") id: UUID): Exercise

    // Trainers
    @POST("api/trainers/trainees/{id}/plan")
    suspend fun assignPlan(
        @Path("id") traineeId: UUID,
        @Query("trainerId") trainerId: UUID,
        @Body request: AssignPlanRequest
    ): TraineePlan

    @POST("api/trainers/assign")
    suspend fun assignTrainer(
        @Query("trainerId") trainerId: UUID,
        @Query("traineeId") traineeId: UUID,
        @Query("gymId") gymId: UUID
    ): TrainerAssignment

    @GET("api/trainers/{trainerId}/assignments")
    suspend fun getAssignments(@Path("trainerId") trainerId: UUID): List<TrainerAssignment>

    @GET("api/trainers/trainees")
    suspend fun getTrainees(@Query("trainerId") trainerId: UUID): List<User>

    @GET("api/trainers/trainees/{id}/progress")
    suspend fun getTraineeProgress(@Path("id") traineeId: UUID): TraineeProgress

    @GET("api/trainers/gym/{gymId}")
    suspend fun getTrainersByGym(@Path("gymId") gymId: UUID): List<User>

    @GET("api/trainers/dashboard")
    suspend fun getTrainerDashboard(@Query("trainerId") trainerId: UUID): TrainerDashboardData

    @GET("api/trainers/all")
    suspend fun getAllTrainers(): List<User>

    @DELETE("api/trainers/assignment/{id}")
    suspend fun deleteAssignment(@Path("id") assignmentId: UUID)

    // Nutrition
    @POST("api/nutrition/log")
    suspend fun logNutrition(@Body request: LogNutritionRequest): NutritionLog

    @GET("api/nutrition/today")
    suspend fun getTodayNutrition(@Query("userId") userId: UUID): MealPlan

    // Community
    @POST("api/community/posts")
    suspend fun createPost(@Body request: CreatePostRequest): Post

    @POST("api/community/posts/{id}/like")
    suspend fun toggleLike(@Path("id") postId: UUID, @Query("userId") userId: UUID): Map<String, Any>

    @GET("api/community/posts/{id}/comments")
    suspend fun getComments(@Path("id") postId: UUID): List<PostComment>

    @POST("api/community/posts/{id}/comments")
    suspend fun addComment(@Path("id") postId: UUID, @Body request: AddCommentRequest): PostComment

    @GET("api/community/leaderboard")
    suspend fun getLeaderboard(): List<LeaderboardEntry>

    @GET("api/community/feed")
    suspend fun getFeed(): List<Post>

    // Chat
    @GET("api/chat/{userId}/messages")
    suspend fun getMessages(
        @Path("userId") otherUserId: UUID,
        @Query("currentUserId") currentUserId: UUID
    ): List<Message>

    @POST("api/chat/{userId}/messages")
    suspend fun sendMessage(
        @Path("userId") receiverId: UUID,
        @Query("senderId") senderId: UUID,
        @Body request: SendMessageRequest
    ): Message

    @GET("api/chat/conversations")
    suspend fun getConversations(@Query("userId") userId: UUID): List<ConversationSummary>

    // Business
    @GET("api/business/revenue-kinetics")
    suspend fun getRevenueKinetics(): List<RevenueDataPoint>

    @GET("api/business/pulse")
    suspend fun getPulse(): List<GymPulse>

    @GET("api/business/insights")
    suspend fun getBusinessInsights(): BusinessInsights

    @GET("api/business/defaulters")
    suspend fun getDefaulters(): List<Defaulter>

    // Hello
    @GET("api/hello")
    suspend fun hello(): Map<String, String>

    @GET("api/greet/{name}")
    suspend fun greet(@Path("name") name: String): Map<String, String>
}
