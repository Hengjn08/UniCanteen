package com.example.unicanteen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.Seller
import com.example.unicanteen.database.SellerRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SelectRestaurantViewModel(private val repository: SellerRepository) : ViewModel() {
    private val _sellers = MutableStateFlow<List<Seller>>(emptyList())
    val sellers: StateFlow<List<Seller>> = _sellers

    init {
        loadSellers() // Call the method to load sellers
    }

    private fun loadSellers() {
        viewModelScope.launch {
            _sellers.value = repository.getAllSellers() // This is a suspend function, safely called here
        }
    }

    fun filterSellersByStatus(status: String) {
        viewModelScope.launch {
            _sellers.value = repository.getSellersByStatus(status)
        }
    }

    fun filterSellersByRating(rating: Double) {
        viewModelScope.launch {
            _sellers.value = repository.getSellersWithHighRating(rating)
        }
    }
}

