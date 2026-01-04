package com.example.foodorderav.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderav.data.Result
import com.example.foodorderav.data.model.Food
import com.example.foodorderav.data.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: FoodRepository = FoodRepository()
) : ViewModel() {

    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods: StateFlow<List<Food>> = _foods.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var allFoods: List<Food> = emptyList()

    init {
        loadFoods()
    }

    private fun loadFoods() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            when (val result = repository.getAllFoods()) {
                is Result.Success -> {
                    allFoods = result.data
                    _foods.value = result.data
                }
                is Result.Error -> {
                    _error.value = result.exception.message ?: "Unknown error"
                }
                else -> {}
            }

            _isLoading.value = false
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        filterFoods(query)
    }

    private fun filterFoods(query: String) {
        _foods.value = if (query.isBlank()) {
            allFoods
        } else {
            allFoods.filter { food ->
                food.name.contains(query, ignoreCase = true) ||
                        food.description.contains(query, ignoreCase = true) ||
                        food.category.contains(query, ignoreCase = true)
            }
        }
    }

    fun retry() {
        loadFoods()
    }
}