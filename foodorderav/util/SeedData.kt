package com.example.foodorderav.util

import com.example.foodorderav.data.model.Food
import com.google.firebase.firestore.FirebaseFirestore

object SeedData {
    fun seedFoods() {
        val firestore = FirebaseFirestore.getInstance()

        val foods = listOf(
            Food(
                id = "pho-bo-01",
                name = "Phở Bò Tái",
                description = "Phở bò tái truyền thống với nước dùng thơm ngon",
                price = 50000.0,
                imageUrl = "https://images.unsplash.com/photo-1591814468924-caf88d1232e1?w=400",
                category = "Món chính",
                rating = 4.5f
            ),
            Food(
                id = "com-rang-01",
                name = "Cơm Rang Dương Châu",
                description = "Cơm rang với tôm, xúc xích và rau củ",
                price = 45000.0,
                imageUrl = "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=400",
                category = "Món chính",
                rating = 4.3f
            ),
            Food(
                id = "bun-cha-01",
                name = "Bún Chả Hà Nội",
                description = "Bún chả với thịt nướng thơm lừng",
                price = 40000.0,
                imageUrl = "https://images.unsplash.com/photo-1559314809-0d155014e29e?w=400",
                category = "Món chính",
                rating = 4.7f
            ),
            Food(
                id = "banh-mi-01",
                name = "Bánh Mì Thịt",
                description = "Bánh mì giòn với thịt nguội và pate",
                price = 25000.0,
                imageUrl = "https://images.unsplash.com/photo-1634843783875-0ac370c6e0f4?w=400",
                category = "Ăn vặt",
                rating = 4.2f
            ),
            Food(
                id = "tra-sua-01",
                name = "Trà Sữa Trân Châu",
                description = "Trà sữa béo ngậy với trân châu dai",
                price = 30000.0,
                imageUrl = "https://images.unsplash.com/photo-1525385133512-2f3bdd039054?w=400",
                category = "Đồ uống",
                rating = 4.4f
            ),
            Food(
                id = "pizza-01",
                name = "Pizza Hải Sản",
                description = "Pizza với tôm, mực và phô mai mozzarella",
                price = 120000.0,
                imageUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400",
                category = "Món Tây",
                rating = 4.6f
            )
        )

        foods.forEach { food ->
            firestore.collection("foods").document(food.id).set(food)
        }
    }
}