package com.example.foodorderav.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderav.data.Result
import com.example.foodorderav.data.model.CartItem
import com.example.foodorderav.data.model.Order
import com.example.foodorderav.data.model.OrderItem
import com.example.foodorderav.data.repository.AuthRepository
import com.example.foodorderav.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository = OrderRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _orderHistory = MutableStateFlow<List<Order>>(emptyList())
    val orderHistory: StateFlow<List<Order>> = _orderHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _orderPlaced = MutableStateFlow(false)
    val orderPlaced: StateFlow<Boolean> = _orderPlaced.asStateFlow()

    // ← ĐẶT HÀNG (GỌI TỪ CHECKOUTSCREEN)
    fun placeOrder(
        cartItems: List<CartItem>,
        customerName: String,
        phone: String,
        address: String,
        note: String = "",
        paymentMethod: String = "COD",
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _orderPlaced.value = false

            val currentUser = authRepository.getCurrentUser()
            if (currentUser == null) {
                val errorMsg = "Vui lòng đăng nhập để đặt hàng"
                _error.value = errorMsg
                _isLoading.value = false
                onError(errorMsg)
                return@launch
            }

            // Validation
            if (customerName.isBlank() || phone.isBlank() || address.isBlank()) {
                val errorMsg = "Vui lòng điền đầy đủ thông tin"
                _error.value = errorMsg
                _isLoading.value = false
                onError(errorMsg)
                return@launch
            }

            // Chuyển đổi CartItem sang OrderItem
            val orderItems = cartItems.map { cartItem ->
                OrderItem(
                    foodId = cartItem.food.id,
                    foodName = cartItem.food.name,
                    price = cartItem.food.price,
                    quantity = cartItem.quantity
                )
            }

            val order = Order(
                userId = currentUser.uid,
                items = orderItems,
                totalPrice = cartItems.sumOf { it.totalPrice },
                customerName = customerName,
                phone = phone,
                address = address,
                note = note,
                paymentMethod = paymentMethod,
                status = "PENDING",
                createdAt = System.currentTimeMillis()
            )

            when (val result = orderRepository.createOrder(order)) {
                is Result.Success -> {
                    _orderPlaced.value = true
                    onSuccess()
                }
                is Result.Error -> {
                    val errorMsg = result.exception.message ?: "Lỗi đặt hàng"
                    _error.value = errorMsg
                    onError(errorMsg)
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    // Load lịch sử đơn hàng
    fun loadOrderHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val currentUser = authRepository.getCurrentUser()
            if (currentUser == null) {
                _error.value = "Vui lòng đăng nhập"
                _isLoading.value = false
                return@launch
            }

            when (val result = orderRepository.getOrderHistory(currentUser.uid)) {
                is Result.Success -> {
                    _orderHistory.value = result.data
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Lỗi tải lịch sử"
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    fun resetOrderPlaced() {
        _orderPlaced.value = false
    }

    fun clearError() {
        _error.value = null
    }
}