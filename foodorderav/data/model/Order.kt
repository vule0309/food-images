package com.example.foodorderav.data.model

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalPrice: Double = 0.0,

    // ← CÁC FIELD CHO CHECKOUTSCREEN
    val customerName: String = "",
    val phone: String = "",
    val address: String = "",
    val note: String = "",
    val paymentMethod: String = "COD",

    val status: String = "PENDING",
    val createdAt: Long = System.currentTimeMillis()
)

data class OrderItem(
    val foodId: String = "",
    val foodName: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0
)