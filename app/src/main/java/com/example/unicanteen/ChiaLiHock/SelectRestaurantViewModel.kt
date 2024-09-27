package com.example.unicanteen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.Seller
import com.example.unicanteen.database.SellerRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectRestaurantViewModel(private val repository: SellerRepository) : ViewModel() {
    private val _sellers = MutableStateFlow<List<Seller>>(emptyList())
    val sellers: StateFlow<List<Seller>> = _sellers.asStateFlow()

    private  val _singleSeller = MutableStateFlow<Seller?>(null)
    val singleSeller: StateFlow<Seller?> = _singleSeller.asStateFlow()

    init {
        loadSellers() // Call the method to load sellers
    }

    private fun loadSellers() {
        viewModelScope.launch {
            _sellers.value = repository.getAllSellers() // This is a suspend function, safely called here
        }
    }
    fun searchSellersByName(query: String) {
        viewModelScope.launch {
            _sellers.value = if (query.isEmpty()) {
                repository.getAllSellers()
            } else {
                repository.searchSellersByName(query)
            }
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
    fun submitRating(sellerId: Int, rating: Double) {
        viewModelScope.launch {
            val seller = repository.getSellerById(sellerId)
            if (seller != null) {
                seller.shopRating = (seller.shopRating*seller.ratingNumber + rating)/(seller.ratingNumber+1)
                seller.ratingNumber = seller.ratingNumber + 1
                repository.updateSeller(seller)
                // Optionally: Update _singleSeller so that the UI reflects the updated rating immediately
                _singleSeller.value = seller
            }
        }
    }
    fun getSellerById(sellerId: Int) {
        viewModelScope.launch {
            _singleSeller.value = repository.getSellerById(sellerId)
        }
    }
}

