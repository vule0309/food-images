package com.example.foodorderav.data.repository

import com.example.foodorderav.data.Result
import com.example.foodorderav.data.model.Food
import com.example.foodorderav.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FoodRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val foodsCollection = firestore.collection(Constants.COLLECTION_FOODS)

    suspend fun getAllFoods(): Result<List<Food>> {
        return try {
            val snapshot = foodsCollection.get().await()
            val foods = snapshot.toObjects(Food::class.java)
            Result.Success(foods)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getFoodById(foodId: String): Result<Food> {
        return try {
            val snapshot = foodsCollection.document(foodId).get().await()
            val food = snapshot.toObject(Food::class.java)
            if (food != null) {
                Result.Success(food)
            } else {
                Result.Error(Exception("Food not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}