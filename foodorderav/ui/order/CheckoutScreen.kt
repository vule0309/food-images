package com.example.foodorderav.ui.order

import android.widget.Toast
import androidx.compose. foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation. verticalScroll
import androidx. compose.material. icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui. Alignment
import androidx.compose. ui.Modifier
import androidx. compose.ui.graphics. Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui. unit.dp
import androidx.compose. ui.unit.sp
import com.example.foodorderav.ui.cart.CartViewModel
import com.example.foodorderav.ui.profile.ProfileViewModel
import com.example.foodorderav.ui.theme.*
import com.example.foodorderav.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    orderViewModel: OrderViewModel,
    profileViewModel: ProfileViewModel,
    onNavigateBack: () -> Unit,
    onOrderSuccess: () -> Unit
) {
    val currentUser by profileViewModel.currentUser.collectAsState()

    var customerName by remember(currentUser?. uid) {
        mutableStateOf(currentUser?.fullName ?: "")
    }
    var phone by remember(currentUser?.uid) {
        mutableStateOf(currentUser?.phone ?: "")
    }
    var address by remember(currentUser?.uid) {
        mutableStateOf(currentUser?.address ?: "")
    }
    var note by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("COD") }

    val context = LocalContext.current
    val cartItems by cartViewModel.cartItems. collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()
    val orderPlaced by orderViewModel.orderPlaced.collectAsState()
    val error by orderViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadCurrentUser()
    }

    LaunchedEffect(orderPlaced) {
        if (orderPlaced) {
            Toast.makeText(context, "🎉 Đặt hàng thành công!", Toast.LENGTH_LONG).show()
            cartViewModel.clearCart()
            orderViewModel.resetOrderPlaced()
            onOrderSuccess()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            orderViewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thanh toán", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = White)
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    . verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Order summary card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default. Receipt,
                                null,
                                tint = Orange500,
                                modifier = Modifier. size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Đơn hàng của bạn",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Gray900
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        cartItems.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${item.quantity}x",
                                        color = Orange500,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier. width(8.dp))
                                    Text(
                                        text = item.food.name,
                                        color = Gray700
                                    )
                                }
                                Text(
                                    text = item.totalPrice.formatPrice(),
                                    fontWeight = FontWeight.Medium,
                                    color = Gray900
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Gray300)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Tổng cộng",
                                fontWeight = FontWeight.Bold,
                                color = Gray900
                            )
                            Text(
                                cartViewModel.getTotalPrice().formatPrice(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Orange500
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier. height(20.dp))

                // Delivery info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.LocalShipping,
                                    null,
                                    tint = Orange500,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Thông tin giao hàng",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight. Bold,
                                    color = Gray900
                                )
                            }

                            if (! currentUser?.fullName.isNullOrBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Green100
                                ) {
                                    Text(
                                        "✓ Từ hồ sơ",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Green600
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text("Họ và tên *") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, null, tint = Orange500)
                            },
                            modifier = Modifier. fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Orange500,
                                focusedLabelColor = Orange500,
                                cursorColor = Orange500
                            )
                        )

                        Spacer(modifier = Modifier. height(12.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Số điện thoại *") },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, null, tint = Orange500)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Orange500,
                                focusedLabelColor = Orange500,
                                cursorColor = Orange500
                            )
                        )

                        Spacer(modifier = Modifier. height(12.dp))

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Địa chỉ giao hàng *") },
                            leadingIcon = {
                                Icon(Icons.Default. LocationOn, null, tint = Orange500)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Orange500,
                                focusedLabelColor = Orange500,
                                cursorColor = Orange500
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = { Text("Ghi chú (tùy chọn)") },
                            leadingIcon = {
                                Icon(Icons.Default.Notes, null, tint = Gray500)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Orange500,
                                focusedLabelColor = Orange500,
                                cursorColor = Orange500
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Payment method
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults. cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default. Payment,
                                null,
                                tint = Orange500,
                                modifier = Modifier. size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Phương thức thanh toán",
                                style = MaterialTheme. typography.titleMedium,
                                fontWeight = FontWeight. Bold,
                                color = Gray900
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // COD Option
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedPaymentMethod == "COD") Orange100 else Gray100
                            ),
                            onClick = { selectedPaymentMethod = "COD" }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPaymentMethod == "COD",
                                    onClick = { selectedPaymentMethod = "COD" },
                                    colors = RadioButtonDefaults.colors(selectedColor = Orange500)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.Money, null, tint = Green500)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        "Thanh toán khi nhận hàng",
                                        fontWeight = FontWeight.Medium,
                                        color = Gray900
                                    )
                                    Text(
                                        "Trả tiền mặt khi giao hàng",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Gray500
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Bank Transfer Option
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedPaymentMethod == "BANK") Orange100 else Gray100
                            ),
                            onClick = { selectedPaymentMethod = "BANK" }
                        ) {
                            Row(
                                modifier = Modifier
                                    . fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPaymentMethod == "BANK",
                                    onClick = { selectedPaymentMethod = "BANK" },
                                    colors = RadioButtonDefaults.colors(selectedColor = Orange500)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons. Default.AccountBalance, null, tint = Blue500)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        "Chuyển khoản ngân hàng",
                                        fontWeight = FontWeight.Medium,
                                        color = Gray900
                                    )
                                    Text(
                                        "Chuyển khoản trước khi giao",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Gray500
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }

            // Bottom button
            Card(
                modifier = Modifier. fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Tổng thanh toán", color = Gray700)
                        Text(
                            cartViewModel.getTotalPrice().formatPrice(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Orange500
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (customerName.isBlank() || phone.isBlank() || address.isBlank()) {
                                Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
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
                        enabled = ! isLoading && cartItems.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Check, null)
                            Spacer(modifier = Modifier. width(8.dp))
                            Text("XÁC NHẬN ĐẶT HÀNG", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}


