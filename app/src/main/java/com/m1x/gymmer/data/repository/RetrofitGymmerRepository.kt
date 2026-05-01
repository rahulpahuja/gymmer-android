package com.m1x.gymmer.data.repository

import com.m1x.gymmer.data.network.ApiService
import com.m1x.gymmer.data.network.models.*

class RetrofitGymmerRepository(private val apiService: ApiService) : IGymmerRepository {

    // Auth
    override suspend fun login(loginRequest: LoginRequest): User = apiService.login(loginRequest)
    override suspend fun register(registerRequest: RegisterRequest): User = apiService.register(registerRequest)
    override suspend fun refresh(refreshRequest: RefreshRequest): Map<String, String> = apiService.refresh(refreshRequest)

    // User
    override suspend fun getProfile(userId: String): User = apiService.getProfile(userId)
    override suspend fun updateProfile(userId: String, request: UpdateProfileRequest): User = apiService.updateProfile(userId, request)
    override suspend fun checkIn(userId: String): CheckIn = apiService.checkIn(userId)
    override suspend fun getDashboard(userId: String): DashboardData = apiService.getDashboard(userId)

    // Workouts
    override suspend fun logWorkout(request: LogWorkoutRequest): WorkoutLog = apiService.logWorkout(request)
    override suspend fun listWorkouts(): List<WorkoutPlan> = apiService.listWorkouts()
    override suspend fun getWorkout(workoutId: String): WorkoutPlan = apiService.getWorkout(workoutId)
    override suspend fun listExercises(category: String?, difficulty: String?): List<Exercise> = apiService.listExercises(category, difficulty)
    override suspend fun getExercise(exerciseId: String): Exercise = apiService.getExercise(exerciseId)

    // Trainers
    override suspend fun assignPlan(trainerId: String, traineeId: String, request: AssignPlanRequest): TraineePlan = apiService.assignPlan(traineeId, trainerId, request)
    override suspend fun assignTrainer(gymId: String, trainerId: String, traineeId: String): TrainerAssignment = apiService.assignTrainer(gymId, trainerId, traineeId)
    override suspend fun getAssignments(trainerId: String): List<TrainerAssignment> = apiService.getAssignments(trainerId)
    override suspend fun getTrainees(trainerId: String): List<User> = apiService.getTrainees(trainerId)
    override suspend fun getTraineeProgress(traineeId: String): TraineeProgress = apiService.getTraineeProgress(traineeId)
    override suspend fun getTrainersByGym(gymId: String): List<User> = apiService.getTrainersByGym(gymId)
    override suspend fun getTrainerDashboard(trainerId: String): TrainerDashboardData = apiService.getTrainerDashboard(trainerId)
    override suspend fun getAllTrainers(): List<User> = apiService.getAllTrainers()
    override suspend fun deleteAssignment(assignmentId: String) = apiService.deleteAssignment(assignmentId)

    // Nutrition
    override suspend fun logNutrition(request: LogNutritionRequest): NutritionLog = apiService.logNutrition(request)
    override suspend fun getTodayNutrition(userId: String): MealPlan = apiService.getTodayNutrition(userId)

    // Community
    override suspend fun createPost(request: CreatePostRequest): Post = apiService.createPost(request)
    override suspend fun toggleLike(postId: String, userId: String): Map<String, Any> = apiService.toggleLike(postId, userId)
    override suspend fun getComments(postId: String): List<PostComment> = apiService.getComments(postId)
    override suspend fun addComment(postId: String, request: AddCommentRequest): PostComment = apiService.addComment(postId, request)
    override suspend fun getLeaderboard(): List<LeaderboardEntry> = apiService.getLeaderboard()
    override suspend fun getFeed(): List<Post> = apiService.getFeed()

    // Chat
    override suspend fun getMessages(senderId: String, receiverId: String): List<Message> = apiService.getMessages(senderId, receiverId)
    override suspend fun sendMessage(senderId: String, receiverId: String, content: String): Message {
        return apiService.sendMessage(senderId, receiverId, SendMessageRequest(content))
    }
    override suspend fun getConversations(userId: String): List<ConversationSummary> = apiService.getConversations(userId)

    // Business
    override suspend fun getRevenueKinetics(): List<RevenueDataPoint> = apiService.getRevenueKinetics()
    override suspend fun getPulse(): List<GymPulse> = apiService.getPulse()
    override suspend fun getBusinessInsights(): BusinessInsights = apiService.getBusinessInsights()
    override suspend fun getDefaulters(): List<Defaulter> = apiService.getDefaulters()

    // Hello
    override suspend fun hello(): Map<String, String> = apiService.hello()
    override suspend fun greet(name: String): Map<String, String> = apiService.greet(name)
}
