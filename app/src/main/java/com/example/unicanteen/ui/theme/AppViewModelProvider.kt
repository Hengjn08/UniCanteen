package com.example.unicanteen.ui.theme

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.unicanteen.HengJunEn.SellerHomeViewModel
import com.example.unicanteen.SelectRestaurantViewModel
import com.example.unicanteen.UniCanteenApplication
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.SellerRepository


/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for SellerHomeViewModel
        initializer {
            SellerHomeViewModel(
                repository = UniCanteenApplication().container.foodRepository
            )
        }

        // Initializer for SelectRestaurantViewModel
        initializer {
            SelectRestaurantViewModel(
               UniCanteenApplication().container.sellerRepository
            )
        }

        // Add more ViewModel initializers here as needed
    }
}