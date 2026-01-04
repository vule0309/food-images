package com.example.foodorderav.data.repository

import com.example.foodorderav.data.Result
import com.example.foodorderav.data.model.Order
import com.example.foodorderav.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class OrderRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val ordersCollection = firestore.collection(Constants.COLLECTION_ORDERS)

    suspend fun createOrder(order: Order): Result<String> {
        return try {
            val docRef = ordersCollection.document()
            val orderWithId = order.copy(orderId = docRef.id)
            docRef.set(orderWithId).await()
            Result.Success(docRef.id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getOrderHistory(userId: String): Result<List<Order>> {
        return try {
            val snapshot = ordersCollection
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val orders = snapshot.toObjects(Order::class.java)
            Result.Success(orders)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}