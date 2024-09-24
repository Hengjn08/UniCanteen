package com.example.unicanteen.ChiaLiHock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectFoodViewModel(
    private val foodListRepository: FoodListRepository
) : ViewModel() {

    private val _foods = MutableStateFlow<List<FoodList>>(emptyList())
    val foods: StateFlow<List<FoodList>> = _foods

    private var sellerId: Int? = null  // Store sellerId when restaurant is selected

    // Function to load foods by sellerId
    fun loadFoodsBySellerId(sellerId: Int) {
        viewModelScope.launch {
            this@SelectFoodViewModel.sellerId = sellerId
            val foodList = foodListRepository.getFoodsBySellerId(sellerId)
            _foods.value = foodList
        }
    }

    // Function to search foods by name for the selected seller
    fun searchFoodsByName(query: String) {
        viewModelScope.launch {
            sellerId?.let {
                val filteredFoods = foodListRepository.searchFoodItemsByName(it, query)
                _foods.value = filteredFoods
            }
        }
    }
}

