package com.example.unicanteen.ui.theme

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.unicanteen.SelectRestaurantViewModel
import com.example.unicanteen.database.SellerRepository


/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: SellerRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SelectRestaurantViewModel::class.java)) {
                return SelectRestaurantViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}