package com.example.foodorderav.ui.cart

import androidx.lifecycle.ViewModel
import com.example.foodorderav.data.model.CartItem
import com.example.foodorderav.data.model.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Tính tổng tiền
    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.totalPrice }
    }

    // Thêm món vào giỏ
    fun addToCart(food: Food, quantity: Int = 1) {
        val currentItems = _cartItems.value.toMutableList()

        val existingItemIndex = currentItems.indexOfFirst { it.food.id == food.id }

        if (existingItemIndex != -1) {
            val existingItem = currentItems[existingItemIndex]
            currentItems[existingItemIndex] = existingItem.copy(
                quantity = existingItem.quantity + quantity
            )
        } else {
            currentItems.add(CartItem(food, quantity))
        }

        _cartItems.value = currentItems
    }

    // Tăng số lượng
    fun increaseQuantity(cartItem: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.food.id == cartItem.food.id }

        if (index != -1) {
            currentItems[index] = cartItem.copy(quantity = cartItem.quantity + 1)
            _cartItems.value = currentItems
        }
    }

    // Giảm số lượng
    fun decreaseQuantity(cartItem: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.food.id == cartItem.food.id }

        if (index != -1) {
            if (cartItem.quantity > 1) {
                currentItems[index] = cartItem.copy(quantity = cartItem.quantity - 1)
            } else {
                currentItems.removeAt(index)
            }
            _cartItems.value = currentItems
        }
    }

    // Xóa món khỏi giỏ
    fun removeFromCart(cartItem: CartItem) {
        _cartItems.value = _cartItems.value.filter { it.food.id != cartItem.food.id }
    }

    // Xóa toàn bộ giỏ hàng
    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Lấy số lượng món trong giỏ
    fun getCartItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }
}