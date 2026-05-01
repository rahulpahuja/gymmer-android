package com.m1x.gymmer.data.repository

import com.m1x.gymmer.data.network.ApiService
import com.m1x.gymmer.data.network.models.*
import java.util.UUID

class RetrofitGymmerRepository(private val apiService: ApiService) : IGymmerRepository {

    // Auth
    override suspend fun login(loginRequest: LoginRequest): User = apiService.login(loginRequest)
    override suspend fun register(registerRequest: RegisterRequest): User = apiService.register(registerRequest)
    override suspend fun refresh(refreshRequest: RefreshRequest): Map<String, String> = apiService.refresh(refreshRequest)

    // User
    override suspend fun getProfile(userId: UUID): User = apiService.getProfile(userId)
    override suspend fun updateProfile(userId: UUID, request: UpdateProfileRequest): User = apiService.updateProfile(userId, request)
    override suspend fun checkIn(userId: UUID): CheckIn = apiService.checkIn(userId)
    override suspend fun getDashboard(userId: UUID): DashboardData = apiService.getDashboard(userId)

    // Workouts
    override suspend fun logWorkout(request: LogWorkoutRequest): WorkoutLog = apiService.logWorkout(request)
    override suspend fun listWorkouts(): List<WorkoutPlan> = apiService.listWorkouts()
    override suspend fun getWorkout(workoutId: UUID): WorkoutPlan = apiService.getWorkout(workoutId)
    override suspend fun listExercises(category: String?, difficulty: String?): List<Exercise> = apiService.listExercises(category, difficulty)
    override suspend fun getExercise(exerciseId: UUID): Exercise = apiService.getExercise(exerciseId)

    // Trainers
    override suspend fun assignPlan(trainerId: UUID, traineeId: UUID, request: AssignPlanRequest): TraineePlan = apiService.assignPlan(trainerId, traineeId, request)
    override suspend fun assignTrainer(gymId: UUID, trainerId: UUID, traineeId: UUID): TrainerAssignment = apiService.assignTrainer(gymId, trainerId, traineeId)
    override suspend fun getAssignments(trainerId: UUID): List<TrainerAssignment> = apiService.getAssignments(trainerId)
    override suspend fun getTrainees(trainerId: UUID): List<User> = apiService.getTrainees(trainerId)
    override suspend fun getTraineeProgress(traineeId: UUID): TraineeProgress = apiService.getTraineeProgress(traineeId)
    override suspend fun getTrainersByGym(gymId: UUID): List<User> = apiService.getTrainersByGym(gymId)
    override suspend fun getTrainerDashboard(trainerId: UUID): TrainerDashboardData = apiService.getTrainerDashboard(trainerId)
    override suspend fun getAllTrainers(): List<User> = apiService.getAllTrainers()
    override suspend fun deleteAssignment(assignmentId: UUID) = apiService.deleteAssignment(assignmentId)

    // Nutrition
    override suspend fun logNutrition(request: LogNutritionRequest): NutritionLog = apiService.logNutrition(request)
    override suspend fun getTodayNutrition(userId: UUID): MealPlan = apiService.getTodayNutrition(userId)

    // Community
    override suspend fun createPost(request: CreatePostRequest): Post = apiService.createPost(request)
    override suspend fun toggleLike(postId: UUID, userId: UUID) = apiService.toggleLike(postId, userId)
    override suspend fun getComments(postId: UUID): List<PostComment> = apiService.getComments(postId)
    override suspend fun addComment(postId: UUID, request: AddCommentRequest): PostComment = apiService.addComment(postId, request)
    override suspend fun getLeaderboard(): List<LeaderboardEntry> = apiService.getLeaderboard()
    override suspend fun getFeed(): List<Post> = apiService.getFeed()

    // Chat
    override suspend fun getMessages(senderId: UUID, receiverId: UUID): List<Message> = apiService.getMessages(senderId, receiverId)
    override suspend fun sendMessage(senderId: UUID, receiverId: UUID, content: String): Message {
        return apiService.sendMessage(senderId, receiverId, SendMessageRequest(content))
    }
    override suspend fun getConversations(userId: UUID): List<ConversationSummary> = apiService.getConversations(userId)

    // Business
    override suspend fun getRevenueKinetics(): List<RevenueDataPoint> = apiService.getRevenueKinetics()
    override suspend fun getPulse(): List<GymPulse> = apiService.getPulse()
    override suspend fun getBusinessInsights(): BusinessInsights = apiService.getBusinessInsights()
    override suspend fun getDefaulters(): List<Defaulter> = apiService.getDefaulters()

    // Hello
    override suspend fun hello(): Map<String, String> = apiService.hello()
    override suspend fun greet(name: String): Map<String, String> = apiService.greet(name)
}
