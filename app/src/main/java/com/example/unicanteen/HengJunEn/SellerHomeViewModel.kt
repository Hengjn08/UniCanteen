package com.example.unicanteen.HengJunEn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SellerHomeViewModel(private val repository: FoodListRepository) : ViewModel() {

    // MutableStateFlow to hold the list of food items for the seller
    private val _foodItems = MutableStateFlow<List<FoodList>>(emptyList())
    val foodItems: StateFlow<List<FoodList>> = _foodItems

    // Method to load food items by seller ID
    fun loadFoodItemsBySellerId(sellerId: Long) {
        viewModelScope.launch {
            _foodItems.value = repository.getFoodItemsBySellerId(sellerId)
        }
    }
}