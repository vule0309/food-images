package com.example.foodorderav.ui.profile

import androidx.lifecycle. ViewModel
import androidx.lifecycle. viewModelScope
import com.example.foodorderav.data.Result
import com.example.foodorderav.data.model.User
import com.example.foodorderav.data.repository.AuthRepository
import kotlinx.coroutines.flow. MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User? > = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String? > = _error.asStateFlow()

    // ========== TRẠNG THÁI CẬP NHẬT THÀNH CÔNG ==========
    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess.asStateFlow()

    init {
        loadCurrentUser()
    }

    // ========== LOAD USER TỪ FIRESTORE (ĐẦY ĐỦ THÔNG TIN) ==========
    fun loadCurrentUser() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            when (val result = authRepository.getCurrentUserFromFirestore()) {
                is Result.Success -> {
                    _currentUser.value = result.data
                }
                is Result.Error -> {
                    _error.value = result.exception. message
                    // Fallback:  lấy từ Firebase Auth
                    _currentUser. value = authRepository.getCurrentUser()
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    // ========== CẬP NHẬT PROFILE ==========
    fun updateProfile(displayName : String,fullName: String, phone: String, address: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _updateSuccess.value = false

            when (val result = authRepository.updateUserProfile(displayName,fullName, phone, address)) {
                is Result. Success -> {
                    _currentUser.value = result.data
                    _updateSuccess.value = true
                }
                is Result.Error -> {
                    _error.value = result.exception. message ?: "Cập nhật thất bại"
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    fun resetUpdateSuccess() {
        _updateSuccess.value = false
    }

    fun logout() {
        authRepository.logout()
        _currentUser.value = null
    }

    fun clearError() {
        _error.value = null
    }
}