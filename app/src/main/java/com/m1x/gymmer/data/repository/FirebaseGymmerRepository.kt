package com.m1x.gymmer.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.m1x.gymmer.data.network.models.*
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebaseGymmerRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val rtdb: FirebaseDatabase = FirebaseDatabase.getInstance("https://gymmer-42987-default-rtdb.firebaseio.com/")
) : IGymmerRepository {

    override suspend fun login(loginRequest: LoginRequest): User {
        val result = auth.signInWithEmailAndPassword(loginRequest.email!!, loginRequest.password!!).await()
        val firebaseUser = result.user ?: throw Exception("Login failed")
        
        // Fetch user details from Firestore
        val doc = firestore.collection("users").document(firebaseUser.uid).get().await()
        return doc.toObject(User::class.java) ?: User(id = UUID.fromString(firebaseUser.uid), email = firebaseUser.email)
    }

    override suspend fun register(registerRequest: RegisterRequest): User {
        val result = auth.createUserWithEmailAndPassword(registerRequest.email!!, registerRequest.password!!).await()
        val firebaseUser = result.user ?: throw Exception("Registration failed")
        
        val user = User(
            id = UUID.randomUUID(), // Using random for internal model, but we'll link via UID in Firestore if needed
            name = registerRequest.name,
            email = registerRequest.email,
            phone = registerRequest.phone,
            role = registerRequest.role,
            gymId = registerRequest.gymId
        )
        
        firestore.collection("users").document(firebaseUser.uid).set(user).await()
        return user
    }

    override suspend fun refresh(refreshRequest: RefreshRequest): Map<String, String> {
        // Firebase handles token refresh automatically
        return mapOf("status" to "handled_by_firebase")
    }

    override suspend fun getProfile(userId: UUID): User {
        val doc = firestore.collection("users").document(userId.toString()).get().await()
        return doc.toObject(User::class.java) ?: throw Exception("User not found")
    }

    override suspend fun updateProfile(userId: UUID, request: UpdateProfileRequest): User {
        val userRef = firestore.collection("users").document(userId.toString())
        val updates = mutableMapOf<String, Any?>()
        request.name?.let { updates["name"] = it }
        request.phone?.let { updates["phone"] = it }
        request.bio?.let { updates["bio"] = it }
        request.avatarUrl?.let { updates["avatarUrl"] = it }
        request.goals?.let { updates["goals"] = it }
        
        userRef.update(updates).await()
        return getProfile(userId)
    }

    override suspend fun checkIn(userId: UUID): CheckIn {
        val checkIn = CheckIn(
            id = UUID.randomUUID(),
            userId = userId,
            checkedInAt = Date().toString()
        )
        firestore.collection("check_ins").add(checkIn).await()
        return checkIn
    }

    override suspend fun getDashboard(userId: UUID): DashboardData {
        val user = getProfile(userId)
        val checkInQuery = firestore.collection("check_ins")
            .whereEqualTo("userId", userId.toString())
            .orderBy("checkedInAt", Query.Direction.DESCENDING)
            .limit(1)
            .get().await()
        
        val lastCheckIn = checkInQuery.documents.firstOrNull()?.toObject(CheckIn::class.java)
        
        return DashboardData(
            user = user,
            todayCheckIn = lastCheckIn,
            activeSessionCount = 5 // Placeholder
        )
    }

    override suspend fun logWorkout(request: LogWorkoutRequest): WorkoutLog {
        val log = WorkoutLog(
            id = UUID.randomUUID(),
            userId = request.userId,
            workoutPlanId = request.workoutPlanId,
            durationMinutes = request.durationMinutes,
            notes = request.notes,
            loggedAt = Date().toString()
        )
        firestore.collection("workout_logs").add(log).await()
        return log
    }

    override suspend fun listWorkouts(): List<WorkoutPlan> {
        val snapshot = firestore.collection("workouts").get().await()
        return snapshot.toObjects(WorkoutPlan::class.java)
    }

    override suspend fun getWorkout(workoutId: UUID): WorkoutPlan {
        val doc = firestore.collection("workouts").document(workoutId.toString()).get().await()
        return doc.toObject(WorkoutPlan::class.java) ?: throw Exception("Workout not found")
    }

    override suspend fun listExercises(category: String?, difficulty: String?): List<Exercise> {
        var query: Query = firestore.collection("exercises")
        category?.let { query = query.whereEqualTo("category", it) }
        difficulty?.let { query = query.whereEqualTo("difficulty", it) }
        
        val snapshot = query.get().await()
        return snapshot.toObjects(Exercise::class.java)
    }

    override suspend fun getExercise(exerciseId: UUID): Exercise {
        val doc = firestore.collection("exercises").document(exerciseId.toString()).get().await()
        return doc.toObject(Exercise::class.java) ?: throw Exception("Exercise not found")
    }

    override suspend fun assignPlan(trainerId: UUID, traineeId: UUID, request: AssignPlanRequest): TraineePlan {
        val plan = TraineePlan(
            id = UUID.randomUUID(),
            traineeId = traineeId,
            trainerId = trainerId,
            planType = request.planType,
            planId = request.planId,
            assignedAt = Date().toString()
        )
        firestore.collection("trainee_plans").add(plan).await()
        return plan
    }

    override suspend fun assignTrainer(gymId: UUID, trainerId: UUID, traineeId: UUID): TrainerAssignment {
        val assignment = TrainerAssignment(
            id = UUID.randomUUID(),
            trainerId = trainerId,
            traineeId = traineeId,
            gymId = gymId,
            createdAt = Date().toString()
        )
        firestore.collection("trainer_assignments").add(assignment).await()
        return assignment
    }

    override suspend fun getAssignments(trainerId: UUID): List<TrainerAssignment> {
        val snapshot = firestore.collection("trainer_assignments")
            .whereEqualTo("trainerId", trainerId.toString())
            .get().await()
        return snapshot.toObjects(TrainerAssignment::class.java)
    }

    override suspend fun getTrainees(trainerId: UUID): List<User> {
        val assignments = getAssignments(trainerId)
        val traineeIds = assignments.map { it.traineeId.toString() }
        if (traineeIds.isEmpty()) return emptyList()
        
        val snapshot = firestore.collection("users")
            .whereIn("id", traineeIds)
            .get().await()
        return snapshot.toObjects(User::class.java)
    }

    override suspend fun getTraineeProgress(traineeId: UUID): TraineeProgress {
        val trainee = getProfile(traineeId)
        val checkIns = firestore.collection("check_ins")
            .whereEqualTo("userId", traineeId.toString())
            .get().await().toObjects(CheckIn::class.java)
        
        val logs = firestore.collection("workout_logs")
            .whereEqualTo("userId", traineeId.toString())
            .get().await().toObjects(WorkoutLog::class.java)
            
        return TraineeProgress(trainee, checkIns, logs)
    }

    override suspend fun getTrainersByGym(gymId: UUID): List<User> {
        val snapshot = firestore.collection("users")
            .whereEqualTo("gymId", gymId.toString())
            .whereEqualTo("role", "TRAINER")
            .get().await()
        return snapshot.toObjects(User::class.java)
    }

    override suspend fun getTrainerDashboard(trainerId: UUID): TrainerDashboardData {
        val trainer = getProfile(trainerId)
        val trainees = getTrainees(trainerId)
        return TrainerDashboardData(trainer, trainees.size, trainees)
    }

    override suspend fun getAllTrainers(): List<User> {
        val snapshot = firestore.collection("users")
            .whereEqualTo("role", "TRAINER")
            .get().await()
        return snapshot.toObjects(User::class.java)
    }

    override suspend fun deleteAssignment(assignmentId: UUID) {
        firestore.collection("trainer_assignments").document(assignmentId.toString()).delete().await()
    }

    override suspend fun logNutrition(request: LogNutritionRequest): NutritionLog {
        val log = NutritionLog(
            id = UUID.randomUUID(),
            userId = request.userId,
            type = request.type,
            amount = request.amount,
            loggedAt = Date().toString()
        )
        firestore.collection("nutrition_logs").add(log).await()
        return log
    }

    override suspend fun getTodayNutrition(userId: UUID): MealPlan {
        val snapshot = firestore.collection("meal_plans")
            .whereEqualTo("userId", userId.toString())
            .limit(1)
            .get().await()
        return snapshot.toObjects(MealPlan::class.java).firstOrNull() ?: throw Exception("No meal plan found")
    }

    override suspend fun createPost(request: CreatePostRequest): Post {
        val post = Post(
            id = UUID.randomUUID(),
            authorId = request.authorId,
            content = request.content,
            imageUrl = request.imageUrl,
            createdAt = Date().toString()
        )
        firestore.collection("posts").add(post).await()
        return post
    }

    override suspend fun toggleLike(postId: UUID, userId: UUID): Map<String, Any> {
        // Complex logic for toggle, simplified here
        return mapOf("status" to "liked")
    }

    override suspend fun getComments(postId: UUID): List<PostComment> {
        val snapshot = firestore.collection("comments")
            .whereEqualTo("postId", postId.toString())
            .get().await()
        return snapshot.toObjects(PostComment::class.java)
    }

    override suspend fun addComment(postId: UUID, request: AddCommentRequest): PostComment {
        val comment = PostComment(
            id = UUID.randomUUID(),
            postId = postId,
            authorId = request.authorId,
            content = request.content,
            createdAt = Date().toString()
        )
        firestore.collection("comments").add(comment).await()
        return comment
    }

    override suspend fun getLeaderboard(): List<LeaderboardEntry> {
        val snapshot = firestore.collection("leaderboard").get().await()
        return snapshot.toObjects(LeaderboardEntry::class.java)
    }

    override suspend fun getFeed(): List<Post> {
        val snapshot = firestore.collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get().await()
        return snapshot.toObjects(Post::class.java)
    }

    override suspend fun getMessages(senderId: UUID, receiverId: UUID): List<Message> {
        val chatPath = if (senderId.toString() < receiverId.toString()) "${senderId}_${receiverId}" else "${receiverId}_${senderId}"
        val snapshot = rtdb.getReference("chats").child(chatPath).get().await()
        return snapshot.children.mapNotNull { it.getValue(Message::class.java) }
    }

    override suspend fun sendMessage(senderId: UUID, receiverId: UUID, content: String): Message {
        val message = Message(
            id = UUID.randomUUID(),
            senderId = senderId,
            receiverId = receiverId,
            content = content,
            sentAt = Date().toString()
        )
        
        // Using Realtime Database for Chat Messages
        val chatPath = if (senderId.toString() < receiverId.toString()) "${senderId}_${receiverId}" else "${receiverId}_${senderId}"
        rtdb.getReference("chats").child(chatPath).push().setValue(message).await()

        return message
    }

    override suspend fun getConversations(userId: UUID): List<ConversationSummary> {
        // Complex query, simplified
        return emptyList()
    }

    override suspend fun getRevenueKinetics(): List<RevenueDataPoint> {
        val snapshot = firestore.collection("revenue").get().await()
        return snapshot.toObjects(RevenueDataPoint::class.java)
    }

    override suspend fun getPulse(): List<GymPulse> {
        val snapshot = firestore.collection("pulse").get().await()
        return snapshot.toObjects(GymPulse::class.java)
    }

    override suspend fun getBusinessInsights(): BusinessInsights {
        val doc = firestore.collection("business_stats").document("current").get().await()
        return doc.toObject(BusinessInsights::class.java) ?: BusinessInsights(0.0, 0.0, 0)
    }

    override suspend fun getDefaulters(): List<Defaulter> {
        val snapshot = firestore.collection("defaulters").get().await()
        return snapshot.toObjects(Defaulter::class.java)
    }

    override suspend fun hello(): Map<String, String> = mapOf("message" to "Hello from Firebase")
    override suspend fun greet(name: String): Map<String, String> = mapOf("message" to "Hello $name from Firebase")
}
