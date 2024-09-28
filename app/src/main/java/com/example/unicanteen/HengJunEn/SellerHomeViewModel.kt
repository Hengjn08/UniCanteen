package com.example.unicanteen.HengJunEn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.Seller
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SellerHomeViewModel(private val foodListRepository: FoodListRepository): ViewModel(){
    private val _foods = MutableStateFlow<List<FoodList>>(emptyList())
    val foods: StateFlow<List<FoodList>> = _foods

    private var sellerId: Int? = null

    private val _shopName = MutableStateFlow<String?>(null)
    val shopName: StateFlow<String?> = _shopName

    fun displayFoodsBySellerId(sellerId: Int) {
        viewModelScope.launch {
            this@SellerHomeViewModel.sellerId = sellerId
            val foodList = foodListRepository.getFoodsBySellerId(sellerId)
            _foods.value = foodList
        }
    }

    fun updateFoodStatus(food: FoodList, isAvailable: Boolean) {
        viewModelScope.launch {
            val updatedFood = food.copy(status = if (isAvailable) "Available" else "Unavailable")
            foodListRepository.updateFood(updatedFood)
            // Re-fetch the food list to update UI
            sellerId?.let {
                _foods.value = foodListRepository.getFoodsBySellerId(it)
            }
        }
    }

    //get shopName by sellerId
    fun getShopNameBySellerId(sellerId: Int) {
        viewModelScope.launch {
            val shopName = foodListRepository.getShopNameBySellerId(sellerId)
            _shopName.value = shopName
        }
    }

}

