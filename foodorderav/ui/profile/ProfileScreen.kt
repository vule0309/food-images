package com.example.foodorderav.ui.profile

import androidx.compose. foundation.layout.*
import androidx. compose.material.icons.Icons
import androidx. compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToOrderHistory: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by profileViewModel.currentUser. collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tài khoản") },
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
                .padding(16.dp)
        ) {
            // User info card
            Card(
                modifier = Modifier. fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Surface(
                        modifier = Modifier. size(64.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUser?.email?.first()?.uppercase() ?: "? ",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier. width(16.dp))

                    // User info
                    Column {
                        Text(
                            text = currentUser?.displayName?. ifBlank { "Người dùng" } ?: "Người dùng",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier. height(4.dp))
                        Text(
                            text = currentUser?.email ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier. height(24.dp))

            // Menu items
            Text(
                text = "Tài khoản",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme. onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Order history
            Card(
                modifier = Modifier. fillMaxWidth(),
                onClick = onNavigateToOrderHistory
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.List,
                            contentDescription = "Order history",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier. width(16.dp))
                        Text(
                            text = "Lịch sử đơn hàng",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Icon(
                        Icons.Default. ChevronRight,
                        contentDescription = "Navigate",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Edit profile (placeholder)
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* TODO: Navigate to edit profile */ }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default. Person,
                            contentDescription = "Edit profile",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Chỉnh sửa thông tin",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Navigate",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Cài đặt",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme. onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Settings (placeholder)
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* TODO: Navigate to settings */ }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme. primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Cài đặt",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Navigate",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // About (placeholder)
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* TODO: Show about dialog */ }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "About",
                            tint = MaterialTheme. colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Về ứng dụng",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Icon(
                        Icons.Default. ChevronRight,
                        contentDescription = "Navigate",
                        tint = MaterialTheme. colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout button
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults. outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Logout, "Logout")
                Spacer(modifier = Modifier.width(8.dp))
                Text("ĐĂNG XUẤT")
            }

            Spacer(modifier = Modifier. height(8.dp))

            // Version info
            Text(
                text = "Food Order AV v1.0. 0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme. colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Đăng xuất") },
            text = { Text("Bạn có chắc chắn muốn đăng xuất? ") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        profileViewModel.logout()
                        onLogout()
                    }
                ) {
                    Text("Đăng xuất", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}