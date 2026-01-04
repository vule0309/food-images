package com.example.foodorderav.ui.detail

import android.widget.Toast
import androidx.compose. foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose. foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons. filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui. graphics.Brush
import androidx. compose.ui.graphics.Color
import androidx.compose. ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose. ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodorderav. data.Result
import com.example.foodorderav.data.model.Food
import com.example.foodorderav.data.repository.FoodRepository
import com. example.foodorderav.ui. cart.CartViewModel
import com.example.foodorderav.ui.theme.*
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
    var error by remember { mutableStateOf<String? >(null) }
    var quantity by remember { mutableStateOf(1) }

    val context = LocalContext.current
    val foodRepository = remember { FoodRepository() }

    LaunchedEffect(foodId) {
        isLoading = true
        when (val result = foodRepository.getFoodById(foodId)) {
            is Result.Success -> {
                food = result.data
                error = null
            }
            is Result.Error -> {
                error = result.exception. message
            }
            else -> {}
        }
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    Text("😕", style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = error ?: "Lỗi không xác định", color = Red500)
                    Spacer(modifier = Modifier. height(16.dp))
                    Button(
                        onClick = onNavigateBack,
                        colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                    ) {
                        Text("Quay lại")
                    }
                }
            }

            food != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Image with overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        AsyncImage(
                            model = food !! .imageUrl,
                            contentDescription = food!!.name,
                            modifier = Modifier. fillMaxSize(),
                            contentScale = ContentScale. Crop
                        )

                        // Gradient overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.3f),
                                            Color. Transparent,
                                            Color.Black. copy(alpha = 0.5f)
                                        )
                                    )
                                )
                        )

                        // Back button
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.TopStart)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(White. copy(alpha = 0.9f))
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                "Back",
                                tint = Gray900
                            )
                        }

                        // Rating badge
                        Surface(
                            modifier = Modifier
                                . padding(16.dp)
                                .align(Alignment.TopEnd),
                            shape = RoundedCornerShape(12.dp),
                            color = Yellow500
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⭐", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = food!!.rating.toString(),
                                    fontWeight = FontWeight.Bold,
                                    color = Gray900
                                )
                            }
                        }
                    }

                    // Content Card
                    Card(
                        modifier = Modifier
                            . fillMaxWidth()
                            . offset(y = (-20).dp),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            // Category chip
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Green100
                            ) {
                                Text(
                                    text = food!!.category,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Green600,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier. height(12.dp))

                            // Name
                            Text(
                                text = food!!.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Gray900
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Price
                            Text(
                                text = food!!.price.formatPrice(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Orange500
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Divider(color = Gray300)

                            Spacer(modifier = Modifier. height(16.dp))

                            // Description
                            Text(
                                text = "Mô tả",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Gray900
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = food!! .description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Gray700,
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Quantity selector
                            Card(
                                modifier = Modifier. fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Gray100)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement. SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Số lượng",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = Gray900
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Decrease button
                                        IconButton(
                                            onClick = { if (quantity > 1) quantity-- },
                                            modifier = Modifier
                                                .size(40.dp)
                                                . clip(CircleShape)
                                                .background(White)
                                        ) {
                                            Icon(
                                                Icons.Default.Remove,
                                                "Decrease",
                                                tint = Orange500
                                            )
                                        }

                                        Text(
                                            text = quantity.toString(),
                                            modifier = Modifier.padding(horizontal = 20.dp),
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Gray900
                                        )

                                        // Increase button
                                        IconButton(
                                            onClick = { quantity++ },
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Orange500)
                                        ) {
                                            Icon(
                                                Icons.Default.Add,
                                                "Increase",
                                                tint = White
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Total price
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tổng tiền:",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Gray700
                                )
                                Text(
                                    text = (food!!.price * quantity).formatPrice(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Orange500
                                )
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
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                            ) {
                                Icon(Icons.Default.ShoppingCart, null)
                                Spacer(modifier = Modifier. width(8.dp))
                                Text(
                                    "THÊM VÀO GIỎ HÀNG",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // View cart button
                            OutlinedButton(
                                onClick = onNavigateToCart,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Orange500)
                            ) {
                                Icon(Icons.Default. Visibility, null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "XEM GIỎ HÀNG (${cartViewModel.getCartItemCount()})",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}