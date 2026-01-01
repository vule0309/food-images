package com.example.foodorderav.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Detail : Screen("detail/{foodId}") {
        fun createRoute(foodId: String) = "detail/$foodId"
    }
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object OrderHistory : Screen("order_history")
    object Profile : Screen("profile")  // ← THÊM MỚI
}