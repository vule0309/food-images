package com.example.foodorderav.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx. compose.foundation.lazy.items
import androidx.compose.material. icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose. material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodorderav.util.formatDate
import com.example.foodorderav.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    orderViewModel: OrderViewModel,
    onNavigateBack: () -> Unit
) {
    val orderHistory by orderViewModel.orderHistory. collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()
    val error by orderViewModel.error. collectAsState()

    // Load order history khi màn hình được tạo
    LaunchedEffect(Unit) {
        orderViewModel. loadOrderHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lịch sử đơn hàng") },
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
                    Spacer(modifier = Modifier. height(16.dp))
                    Button(onClick = { orderViewModel.loadOrderHistory() }) {
                        Text("Thử lại")
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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📋",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier. height(16.dp))
                        Text(
                            text = "Chưa có đơn hàng nào",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Đặt món ăn để xem lịch sử đơn hàng",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = onNavigateBack) {
                            Text("Về trang chủ")
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
                    verticalArrangement = Arrangement. spacedBy(12.dp)
                ) {
                    items(orderHistory) { order ->
                        OrderCard(order = order)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: com.example.foodorderav.data.model.Order) {
    Card(
        modifier = Modifier. fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Order ID and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Đơn #${order.orderId. take(8)}",
                    style = MaterialTheme.typography.titleMedium
                )

                StatusChip(status = order.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date
            Text(
                text = order.createdAt.formatDate(),
                style = MaterialTheme. typography.bodySmall,
                color = MaterialTheme. colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Customer info
            if (order.customerName.isNotBlank()) {
                Text(
                    text = "Người nhận: ${order.customerName}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "SĐT: ${order.phone}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Địa chỉ: ${order.address}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Items
            Text(
                text = "Món ăn:",
                style = MaterialTheme. typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.foodName} x${item.quantity}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = (item.price * item.quantity).formatPrice(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement. SpaceBetween
            ) {
                Text(
                    text = "Tổng cộng:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = order. totalPrice.formatPrice(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Payment method
            if (order.paymentMethod. isNotBlank()) {
                Spacer(modifier = Modifier. height(8.dp))
                Text(
                    text = "Thanh toán: ${
                        when(order.paymentMethod) {
                            "COD" -> "Khi nhận hàng"
                            "BANK" -> "Chuyển khoản"
                            else -> order.paymentMethod
                        }
                    }",
                    style = MaterialTheme.typography. bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Note
            if (order.note. isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ghi chú: ${order.note}",
                    style = MaterialTheme.typography. bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (backgroundColor, textColor, text) = when (status. uppercase()) {
        "PENDING" -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            "Đang xử lý"
        )
        "CONFIRMED" -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme. colorScheme.onPrimaryContainer,
            "Đã xác nhận"
        )
        "SHIPPING" -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "Đang giao"
        )
        "COMPLETED" -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme. colorScheme.primary,
            "Hoàn thành"
        )
        "CANCELLED" -> Triple(
            MaterialTheme.colorScheme. errorContainer,
            MaterialTheme. colorScheme.error,
            "Đã hủy"
        )
        else -> Triple(
            MaterialTheme.colorScheme. surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            status
        )
    }

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4. dp),
            style = MaterialTheme.typography.labelMedium,
            color = textColor
        )
    }
}