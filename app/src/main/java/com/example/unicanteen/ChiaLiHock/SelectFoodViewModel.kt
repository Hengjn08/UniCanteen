package com.example.unicanteen.ChiaLiHock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectFoodViewModel(
    private val foodListRepository: FoodListRepository
) : ViewModel() {

    // LiveData for the list of foods
    private val _foods = MutableStateFlow<List<FoodList>>(emptyList())
    val foods: StateFlow<List<FoodList>> = _foods

    // LiveData for the filtered food list based on selected type
    private val _filteredFoods = MutableStateFlow<List<FoodList>>(emptyList())
    val filteredFoods: StateFlow<List<FoodList>> = _filteredFoods

    // LiveData for the selected food type
    private val _selectedFoodType = MutableStateFlow<String?>(null)
    val selectedFoodType: StateFlow<String?> = _selectedFoodType

    private val _foodTypes = MutableStateFlow<List<String>>(emptyList())
    val foodTypes: StateFlow<List<String>> = _foodTypes

    private val _shopName = MutableStateFlow<String?>(null)
    val shopName: StateFlow<String?> = _shopName
    // Load foods by seller ID
    fun loadFoodsBySellerId(sellerId: Int) {
        viewModelScope.launch {
            val foodList = foodListRepository.getFoodsBySellerIdAndStatus(sellerId,"Available")
            _foods.value = foodList
            _filteredFoods.value = foodList // Initially show all foods
        }
    }

    // Search foods by name
    fun searchFoodsByName(query: String) {
        _filteredFoods.value = _foods.value.filter { it.foodName.contains(query, ignoreCase = true) }
    }

    // Filter foods by the selected food type
    fun filterFoodsByType(type: String) {
        _selectedFoodType.value = type

        // Filter based on food type; "All" shows all foods
        _filteredFoods.value = if (type == "All") {
            _foods.value
        } else {
            _foods.value.filter { it.type == type }
        }
    }

    fun loadFoodTypeBySellerId(sellerId: Int) {
        viewModelScope.launch {
            val foodList = foodListRepository.getFoodTypeBySellerId(sellerId)
            _foodTypes.value = foodList
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


