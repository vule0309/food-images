package com.example.foodorderav.ui.auth

import android.widget.Toast
import androidx. compose.foundation.layout.*
import androidx.compose.foundation. text.KeyboardOptions
import androidx.compose.material. icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx. compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui. unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var displayName by remember { mutableStateOf("") }  // ← THÊM MỚI
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val isLoading by authViewModel.isLoading.collectAsState()
    val error by authViewModel.error.collectAsState()
    val authSuccess by authViewModel.authSuccess.collectAsState()

    LaunchedEffect(authSuccess) {
        if (authSuccess) {
            onNavigateToHome()
            authViewModel.resetAuthSuccess()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            authViewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đăng ký") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToLogin) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                . fillMaxSize()
                .padding(paddingValues)
                . padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Tạo tài khoản mới",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ← THÊM MỚI: TRƯỜNG HỌ VÀ TÊN
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Họ và tên") },
                leadingIcon = { Icon(Icons.Default.Person, "Name") },
                singleLine = true,
                modifier = Modifier. fillMaxWidth()
            )

            Spacer(modifier = Modifier. height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier. fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16. dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mật khẩu") },
                leadingIcon = { Icon(Icons.Default.Lock, "Password") },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default. Visibility
                            else Icons.Default. VisibilityOff,
                            "Toggle"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Xác nhận mật khẩu") },
                leadingIcon = { Icon(Icons. Default.Lock, "Confirm Password") },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = ! confirmPasswordVisible }) {
                        Icon(
                            if (confirmPasswordVisible) Icons.Default. Visibility
                            else Icons. Default.VisibilityOff,
                            "Toggle"
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        displayName.isBlank() -> {  // ← THÊM VALIDATION
                            Toast.makeText(context, "Vui lòng nhập họ và tên", Toast.LENGTH_SHORT).show()
                        }
                        email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                            Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                        }
                        password. length < 6 -> {
                            Toast.makeText(context, "Mật khẩu phải có ít nhất 6 ký tự", Toast. LENGTH_SHORT).show()
                        }
                        password != confirmPassword -> {
                            Toast.makeText(context, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            authViewModel.register(
                                email. trim(),
                                password,
                                displayName.trim()  // ← GỬI DISPLAYNAME
                            )
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier. size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("ĐĂNG KÝ")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("Đã có tài khoản? Đăng nhập")
            }
        }
    }
}