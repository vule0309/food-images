package com.example.foodorderav.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val fullName: String = "",      // Họ và tên đầy đủ
    val phone: String = "",         // Số điện thoại
    val address: String = ""        // Địa chỉ
)