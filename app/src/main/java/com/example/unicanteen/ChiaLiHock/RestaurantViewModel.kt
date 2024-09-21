package com.example.unicanteen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestaurantViewModel : ViewModel() {
    private val _selectedRestaurant = MutableLiveData<Seller>()
    val selectedRestaurant: LiveData<Seller> get() = _selectedRestaurant

    fun selectRestaurant(restaurant: Seller) {
        _selectedRestaurant.value = restaurant
    }
}
