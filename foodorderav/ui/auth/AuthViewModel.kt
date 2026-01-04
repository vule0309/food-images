package com.example.foodorderav.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com. example.foodorderav.data. Result
import com.example.foodorderav.data.model.User
import com.example.foodorderav.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines. launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User? > = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _authSuccess = MutableStateFlow(false)
    val authSuccess: StateFlow<Boolean> = _authSuccess.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        _currentUser.value = repository.getCurrentUser()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _authSuccess.value = false

            when (val result = repository.login(email, password)) {
                is Result.Success -> {
                    _currentUser.value = result.data
                    _authSuccess.value = true
                }
                is Result.Error -> {
                    _error.value = result.exception. message ?: "Đăng nhập thất bại"
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    // ← CẬP NHẬT HÀM NÀY
    fun register(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _authSuccess.value = false

            when (val result = repository.register(email, password, displayName)) {
                is Result.Success -> {
                    _currentUser.value = result. data
                    _authSuccess. value = true
                }
                is Result.Error -> {
                    _error.value = result. exception.message ?: "Đăng ký thất bại"
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    fun logout() {
        repository.logout()
        _currentUser.value = null
    }

    fun clearError() {
        _error. value = null
    }

    fun resetAuthSuccess() {
        _authSuccess.value = false
    }
}
