package com.m1x.gymmer.data.repository

import com.m1x.gymmer.data.network.ApiService
import com.m1x.gymmer.data.network.models.*
import java.util.UUID

class GymmerRepository(private val apiService: ApiService) {

    // Auth
    suspend fun login(request: LoginRequest): User = apiService.login(request)
    suspend fun register(request: RegisterRequest): User = apiService.register(request)
    suspend fun refresh(request: RefreshRequest): Map<String, String> = apiService.refresh(request)

    // User
    suspend fun getProfile(userId: UUID): User = apiService.getProfile(userId)
    suspend fun updateProfile(userId: UUID, request: UpdateProfileRequest): User = apiService.updateProfile(userId, request)
    suspend fun checkIn(userId: UUID): CheckIn = apiService.checkIn(userId)
    suspend fun getDashboard(userId: UUID): DashboardData = apiService.getDashboard(userId)

    // Workouts
    suspend fun logWorkout(request: LogWorkoutRequest): WorkoutLog = apiService.logWorkout(request)
    suspend fun listWorkouts(): List<WorkoutPlan> = apiService.listWorkouts()
    suspend fun getWorkout(id: UUID): WorkoutPlan = apiService.getWorkout(id)
    suspend fun listExercises(category: String? = null, difficulty: String? = null): List<Exercise> = apiService.listExercises(category, difficulty)
    suspend fun getExercise(id: UUID): Exercise = apiService.getExercise(id)

    // Trainers
    suspend fun assignPlan(traineeId: UUID, trainerId: UUID, request: AssignPlanRequest): TraineePlan = apiService.assignPlan(traineeId, trainerId, request)
    suspend fun assignTrainer(trainerId: UUID, traineeId: UUID, gymId: UUID): TrainerAssignment = apiService.assignTrainer(trainerId, traineeId, gymId)
    suspend fun getAssignments(trainerId: UUID): List<TrainerAssignment> = apiService.getAssignments(trainerId)
    suspend fun getTrainees(trainerId: UUID): List<User> = apiService.getTrainees(trainerId)
    suspend fun getTraineeProgress(traineeId: UUID): TraineeProgress = apiService.getTraineeProgress(traineeId)
    suspend fun getTrainersByGym(gymId: UUID): List<User> = apiService.getTrainersByGym(gymId)
    suspend fun getTrainerDashboard(trainerId: UUID): TrainerDashboardData = apiService.getTrainerDashboard(trainerId)
    suspend fun getAllTrainers(): List<User> = apiService.getAllTrainers()
    suspend fun deleteAssignment(assignmentId: UUID) = apiService.deleteAssignment(assignmentId)

    // Nutrition
    suspend fun logNutrition(request: LogNutritionRequest): NutritionLog = apiService.logNutrition(request)
    suspend fun getTodayNutrition(userId: UUID): MealPlan = apiService.getTodayNutrition(userId)

    // Community
    suspend fun createPost(request: CreatePostRequest): Post = apiService.createPost(request)
    suspend fun toggleLike(postId: UUID, userId: UUID) = apiService.toggleLike(postId, userId)
    suspend fun getComments(postId: UUID): List<PostComment> = apiService.getComments(postId)
    suspend fun addComment(postId: UUID, request: AddCommentRequest): PostComment = apiService.addComment(postId, request)
    suspend fun getLeaderboard(): List<LeaderboardEntry> = apiService.getLeaderboard()
    suspend fun getFeed(): List<Post> = apiService.getFeed()

    // Chat
    suspend fun getMessages(otherUserId: UUID, currentUserId: UUID): List<Message> = apiService.getMessages(otherUserId, currentUserId)
    suspend fun sendMessage(receiverId: UUID, senderId: UUID, content: String): Message {
        return apiService.sendMessage(receiverId, senderId, SendMessageRequest(content))
    }
    suspend fun getConversations(userId: UUID): List<ConversationSummary> = apiService.getConversations(userId)

    // Business
    suspend fun getRevenueKinetics(): List<RevenueDataPoint> = apiService.getRevenueKinetics()
    suspend fun getPulse(): List<GymPulse> = apiService.getPulse()
    suspend fun getBusinessInsights(): BusinessInsights = apiService.getBusinessInsights()
    suspend fun getDefaulters(): List<Defaulter> = apiService.getDefaulters()

    // Hello
    suspend fun hello(): Map<String, String> = apiService.hello()
    suspend fun greet(name: String): Map<String, String> = apiService.greet(name)
}
