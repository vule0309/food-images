package com.example.foodorderav.ui.order

import androidx. compose.foundation.background
import androidx.compose.foundation. layout.*
import androidx.compose. foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose. foundation.shape.CircleShape
import androidx.compose. foundation.shape.RoundedCornerShape
import androidx.compose. material. icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui. text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose. ui.unit.sp
import com.example.foodorderav.data.model.Order
import com.example.foodorderav.ui.theme.*
import com.example.foodorderav.util.formatDate
import com.example.foodorderav.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    orderViewModel: OrderViewModel,
    onNavigateBack: () -> Unit
) {
    val orderHistory by orderViewModel.orderHistory.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()
    val error by orderViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        orderViewModel.loadOrderHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Lịch sử đơn hàng", fontWeight = FontWeight.Bold)
                },
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
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Orange500)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Đang tải đơn hàng.. .", color = Gray500)
                    }
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        . fillMaxSize()
                        . padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Red100),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("😕", fontSize = 40.sp)
                        }
                        Spacer(modifier = Modifier. height(16.dp))
                        Text(
                            text = error ?: "Lỗi không xác định",
                            color = Red500
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { orderViewModel.loadOrderHistory() },
                            colors = ButtonDefaults.buttonColors(containerColor = Orange500),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default. Refresh, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Thử lại")
                        }
                    }
                }
            }

            orderHistory.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment. CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Orange100),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📋", fontSize = 60.sp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Chưa có đơn hàng nào",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Gray900
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Hãy đặt món ăn yêu thích ngay nào! ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Gray500
                        )

                        Spacer(modifier = Modifier. height(32.dp))

                        Button(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                        ) {
                            Icon(Icons.Default.Restaurant, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Khám phá món ăn", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header stats
                    item {
                        Card(
                            modifier = Modifier. fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Orange500)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = orderHistory.size.toString(),
                                        style = MaterialTheme. typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = White
                                    )
                                    Text(
                                        text = "Đơn hàng",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = White. copy(alpha = 0.8f)
                                    )
                                }

                                Divider(
                                    modifier = Modifier
                                        .height(50.dp)
                                        . width(1.dp),
                                    color = White.copy(alpha = 0.3f)
                                )



                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = orderHistory.count { it.status. uppercase() == "COMPLETED" }.toString(),
                                        style = MaterialTheme.typography. headlineMedium,
                                        fontWeight = FontWeight. Bold,
                                        color = White
                                    )
                                    Text(
                                        text = "Hoàn thành",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = White.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }

                    // Order list
                    items(orderHistory) { order ->
                        OrderCard(order = order)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier. fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier. padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment. CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Orange100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default. Receipt,
                            null,
                            tint = Orange500,
                            modifier = Modifier. size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "#${order.orderId. take(8).uppercase()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Gray900
                        )
                        Text(
                            text = order.createdAt.formatDate(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Gray500
                        )
                    }
                }

                StatusChip(status = order.status)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delivery info
            if (order.customerName.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Gray50)
                ) {
                    Column(modifier = Modifier. padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default. Person,
                                null,
                                tint = Gray500,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier. width(8.dp))
                            Text(
                                text = order.customerName,
                                style = MaterialTheme.typography. bodyMedium,
                                color = Gray700
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment. CenterVertically) {
                            Icon(
                                Icons. Default.Phone,
                                null,
                                tint = Gray500,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier. width(8.dp))
                            Text(
                                text = order.phone,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Gray700
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Default.LocationOn,
                                null,
                                tint = Gray500,
                                modifier = Modifier. size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = order. address,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Gray700
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Items
            Text(
                text = "Món đã đặt",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Gray900
            )

            Spacer(modifier = Modifier.height(8.dp))

            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(
                            text = "${item.quantity}x",
                            color = Orange500,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier. width(8.dp))
                        Text(
                            text = item.foodName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Gray700
                        )
                    }
                    Text(
                        text = (item.price * item.quantity).formatPrice(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Gray900
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Gray300)
            Spacer(modifier = Modifier.height(12.dp))

            // Total & Payment
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Tổng cộng",
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray500
                    )
                    Text(
                        text = order.totalPrice.formatPrice(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Orange500
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (order.paymentMethod == "COD") Green100 else Blue100
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (order.paymentMethod == "COD") Icons.Default.Money
                            else Icons.Default.AccountBalance,
                            null,
                            tint = if (order.paymentMethod == "COD") Green600 else Blue600,
                            modifier = Modifier. size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (order.paymentMethod == "COD") "COD" else "Bank",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (order.paymentMethod == "COD") Green600 else Blue600
                        )
                    }
                }
            }

            // Note
            if (order.note. isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Default.Notes,
                        null,
                        tint = Gray500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray500
                    )
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (backgroundColor, textColor, icon, text) = when (status.uppercase()) {
        "PENDING" -> listOf(Yellow100, Yellow600, Icons.Default.Schedule, "Đang xử lý")
        "CONFIRMED" -> listOf(Blue100, Blue600, Icons.Default.CheckCircle, "Đã xác nhận")
        "SHIPPING" -> listOf(Orange100, Orange500, Icons.Default.LocalShipping, "Đang giao")
        "COMPLETED" -> listOf(Green100, Green600, Icons.Default.Done, "Hoàn thành")
        "CANCELLED" -> listOf(Red100, Red500, Icons.Default.Cancel, "Đã hủy")
        else -> listOf(Gray100, Gray500, Icons.Default.Info, status)
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor as androidx.compose.ui.graphics.Color
    ) {
        Row(
            modifier = Modifier. padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon as androidx.compose.ui.graphics. vector.ImageVector,
                null,
                tint = textColor as androidx.compose.ui.graphics. Color,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text as String,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}

