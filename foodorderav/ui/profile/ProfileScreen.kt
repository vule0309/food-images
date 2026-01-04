package com.example.foodorderav.ui.profile

import androidx.compose. foundation.background
import androidx.compose.foundation. layout.*
import androidx.compose. foundation.rememberScrollState
import androidx.compose.foundation.shape. CircleShape
import androidx.compose. foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material. icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui. graphics. Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui. unit.sp
import com.example.foodorderav.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToOrderHistory: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by profileViewModel.currentUser. collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        profileViewModel.loadCurrentUser()
    }

    Scaffold(
        containerColor = Gray50
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Orange500, Orange400)
                        )
                    )
                    .padding(top = 20.dp, bottom = 40.dp)
            ) {
                Column(
                    modifier = Modifier. fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Back button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = White)
                        }
                    }

                    // Avatar
                    Box(
                        modifier = Modifier
                            . size(100.dp)
                            .clip(CircleShape)
                            .background(White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser?.email?.first()?.uppercase() ?: "?",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = Orange500
                        )
                    }

                    Spacer(modifier = Modifier. height(16.dp))

                    // Name
                    Text(
                        text = currentUser?.displayName?. ifBlank { "Người dùng" } ?: "Người dùng",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )

                    Spacer(modifier = Modifier. height(4.dp))

                    // Email
                    Text(
                        text = currentUser?.email ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White. copy(alpha = 0.9f)
                    )

                    // Additional info if available
                    if (! currentUser?.fullName.isNullOrBlank() || !currentUser?.phone.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement. Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!currentUser?.phone.isNullOrBlank()) {
                                Icon(
                                    Icons.Default.Phone,
                                    null,
                                    tint = White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = currentUser?.phone ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier. height(8.dp))
            // Profile Card (overlapping)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults. cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Tài khoản",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Order History
                    ProfileMenuItem(
                        icon = Icons. Default.Receipt,
                        iconColor = Orange500,
                        iconBgColor = Orange100,
                        title = "Lịch sử đơn hàng",
                        subtitle = "Xem các đơn hàng đã đặt",
                        onClick = onNavigateToOrderHistory
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Edit Profile
                    ProfileMenuItem(
                        icon = Icons.Default. Edit,
                        iconColor = Green500,
                        iconBgColor = Green100,
                        title = "Chỉnh sửa thông tin",
                        subtitle = "Họ tên, SĐT, Địa chỉ",
                        onClick = onNavigateToEditProfile
                    )
                }
            }

            Spacer(modifier = Modifier. height(8.dp))

            // Settings Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Cài đặt",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )

                    Spacer(modifier = Modifier. height(16.dp))

                    // Notifications
                    ProfileMenuItem(
                        icon = Icons.Default. Notifications,
                        iconColor = Yellow500,
                        iconBgColor = Yellow100,
                        title = "Thông báo",
                        subtitle = "Quản lý thông báo",
                        onClick = { }
                    )

                    Spacer(modifier = Modifier. height(12.dp))

                    // Settings
                    ProfileMenuItem(
                        icon = Icons.Default.Settings,
                        iconColor = Gray500,
                        iconBgColor = Gray100,
                        title = "Cài đặt chung",
                        subtitle = "Ngôn ngữ, giao diện",
                        onClick = { }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Help
                    ProfileMenuItem(
                        icon = Icons.Default.Help,
                        iconColor = Blue500,
                        iconBgColor = Blue100,
                        title = "Trợ giúp & Hỗ trợ",
                        subtitle = "FAQ, liên hệ",
                        onClick = { }
                    )
                }
            }

            Spacer(modifier = Modifier. height(24.dp))

            // Logout button
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red100,
                    contentColor = Red500
                )
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Đăng xuất",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier. height(16.dp))

            // Version
            Text(
                text = "Food Order AV v1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = Gray500,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Logout dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = White,
            icon = {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Red100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons. Default.Logout,
                        null,
                        tint = Red500,
                        modifier = Modifier. size(30.dp)
                    )
                }
            },
            title = {
                Text(
                    "Đăng xuất",
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )
            },
            text = {
                Text(
                    "Bạn có chắc chắn muốn đăng xuất khỏi tài khoản?",
                    color = Gray700
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        profileViewModel.logout()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red500),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Đăng xuất", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Gray700)
                ) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    iconColor: androidx.compose.ui.graphics.Color,
    iconBgColor:  androidx.compose.ui.graphics. Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier. fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Gray50),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment. Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier. size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography. bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Gray900
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme. typography.bodySmall,
                    color = Gray500
                )
            }

            // Arrow
            Icon(
                Icons.Default.ChevronRight,
                null,
                tint = Gray500
            )
        }
    }
}

