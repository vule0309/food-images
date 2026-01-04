package com.example.foodorderav.ui.home

import androidx.compose. foundation.background
import androidx.compose.foundation. clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx. compose.foundation.lazy.grid. LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material. icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui. graphics.Brush
import androidx. compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.foodorderav. data.model.Food
import com.example.foodorderav.ui.cart.CartViewModel
import com.example.foodorderav.ui.theme.*
import com.example.foodorderav.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    onFoodClick: (String) -> Unit,
    onCartClick: () -> Unit,
    onOrderHistoryClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit
) {
    val foods by homeViewModel.foods.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val error by homeViewModel.error.collectAsState()
    val searchQuery by homeViewModel.searchQuery. collectAsState()
    val cartItemCount = cartViewModel.getCartItemCount()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🍔", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier. width(8.dp))
                        Text(
                            "Food Order",
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick) {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge(containerColor = Red500) {
                                        Text(
                                            cartItemCount.toString(),
                                            color = White
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                "Cart",
                                tint = White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Orange500,
                    titleContentColor = White
                )
            )
        },
        containerColor = Gray50
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar with gradient background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Orange500, Orange400)
                        )
                    )
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { homeViewModel.onSearchQueryChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Tìm kiếm món ăn.. .", color = Gray500) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, "Search", tint = Orange500)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                        focusedBorderColor = Orange500,
                        unfocusedBorderColor = Gray300
                    )
                )
            }

            // Content
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier. fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Orange500)
                    }
                }

                error != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("😕", style = MaterialTheme.typography. displayMedium)
                        Spacer(modifier = Modifier. height(16.dp))
                        Text(
                            text = error ?: "Lỗi không xác định",
                            color = Red500
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { homeViewModel.retry() },
                            colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                        ) {
                            Text("Thử lại")
                        }
                    }
                }

                foods.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🍽️", style = MaterialTheme. typography.displayMedium)
                            Spacer(modifier = Modifier. height(8.dp))
                            Text("Không tìm thấy món ăn", color = Gray500)
                        }
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement. spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(foods) { food ->
                            FoodItem(
                                food = food,
                                onClick = { onFoodClick(food.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodItem(
    food: Food,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults. cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = food.imageUrl,
                    contentDescription = food.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Rating badge
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd),
                    shape = RoundedCornerShape(8.dp),
                    color = Yellow500
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⭐", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier. width(2.dp))
                        Text(
                            text = food.rating.toString(),
                            style = MaterialTheme. typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Gray900
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Gray900
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = food.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Gray500
                )

                Spacer(modifier = Modifier. height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = food.price.formatPrice(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Orange500
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Green100
                    ) {
                        Text(
                            text = food. category,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Green600
                        )
                    }
                }
            }
        }
    }
}