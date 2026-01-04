package com.example.foodorderav.data.model

data class Food(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    //lưu ảnh nốt còn lại lên github
    val imageUrl: String = "",
    val category: String = "",
    val rating: Float = 0f
)