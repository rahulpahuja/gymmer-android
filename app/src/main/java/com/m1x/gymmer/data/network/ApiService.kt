package com.m1x.gymmer.data.network

import com.m1x.gymmer.data.network.models.*
import retrofit2.http.*

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): User

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): User

    @POST("auth/refresh")
    suspend fun refresh(@Body refreshRequest: RefreshRequest): Map<String, String>

    @GET("user/profile")
    suspend fun getProfile(@Query("userId") userId: String): User

    @PUT("user/profile")
    suspend fun updateProfile(@Query("userId") userId: String, @Body request: UpdateProfileRequest): User

    @POST("user/check-in")
    suspend fun checkIn(@Query("userId") userId: String): CheckIn

    @GET("user/dashboard")
    suspend fun getDashboard(@Query("userId") userId: String): DashboardData

    @POST("workouts/log")
    suspend fun logWorkout(@Body request: LogWorkoutRequest): WorkoutLog

    @GET("workouts")
    suspend fun listWorkouts(): List<WorkoutPlan>

    @GET("workouts/{id}")
    suspend fun getWorkout(@Path("id") workoutId: String): WorkoutPlan

    @GET("exercises")
    suspend fun listExercises(
        @Query("category") category: String? = null,
        @Query("difficulty") difficulty: String? = null
    ): List<Exercise>

    @GET("exercises/{id}")
    suspend fun getExercise(@Path("id") exerciseId: String): Exercise

    @POST("trainer/trainees/{id}/plan")
    suspend fun assignPlan(
        @Path("id") traineeId: String,
        @Query("trainerId") trainerId: String,
        @Body request: AssignPlanRequest
    ): TraineePlan

    @POST("gyms/{gymId}/trainers/{trainerId}/assign/{traineeId}")
    suspend fun assignTrainer(
        @Path("gymId") gymId: String,
        @Path("trainerId") trainerId: String,
        @Path("traineeId") traineeId: String
    ): TrainerAssignment

    @GET("trainer/assignments")
    suspend fun getAssignments(@Query("trainerId") trainerId: String): List<TrainerAssignment>

    @GET("trainer/trainees")
    suspend fun getTrainees(@Query("trainerId") trainerId: String): List<User>

    @GET("trainer/trainees/{id}/progress")
    suspend fun getTraineeProgress(@Path("id") traineeId: String): TraineeProgress

    @GET("gyms/{gymId}/trainers")
    suspend fun getTrainersByGym(@Path("gymId") gymId: String): List<User>

    @GET("trainer/dashboard")
    suspend fun getTrainerDashboard(@Query("trainerId") trainerId: String): TrainerDashboardData

    @GET("trainers")
    suspend fun getAllTrainers(): List<User>

    @DELETE("trainer/assignments/{id}")
    suspend fun deleteAssignment(@Path("id") assignmentId: String)

    @POST("nutrition/log")
    suspend fun logNutrition(@Body request: LogNutritionRequest): NutritionLog

    @GET("nutrition/today")
    suspend fun getTodayNutrition(@Query("userId") userId: String): MealPlan

    @POST("community/posts")
    suspend fun createPost(@Body request: CreatePostRequest): Post

    @POST("community/posts/{postId}/like")
    suspend fun toggleLike(@Path("postId") postId: String, @Query("userId") userId: String): Map<String, Any>

    @GET("community/posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: String): List<PostComment>

    @POST("community/posts/{postId}/comments")
    suspend fun addComment(@Path("postId") postId: String, @Body request: AddCommentRequest): PostComment

    @GET("community/leaderboard")
    suspend fun getLeaderboard(): List<LeaderboardEntry>

    @GET("community/feed")
    suspend fun getFeed(): List<Post>

    @GET("chat/{receiverId}/messages")
    suspend fun getMessages(
        @Path("receiverId") receiverId: String,
        @Query("senderId") senderId: String
    ): List<Message>

    @POST("chat/{receiverId}/messages")
    suspend fun sendMessage(
        @Path("receiverId") receiverId: String,
        @Query("senderId") senderId: String,
        @Body request: SendMessageRequest
    ): Message

    @GET("chat/conversations")
    suspend fun getConversations(@Query("userId") userId: String): List<ConversationSummary>

    @GET("business/revenue-kinetics")
    suspend fun getRevenueKinetics(): List<RevenueDataPoint>

    @GET("business/pulse")
    suspend fun getPulse(): List<GymPulse>

    @GET("business/insights")
    suspend fun getBusinessInsights(): BusinessInsights

    @GET("business/defaulters")
    suspend fun getDefaulters(): List<Defaulter>

    @POST("payments/process")
    suspend fun processPayment(@Body request: PaymentRequest): PaymentResponse

    @PUT("notifications/config")
    suspend fun updateNotificationConfig(@Body config: NotificationConfig)

    @GET("notifications/config")
    suspend fun getNotificationConfig(@Query("userId") userId: String): NotificationConfig

    @GET("hello")
    suspend fun hello(): Map<String, String>

    @GET("greet")
    suspend fun greet(@Query("name") name: String): Map<String, String>
}
