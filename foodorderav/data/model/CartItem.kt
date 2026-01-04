package com.example.foodorderav.data.model

data class CartItem(
    val food: Food,
    val quantity: Int = 1
) {
    val totalPrice: Double
        get() = food.price * quantity
}