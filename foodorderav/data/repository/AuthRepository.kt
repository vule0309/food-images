package com.example.foodorderav.data.repository

import com.example.foodorderav.data.Result
import com.example.foodorderav.data.model.User
import com.example.foodorderav.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com. google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection(Constants.COLLECTION_USERS)

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("User not found")

            // Lấy thông tin user từ Firestore
            val userDoc = usersCollection.document(firebaseUser.uid).get(). await()

            val user = if (userDoc.exists()) {
                // Có data trong Firestore
                userDoc.toObject(User::class.java) ?: User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: ""
                )
            } else {
                // Không có data trong Firestore, tạo mới
                val newUser = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: ""
                )
                usersCollection. document(firebaseUser.uid).set(newUser).await()
                newUser
            }

            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // ← CẬP NHẬT HÀM NÀY
    suspend fun register(email: String, password: String, displayName: String): Result<User> {
        return try {
            // 1. Tạo tài khoản Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Registration failed")

            // 2. Cập nhật displayName trong Firebase Auth
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            firebaseUser.updateProfile(profileUpdates).await()

            // 3. Tạo User object
            val user = User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                displayName = displayName
            )

            // 4. Lưu vào Firestore
            usersCollection.document(firebaseUser. uid).set(user).await()

            Result.Success(user)
        } catch (e: Exception) {
            Result. Error(e)
        }
    }

    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            uid = firebaseUser. uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName ?: ""
        )
    }

    fun logout() {
        auth.signOut()
    }
}