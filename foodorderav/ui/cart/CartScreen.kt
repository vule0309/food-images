package com.example.foodorderav.ui.cart

import android.widget.Toast
import androidx. compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx. compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose. foundation.shape.RoundedCornerShape
import androidx.compose. material.icons.Icons
import androidx. compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui. layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx. compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodorderav. data.model.CartItem
import com.example.foodorderav.ui.theme.*
import com.example.foodorderav.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel:  CartViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCheckout: () -> Unit
) {
    val cartItems by cartViewModel.cartItems. collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Giỏ hàng",
                            fontWeight = FontWeight.Bold
                        )
                        if (cartItems.isNotEmpty()) {
                            Spacer(modifier = Modifier. width(8.dp))
                            Badge(containerColor = White, contentColor = Orange500) {
                                Text(cartItems.size.toString())
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons. Default.ArrowBack, "Back", tint = White)
                    }
                },
                actions = {
                    if (cartItems.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                cartViewModel.clearCart()
                                Toast.makeText(context, "Đã xóa toàn bộ giỏ hàng", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.DeleteSweep, "Clear cart", tint = White)
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
        if (cartItems.isEmpty()) {
            // Empty cart
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment. Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Orange100),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🛒", fontSize = 60.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Giỏ hàng trống",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Thêm món ăn yêu thích vào giỏ hàng nhé!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray500
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                    ) {
                        Icon(Icons. Default.Restaurant, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Khám phá món ăn", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Cart items list
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems) { cartItem ->
                        CartItemCard(
                            cartItem = cartItem,
                            onIncreaseQuantity = { cartViewModel.increaseQuantity(cartItem) },
                            onDecreaseQuantity = { cartViewModel.decreaseQuantity(cartItem) },
                            onRemove = {
                                cartViewModel.removeFromCart(cartItem)
                                Toast.makeText(
                                    context,
                                    "Đã xóa ${cartItem.food.name}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }

                // Bottom summary
                Card(
                    modifier = Modifier. fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults. cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Summary rows
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Tạm tính (${cartViewModel.getCartItemCount()} món)",
                                color = Gray700
                            )
                            Text(
                                cartViewModel.getTotalPrice().formatPrice(),
                                fontWeight = FontWeight.Medium,
                                color = Gray900
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Phí vận chuyển", color = Gray700)
                            Text(
                                "Miễn phí",
                                color = Green500,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider(color = Gray300)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier. fillMaxWidth(),
                            horizontalArrangement = Arrangement. SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Tổng cộng",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Gray900
                            )
                            Text(
                                cartViewModel.getTotalPrice().formatPrice(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Orange500
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onNavigateToCheckout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                        ) {
                            Text(
                                "TIẾN HÀNH THANH TOÁN",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default. ArrowForward, null)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier. fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults. cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Food image
            AsyncImage(
                model = cartItem.food.imageUrl,
                contentDescription = cartItem.food.name,
                modifier = Modifier
                    . size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale. Crop
            )

            Spacer(modifier = Modifier. width(12.dp))

            // Food info
            Column(
                modifier = Modifier. weight(1f)
            ) {
                Text(
                    text = cartItem.food.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Gray900
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = cartItem.food.price.formatPrice(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Orange500,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier. height(8.dp))

                // Quantity controls
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Decrease
                    IconButton(
                        onClick = onDecreaseQuantity,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Gray100)
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            "Decrease",
                            modifier = Modifier.size(18.dp),
                            tint = Gray700
                        )
                    }

                    Text(
                        text = cartItem.quantity.toString(),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme. typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )

                    // Increase
                    IconButton(
                        onClick = onIncreaseQuantity,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Orange500)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            "Increase",
                            modifier = Modifier.size(18.dp),
                            tint = White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Total price for this item
                    Text(
                        text = cartItem.totalPrice.formatPrice(),
                        style = MaterialTheme.typography. titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )
                }
            }

            // Remove button
            IconButton(
                onClick = onRemove,
                modifier = Modifier. size(32.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    "Remove",
                    tint = Red500,
                    modifier = Modifier. size(20.dp)
                )
            }
        }
    }
}