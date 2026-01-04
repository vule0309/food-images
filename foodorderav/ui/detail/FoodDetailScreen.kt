package com.example.foodorderav.ui.detail

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.foodorderav.data.Result
import com.example.foodorderav.data.model.Food
import com.example.foodorderav.data.repository.FoodRepository
import com.example.foodorderav.ui.cart.CartViewModel
import com.example.foodorderav.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    foodId: String,
    cartViewModel: CartViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    var food by remember { mutableStateOf<Food?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var quantity by remember { mutableStateOf(1) }

    val context = LocalContext.current
    val foodRepository = remember { FoodRepository() }

    // Load food details
    LaunchedEffect(foodId) {
        isLoading = true
        when (val result = foodRepository.getFoodById(foodId)) {
            is Result.Success -> {
                food = result.data
                error = null
            }
            is Result.Error -> {
                error = result.exception.message
            }
            else -> {}
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết món ăn") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = error ?: "Lỗi không xác định",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onNavigateBack) {
                        Text("Quay lại")
                    }
                }
            }

            food != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Food image
                    AsyncImage(
                        model = food!!.imageUrl,
                        contentDescription = food!!.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Food info
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = food!!.name,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(
                                text = "⭐ ${food!!.rating}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = food!!.category,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = food!!.description,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = food!!.price.formatPrice(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Quantity selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Số lượng:",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { if (quantity > 1) quantity-- }
                                ) {
                                    Icon(Icons.Default.Remove, "Decrease")
                                }

                                Text(
                                    text = quantity.toString(),
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                IconButton(
                                    onClick = { quantity++ }
                                ) {
                                    Icon(Icons.Default.Add, "Increase")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Add to cart button
                        Button(
                            onClick = {
                                cartViewModel.addToCart(food!!, quantity)
                                Toast.makeText(
                                    context,
                                    "Đã thêm ${quantity} ${food!!.name} vào giỏ hàng",
                                    Toast.LENGTH_SHORT
                                ).show()
                                quantity = 1
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text("THÊM VÀO GIỎ HÀNG")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Go to cart button
                        OutlinedButton(
                            onClick = onNavigateToCart,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text("XEM GIỎ HÀNG (${cartViewModel.getCartItemCount()})")
                        }
                    }
                }
            }
        }
    }
}