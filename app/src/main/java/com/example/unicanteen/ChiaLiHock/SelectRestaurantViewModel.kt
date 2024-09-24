package com.example.unicanteen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.Seller
import com.example.unicanteen.database.SellerRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SelectRestaurantViewModel(repository: SellerRepository) : ViewModel() {
//    private val _sellers = MutableStateFlow<List<Seller>>(emptyList())
//    val sellers: StateFlow<List<Seller>> = _sellers
//
//    init {
//        loadSellers() // Call the method to load sellers
//    }
//
//    private fun loadSellers() {
//        viewModelScope.launch {
//            _sellers.value = repository.getAllSellers() // This is a suspend function, safely called here
//        }
//    }
//
//    fun filterSellersByStatus(status: String) {
//        viewModelScope.launch {
//            _sellers.value = repository.getSellersByStatus(status)
//        }
//    }
//
//    fun filterSellersByRating(rating: Double) {
//        viewModelScope.launch {
//            _sellers.value = repository.getSellersWithHighRating(rating)
//        }
//    }

    val selectRestaurantUiState: StateFlow<SelectRestaurantUiState> =
        repository.getAllSellers()
            .map { sellers ->
                SelectRestaurantUiState(sellers) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = SelectRestaurantUiState()
            )
}

//class SellerHomeViewModel(
//    private val foodRepository: FoodListRepository,
//    private val sellerId: Int
//) : ViewModel() {
//
//    // Exposing the list of food items as a StateFlow
//    val sellerHomeUiState: StateFlow<SellerHomeUiState> =
//        foodRepository.getFoodItemsBySellerId(sellerId)
//            .map { foodList -> SellerHomeUiState(foodList) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000L),
//                initialValue = SellerHomeUiState()
//            )
//}
//
data class SelectRestaurantUiState(val itemList: List<Seller> = listOf())