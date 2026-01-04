package com.example.foodorderav.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose. runtime.getValue
import androidx.compose.ui. Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example. foodorderav.ui.auth.AuthViewModel
import com.example.foodorderav.ui.auth.LoginScreen
import com.example.foodorderav.ui.auth.RegisterScreen
import com.example.foodorderav.ui.cart.CartScreen
import com.example.foodorderav.ui.cart.CartViewModel
import com.example.foodorderav.ui.detail.FoodDetailScreen
import com.example.foodorderav.ui.home.HomeScreen
import com.example.foodorderav.ui.home.HomeViewModel
import com.example.foodorderav.ui.order.CheckoutScreen
import com. example.foodorderav.ui. order.OrderHistoryScreen
import com.example.foodorderav.ui.order. OrderViewModel
import com.example.foodorderav.ui.profile.EditProfileScreen  // ← THÊM IMPORT
import com.example.foodorderav.ui.profile.ProfileScreen
import com.example.foodorderav.ui.profile.ProfileViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    cartViewModel:  CartViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel(),
    profileViewModel:  ProfileViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    val startDestination = if (currentUser != null) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ... các composable khác giữ nguyên ...

        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen. Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home. route) {
            HomeScreen(
                homeViewModel = homeViewModel,
                cartViewModel = cartViewModel,
                onFoodClick = { foodId ->
                    navController.navigate(Screen.Detail.createRoute(foodId))
                },
                onCartClick = {
                    navController.navigate(Screen.Cart.route)
                },
                onOrderHistoryClick = {
                    navController.navigate(Screen.OrderHistory.route)
                },
                onProfileClick = {
                    navController.navigate(Screen. Profile.route)
                },
                onLogout = {
                    authViewModel. logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("foodId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val foodId = backStackEntry.arguments?.getString("foodId") ?: return@composable
            FoodDetailScreen(
                foodId = foodId,
                cartViewModel = cartViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                cartViewModel = cartViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCheckout = {
                    navController.navigate(Screen.Checkout. route)
                }
            )
        }

        // ========== CẬP NHẬT CHECKOUT - THÊM PROFILEVIEWMODEL ==========
        composable(Screen.Checkout.route) {
            CheckoutScreen(
                cartViewModel = cartViewModel,
                orderViewModel = orderViewModel,
                profileViewModel = profileViewModel,  // ← THÊM DÒNG NÀY
                onNavigateBack = {
                    navController. popBackStack()
                },
                onOrderSuccess = {
                    navController. navigate(Screen.OrderHistory.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(
                orderViewModel = orderViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ========== CẬP NHẬT PROFILE - THÊM CALLBACK ==========
        composable(Screen.Profile.route) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToOrderHistory = {
                    navController.navigate(Screen.OrderHistory. route)
                },
                onNavigateToEditProfile = {  // ← THÊM CALLBACK MỚI
                    navController.navigate(Screen.EditProfile. route)
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // ========== THÊM MÀN HÌNH EDIT PROFILE ==========
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                profileViewModel = profileViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}