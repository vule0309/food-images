package com.example.foodorderav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.foodorderav.ui.auth.AuthViewModel
import com.example.foodorderav.ui.cart.CartViewModel
import com.example.foodorderav.ui.home.HomeViewModel
import com.example.foodorderav.ui.navigation.NavGraph
import com.example.foodorderav.ui.navigation.Screen
import com.example.foodorderav.ui.order.OrderViewModel
import com.example.foodorderav.ui.profile.ProfileViewModel
import com.example.foodorderav.ui.theme.FoodOrderAVTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodOrderAVTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Tạo ViewModels ở đây để share giữa các màn hình
    val authViewModel: AuthViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    val currentUser by authViewModel.currentUser.collectAsState()

    // Theo dõi route hiện tại
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Các route KHÔNG hiển thị Bottom Nav
    val routesWithoutBottomNav = listOf(
        Screen.Login.route,
        Screen.Register.route,
        "detail/",  // Bất kỳ route nào bắt đầu bằng "detail/"
        Screen.Checkout.route
    )

    // Kiểm tra có hiển thị Bottom Nav không
    val showBottomBar = currentUser != null &&
            routesWithoutBottomNav.none { route ->
                currentRoute?.startsWith(route) == true
            }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    cartItemCount = cartViewModel.getCartItemCount()
                )
            }
        }
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            authViewModel = authViewModel,
            cartViewModel = cartViewModel,
            homeViewModel = homeViewModel,
            orderViewModel = orderViewModel,
            profileViewModel = profileViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?,
    cartItemCount: Int
) {
    val items = listOf(
        BottomNavItem(
            route = Screen.Home.route,
            icon = Icons.Default.Home,
            label = "Home"
        ),
        BottomNavItem(
            route = Screen.Cart.route,
            icon = Icons.Default.ShoppingCart,
            label = "Cart",
            badgeCount = if (cartItemCount > 0) cartItemCount else null
        ),
        BottomNavItem(
            route = Screen.OrderHistory.route,
            icon = Icons.Default.List,
            label = "History"
        ),
        BottomNavItem(
            route = Screen.Profile.route,
            icon = Icons.Default.Person,
            label = "Profile"
        )
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Xóa back stack để tránh tích lũy
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    if (item.badgeCount != null) {
                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(item.badgeCount.toString())
                                }
                            }
                        ) {
                            Icon(item.icon, contentDescription = item.label)
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.label)
                    }
                },
                label = { Text(item.label) }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val badgeCount: Int? = null
)