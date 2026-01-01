package com.example.foodorderav.ui.order

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.foodorderav.ui.cart.CartViewModel
import com.example.foodorderav.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    orderViewModel: OrderViewModel,
    onNavigateBack: () -> Unit,
    onOrderSuccess: () -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("COD") }

    val context = LocalContext.current
    val cartItems by cartViewModel.cartItems.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()
    val orderPlaced by orderViewModel.orderPlaced.collectAsState()
    val error by orderViewModel.error.collectAsState()

    // Navigate khi đặt hàng thành công
    LaunchedEffect(orderPlaced) {
        if (orderPlaced) {
            cartViewModel.clearCart()
            orderViewModel.resetOrderPlaced()
            onOrderSuccess()
        }
    }

    // Hiển thị lỗi
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            orderViewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thanh toán") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Order summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Đơn hàng của bạn",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    cartItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.food.name} x${item.quantity}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = item.totalPrice.formatPrice(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tổng cộng:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = cartViewModel.getTotalPrice().formatPrice(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Customer info
            Text(
                text = "Thông tin giao hàng",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Họ và tên *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Số điện thoại *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Địa chỉ giao hàng *") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Ghi chú (tùy chọn)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Payment method
            Text(
                text = "Phương thức thanh toán",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedPaymentMethod == "COD",
                    onClick = { selectedPaymentMethod = "COD" }
                )
                Text("Thanh toán khi nhận hàng (COD)")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedPaymentMethod == "BANK",
                    onClick = { selectedPaymentMethod = "BANK" }
                )
                Text("Chuyển khoản ngân hàng")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Place order button
            Button(
                onClick = {
                    if (customerName.isBlank() || phone.isBlank() || address.isBlank()) {
                        Toast.makeText(
                            context,
                            "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    orderViewModel.placeOrder(
                        cartItems = cartItems,
                        customerName = customerName,
                        phone = phone,
                        address = address,
                        note = note,
                        paymentMethod = selectedPaymentMethod
                    )
                },
                enabled = !isLoading && cartItems.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("ĐẶT HÀNG")
                }
            }
        }
    }
}