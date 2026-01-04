package com.example.foodorderav.data.repository

import com.example.foodorderav.data.Result
import com.example.foodorderav. data.model.User
import com.example.foodorderav.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection(Constants.COLLECTION_USERS)

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult. user ?: throw Exception("User not found")

            val userDoc = usersCollection. document(firebaseUser.uid).get().await()

            val user = if (userDoc.exists()) {
                userDoc.toObject(User::class.java) ?: User(
                    uid = firebaseUser. uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: ""
                )
            } else {
                val newUser = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: ""
                )
                usersCollection.document(firebaseUser.uid).set(newUser).await()
                newUser
            }

            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun register(email: String, password: String, displayName: String): Result<User> {
        return try {
            val authResult = auth. createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Registration failed")

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            firebaseUser.updateProfile(profileUpdates).await()

            val user = User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?:  "",
                displayName = displayName
            )

            usersCollection.document(firebaseUser.uid).set(user).await()

            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // ========== LẤY USER TỪ FIRESTORE (ĐẦY ĐỦ THÔNG TIN) ==========
    suspend fun getCurrentUserFromFirestore(): Result<User> {
        return try {
            val firebaseUser = auth.currentUser ?: throw Exception("Not logged in")
            val userDoc = usersCollection.document(firebaseUser.uid).get().await()

            val user = if (userDoc.exists()) {
                userDoc.toObject(User::class.java) ?: throw Exception("Parse error")
            } else {
                // Tạo mới nếu chưa có
                val newUser = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: ""
                )
                usersCollection.document(firebaseUser.uid).set(newUser).await()
                newUser
            }

            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // ========== CẬP NHẬT PROFILE ==========
    suspend fun updateUserProfile(
        displayName: String,
        fullName: String,
        phone:  String,
        address: String
    ): Result<User> {
        return try {
            val firebaseUser = auth.currentUser ?: throw Exception("Not logged in")

            // Cập nhật các field mới trong Firestore
            val updates = mapOf(
                "displayName" to displayName,
                "fullName" to fullName,
                "phone" to phone,
                "address" to address
            )

            usersCollection.document(firebaseUser.uid).update(updates).await()

            // Lấy user đã cập nhật
            val userDoc = usersCollection. document(firebaseUser.uid).get().await()
            val updatedUser = userDoc.toObject(User::class.java)
                ?: throw Exception("Failed to get updated user")

            Result.Success(updatedUser)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName ?: ""
        )
    }

    fun logout() {
        auth.signOut()
    }
}