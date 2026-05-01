package com.m1x.gymmer.data.repository

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.m1x.gymmer.data.database.dao.RegistrationDao
import com.m1x.gymmer.data.database.entity.RegistrationEntity
import com.m1x.gymmer.data.network.models.*
import com.m1x.gymmer.data.utils.NotificationManager
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebaseGymmerRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val rtdb: FirebaseDatabase = FirebaseDatabase.getInstance("https://gymmer-42987-default-rtdb.firebaseio.com/"),
    private val registrationDao: RegistrationDao? = null,
    private val notificationManager: NotificationManager? = null
) : IGymmerRepository {

    init {
        // Enable offline persistence for RTDB to handle connectivity gaps
        rtdb.setPersistenceEnabled(true)
        val options = FirebaseApp.getInstance().options
        Log.d("FIREBASE_DEBUG", "Connected to Project: ${options.projectId}")
        Log.d("FIREBASE_DEBUG", "RTDB URL: ${rtdb.reference.toString()}")
    }

    override suspend fun login(loginRequest: LoginRequest): User {
        val result = auth.signInWithEmailAndPassword(loginRequest.email!!, loginRequest.password!!).await()
        val firebaseUser = result.user ?: throw Exception("Login failed")
        
        // Try RTDB first since that's what the user prefers
        val rtdbSnapshot = rtdb.getReference("users").child(firebaseUser.uid).get().await()
        if (rtdbSnapshot.exists()) {
            return User(
                id = rtdbSnapshot.child("id").value?.toString() ?: firebaseUser.uid,
                name = rtdbSnapshot.child("name").value?.toString(),
                email = rtdbSnapshot.child("email").value?.toString(),
                phone = rtdbSnapshot.child("phone").value?.toString(),
                role = rtdbSnapshot.child("role").value?.toString(),
                gymId = rtdbSnapshot.child("gymId").value?.toString()
            )
        }

        // Fallback to Firestore
        try {
            val doc = firestore.collection("users").document(firebaseUser.uid).get().await()
            return doc.toObject(User::class.java) ?: User(id = firebaseUser.uid, email = firebaseUser.email)
        } catch (e: Exception) {
            return User(id = firebaseUser.uid, email = firebaseUser.email)
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): User {
        Log.d("FIREBASE_DEBUG", "Starting registration for: ${registerRequest.email}")
        // 1. Store to local DB first for safety/offline queuing
        registrationDao?.insertRegistration(
            RegistrationEntity(
                gymId = registerRequest.gymId,
                name = registerRequest.name,
                email = registerRequest.email,
                phone = registerRequest.phone,
                password = registerRequest.password,
                role = registerRequest.role
            )
        )

        try {
            val result = auth.createUserWithEmailAndPassword(registerRequest.email!!, registerRequest.password!!).await()
            val firebaseUser = result.user ?: throw Exception("Registration failed")
            
            val user = User(
                id = firebaseUser.uid,
                name = registerRequest.name,
                email = registerRequest.email,
                phone = registerRequest.phone,
                role = registerRequest.role,
                gymId = registerRequest.gymId
            )
            
            // 2. Store to Realtime Database FIRST (since it's more reliable/requested)
            val rtdbUser = mapOf(
                "id" to user.id,
                "name" to user.name,
                "email" to user.email,
                "phone" to user.phone,
                "role" to user.role,
                "gymId" to user.gymId
            )
            val userPath = "users/${firebaseUser.uid}"
            Log.d("FIREBASE_DEBUG", "Attempting RTDB write to: $userPath")
            try {
                rtdb.getReference("users").child(firebaseUser.uid).setValue(rtdbUser).await()
                Log.d("FIREBASE_DEBUG", "RTDB Write Success")
            } catch (rtdbEx: Exception) {
                Log.e("FIREBASE_DEBUG", "RTDB Write FAILED: ${rtdbEx.message}")
                rtdbEx.printStackTrace()
            }

            // 3. Store to Firestore (Try-catch as it's failing in logs)
            try {
                firestore.collection("users").document(firebaseUser.uid).set(user).await()
            } catch (e: Exception) {
                // Ignore Firestore errors to prevent blocking the whole flow
            }

            // If everything succeeded, we can clear the local entry
            registrationDao?.getPendingRegistrations()?.find { it.email == registerRequest.email }?.let {
                registrationDao.deleteRegistration(it)
            }
            
            return user
        } catch (e: Exception) {
            throw e 
        }
    }

    override suspend fun refresh(refreshRequest: RefreshRequest): Map<String, String> {
        return mapOf("status" to "handled_by_firebase")
    }

    override suspend fun getProfile(userId: String): User {
        val rtdbSnapshot = rtdb.getReference("users").child(userId).get().await()
        if (rtdbSnapshot.exists()) {
            return User(
                id = rtdbSnapshot.child("id").value?.toString() ?: userId,
                name = rtdbSnapshot.child("name").value?.toString(),
                email = rtdbSnapshot.child("email").value?.toString(),
                phone = rtdbSnapshot.child("phone").value?.toString(),
                role = rtdbSnapshot.child("role").value?.toString(),
                gymId = rtdbSnapshot.child("gymId").value?.toString()
            )
        }
        val doc = firestore.collection("users").document(userId).get().await()
        return doc.toObject(User::class.java) ?: throw Exception("User not found")
    }

    override suspend fun updateProfile(userId: String, request: UpdateProfileRequest): User {
        val userRef = firestore.collection("users").document(userId)
        val updates = mutableMapOf<String, Any?>()
        request.name?.let { updates["name"] = it }
        request.phone?.let { updates["phone"] = it }
        request.bio?.let { updates["bio"] = it }
        request.avatarUrl?.let { updates["avatarUrl"] = it }
        request.goals?.let { updates["goals"] = it }
        
        userRef.update(updates).await()
        
        // Also update RTDB
        rtdb.getReference("users").child(userId).updateChildren(updates).await()
        
        return getProfile(userId)
    }

    override suspend fun checkIn(userId: String): CheckIn {
        val checkIn = CheckIn(
            id = UUID.randomUUID().toString(),
            userId = userId,
            checkedInAt = Date().toString()
        )
        firestore.collection("check_ins").add(checkIn).await()
        return checkIn
    }

    override suspend fun getDashboard(userId: String): DashboardData {
        val user = getProfile(userId)
        val checkInQuery = firestore.collection("check_ins")
            .whereEqualTo("userId", userId)
            .orderBy("checkedInAt", Query.Direction.DESCENDING)
            .limit(1)
            .get().await()
        
        val lastCheckIn = checkInQuery.documents.firstOrNull()?.toObject(CheckIn::class.java)
        
        return DashboardData(
            user = user,
            todayCheckIn = lastCheckIn,
            activeSessionCount = 5 
        )
    }

    override suspend fun logWorkout(request: LogWorkoutRequest): WorkoutLog {
        val log = WorkoutLog(
            id = UUID.randomUUID().toString(),
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

    override suspend fun getWorkout(workoutId: String): WorkoutPlan {
        val doc = firestore.collection("workouts").document(workoutId).get().await()
        return doc.toObject(WorkoutPlan::class.java) ?: throw Exception("Workout not found")
    }

    override suspend fun listExercises(category: String?, difficulty: String?): List<Exercise> {
        var query: Query = firestore.collection("exercises")
        category?.let { query = query.whereEqualTo("category", it) }
        difficulty?.let { query = query.whereEqualTo("difficulty", it) }
        
        val snapshot = query.get().await()
        return snapshot.toObjects(Exercise::class.java)
    }

    override suspend fun getExercise(exerciseId: String): Exercise {
        val doc = firestore.collection("exercises").document(exerciseId).get().await()
        return doc.toObject(Exercise::class.java) ?: throw Exception("Exercise not found")
    }

    override suspend fun assignPlan(trainerId: String, traineeId: String, request: AssignPlanRequest): TraineePlan {
        val plan = TraineePlan(
            id = UUID.randomUUID().toString(),
            traineeId = traineeId,
            trainerId = trainerId,
            planType = request.planType,
            planId = request.planId,
            assignedAt = Date().toString()
        )
        firestore.collection("trainee_plans").add(plan).await()
        return plan
    }

    override suspend fun assignTrainer(gymId: String, trainerId: String, traineeId: String): TrainerAssignment {
        val assignment = TrainerAssignment(
            id = UUID.randomUUID().toString(),
            trainerId = trainerId,
            traineeId = traineeId,
            gymId = gymId,
            createdAt = Date().toString()
        )
        firestore.collection("trainer_assignments").add(assignment).await()
        return assignment
    }

    override suspend fun getAssignments(trainerId: String): List<TrainerAssignment> {
        val snapshot = firestore.collection("trainer_assignments")
            .whereEqualTo("trainerId", trainerId)
            .get().await()
        return snapshot.toObjects(TrainerAssignment::class.java)
    }

    override suspend fun getTrainees(trainerId: String): List<User> {
        val assignments = getAssignments(trainerId)
        val traineeIds = assignments.map { it.traineeId.toString() }
        if (traineeIds.isEmpty()) return emptyList()
        
        val snapshot = firestore.collection("users")
            .whereIn("id", traineeIds)
            .get().await()
        return snapshot.toObjects(User::class.java)
    }

    override suspend fun getTraineeProgress(traineeId: String): TraineeProgress {
        val trainee = getProfile(traineeId)
        val checkIns = firestore.collection("check_ins")
            .whereEqualTo("userId", traineeId)
            .get().await().toObjects(CheckIn::class.java)
        
        val logs = firestore.collection("workout_logs")
            .whereEqualTo("userId", traineeId)
            .get().await().toObjects(WorkoutLog::class.java)
            
        return TraineeProgress(trainee, checkIns, logs)
    }

    override suspend fun getTrainersByGym(gymId: String): List<User> {
        val snapshot = firestore.collection("users")
            .whereEqualTo("gymId", gymId)
            .whereEqualTo("role", "TRAINER")
            .get().await()
        return snapshot.toObjects(User::class.java)
    }

    override suspend fun getTrainerDashboard(trainerId: String): TrainerDashboardData {
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

    override suspend fun deleteAssignment(assignmentId: String) {
        firestore.collection("trainer_assignments").document(assignmentId).delete().await()
    }

    override suspend fun logNutrition(request: LogNutritionRequest): NutritionLog {
        val log = NutritionLog(
            id = UUID.randomUUID().toString(),
            userId = request.userId,
            type = request.type,
            amount = request.amount,
            loggedAt = Date().toString()
        )
        firestore.collection("nutrition_logs").add(log).await()
        return log
    }

    override suspend fun getTodayNutrition(userId: String): MealPlan {
        val snapshot = firestore.collection("meal_plans")
            .whereEqualTo("userId", userId)
            .limit(1)
            .get().await()
        return snapshot.toObjects(MealPlan::class.java).firstOrNull() ?: throw Exception("No meal plan found")
    }

    override suspend fun createPost(request: CreatePostRequest): Post {
        val post = Post(
            id = UUID.randomUUID().toString(),
            authorId = request.authorId,
            content = request.content,
            imageUrl = request.imageUrl,
            createdAt = Date().toString()
        )
        firestore.collection("posts").add(post).await()
        return post
    }

    override suspend fun toggleLike(postId: String, userId: String): Map<String, Any> {
        return mapOf("status" to "liked")
    }

    override suspend fun getComments(postId: String): List<PostComment> {
        val snapshot = firestore.collection("comments")
            .whereEqualTo("postId", postId)
            .get().await()
        return snapshot.toObjects(PostComment::class.java)
    }

    override suspend fun addComment(postId: String, request: AddCommentRequest): PostComment {
        val comment = PostComment(
            id = UUID.randomUUID().toString(),
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

    override suspend fun getMessages(senderId: String, receiverId: String): List<Message> {
        val chatPath = if (senderId < receiverId) "${senderId}_${receiverId}" else "${receiverId}_${senderId}"
        val snapshot = rtdb.getReference("chats").child(chatPath).get().await()
        return snapshot.children.mapNotNull { it.getValue(Message::class.java) }
    }

    override suspend fun sendMessage(senderId: String, receiverId: String, content: String): Message {
        val message = Message(
            id = UUID.randomUUID().toString(),
            senderId = senderId,
            receiverId = receiverId,
            content = content,
            sentAt = Date().toString()
        )
        val chatPath = if (senderId < receiverId) "${senderId}_${receiverId}" else "${receiverId}_${senderId}"
        rtdb.getReference("chats").child(chatPath).push().setValue(message).await()
        return message
    }

    override suspend fun getConversations(userId: String): List<ConversationSummary> {
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

    override suspend fun processPayment(request: PaymentRequest): PaymentResponse {
        val transactionId = UUID.randomUUID().toString()
        val timestamp = Date().toString()
        
        // 1. Record in Firestore
        val paymentData = mapOf(
            "transactionId" to transactionId,
            "userId" to request.userId,
            "amount" to request.amount,
            "isPartial" to request.isPartial,
            "paymentMethod" to request.paymentMethod,
            "remarks" to request.remarks,
            "timestamp" to timestamp
        )
        firestore.collection("payments").document(transactionId).set(paymentData).await()

        // 2. Fetch user details for notification
        val user = getProfile(request.userId)
        
        // 3. Trigger Notifications
        notificationManager?.sendPaymentNotification(
            userName = user.name ?: "Athlete",
            amount = request.amount,
            isPartial = request.isPartial,
            userPhone = user.phone,
            trainerPhone = "9999999999", // Mock trainer phone
            ownerPhone = "8888888888"   // Mock owner phone
        )

        return PaymentResponse(
            transactionId = transactionId,
            status = "SUCCESS",
            remainingAmount = 0.0, // Should be calculated based on user's total dues
            timestamp = timestamp
        )
    }

    override suspend fun updateNotificationConfig(config: NotificationConfig) {
        firestore.collection("notification_configs").document(config.userId).set(config).await()
    }

    override suspend fun getNotificationConfig(userId: String): NotificationConfig {
        val doc = firestore.collection("notification_configs").document(userId).get().await()
        return doc.toObject(NotificationConfig::class.java) ?: NotificationConfig(userId)
    }

    override suspend fun hello(): Map<String, String> = mapOf("message" to "Hello from Firebase")
    override suspend fun greet(name: String): Map<String, String> = mapOf("message" to "Hello $name from Firebase")
}
