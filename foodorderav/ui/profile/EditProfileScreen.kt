package com.example.foodorderav.ui.profile

import android.widget.Toast
import androidx.compose. foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose. foundation.shape.RoundedCornerShape
import androidx.compose. foundation.text.KeyboardOptions
import androidx.compose.foundation. verticalScroll
import androidx. compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui. graphics.Brush
import androidx. compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui. unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodorderav.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val currentUser by profileViewModel.currentUser. collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val error by profileViewModel.error.collectAsState()
    val updateSuccess by profileViewModel.updateSuccess. collectAsState()

    val context = LocalContext.current

    var displayName by remember(currentUser) {
        mutableStateOf(currentUser?.displayName ?: "")
    }
    var fullName by remember(currentUser) {
        mutableStateOf(currentUser?.fullName ?: "")
    }
    var phone by remember(currentUser) {
        mutableStateOf(currentUser?. phone ?: "")
    }
    var address by remember(currentUser) {
        mutableStateOf(currentUser?.address ?: "")
    }

    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            Toast.makeText(context, "✅ Cập nhật thành công!", Toast.LENGTH_SHORT).show()
            profileViewModel.resetUpdateSuccess()
            onNavigateBack()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            profileViewModel.clearError()
        }
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
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Orange500, Orange400)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Top bar
                    Row(
                        modifier = Modifier. fillMaxWidth(),
                        horizontalArrangement = Arrangement. SpaceBetween,
                        verticalAlignment = Alignment. CenterVertically
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(White. copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = White)
                        }

                        Text(
                            text = "Chỉnh sửa thông tin",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )

                        // Placeholder for alignment
                        Spacer(modifier = Modifier. size(40.dp))
                    }

                    Spacer(modifier = Modifier. height(20.dp))

                    // User card
                    Card(
                        modifier = Modifier. fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    . clip(CircleShape)
                                    .background(Orange100),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentUser?.email?.first()?.uppercase() ?: "?",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Orange500
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = currentUser?.displayName?. ifBlank { "Người dùng" } ?: "Người dùng",
                                    style = MaterialTheme. typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Gray900
                                )
                                Spacer(modifier = Modifier. height(2.dp))
                                Text(
                                    text = currentUser?.email ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Gray500
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier. height(20.dp))

            // Form
            Column(
                modifier = Modifier. padding(horizontal = 16.dp)
            ) {
                // Info card
                Card(
                    modifier = Modifier. fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Green100)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default. Lightbulb,
                            null,
                            tint = Green600,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Thông tin này sẽ được tự động điền khi bạn đặt hàng",
                            style = MaterialTheme.typography.bodySmall,
                            color = Green600
                        )
                    }
                }

                Spacer(modifier = Modifier. height(20.dp))

                // Form Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Thông tin cá nhân",
                            style = MaterialTheme. typography.titleMedium,
                            fontWeight = FontWeight. Bold,
                            color = Gray900
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Tên hiển thị",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Gray700
                        )
                        Spacer(modifier = Modifier. height(8.dp))
                        // Tên hiển thị
                        OutlinedTextField(
                            value = displayName,
                            onValueChange = { displayName = it },
                            label = { Text("Tên hiển thị") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Orange500
                                )
                            },
                            modifier = Modifier. fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Orange500,
                                unfocusedBorderColor = Gray300,
                                cursorColor = Orange500
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Full Name
                        Text(
                            text = "Họ và tên đầy đủ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Gray700
                        )
                        Spacer(modifier = Modifier. height(8.dp))
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            placeholder = { Text("Nguyễn Văn A", color = Gray500) },
                            leadingIcon = {
                                Icon(Icons.Default.Person, null, tint = Orange500)
                            },
                            modifier = Modifier. fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Orange500,
                                unfocusedBorderColor = Gray300,
                                cursorColor = Orange500
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Phone
                        Text(
                            text = "Số điện thoại",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Gray700
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            placeholder = { Text("0901234567", color = Gray500) },
                            leadingIcon = {
                                Icon(Icons. Default.Phone, null, tint = Orange500)
                            },
                            modifier = Modifier. fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Orange500,
                                unfocusedBorderColor = Gray300,
                                cursorColor = Orange500
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Address
                        Text(
                            text = "Địa chỉ",
                            style = MaterialTheme. typography.bodyMedium,
                            fontWeight = FontWeight. Medium,
                            color = Gray700
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            placeholder = { Text("123 Đường ABC, Quận XYZ, TP. HCM", color = Gray500) },
                            leadingIcon = {
                                Icon(Icons.Default.LocationOn, null, tint = Orange500)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Orange500,
                                unfocusedBorderColor = Gray300,
                                cursorColor = Orange500
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Save button
                Button(
                    onClick = {
                        profileViewModel.updateProfile(
                            displayName = displayName.trim(),
                            fullName = fullName.trim(),
                            phone = phone.trim(),
                            address = address.trim()
                        )
                    },
                    enabled = !isLoading,
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
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Đang lưu.. .", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Check, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("LƯU THÔNG TIN", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Cancel button
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Gray700)
                ) {
                    Text("Hủy")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}