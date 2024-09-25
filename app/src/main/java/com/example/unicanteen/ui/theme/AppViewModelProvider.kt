package com.example.unicanteen.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unicanteen.ChiaLiHock.AddOnViewModel
import com.example.unicanteen.ChiaLiHock.FoodDetailViewModel
import com.example.unicanteen.ChiaLiHock.SelectFoodViewModel
import com.example.unicanteen.SelectRestaurantViewModel
import com.example.unicanteen.database.AddOnRepository
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.SellerRepository


/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: SellerRepository? = null,
        private val repository2: FoodListRepository? = null,
        private val repository3: AddOnRepository? = null

    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SelectRestaurantViewModel::class.java)) {
                return repository?.let { SelectRestaurantViewModel(it) } as T
            }
            else if( modelClass.isAssignableFrom(SelectFoodViewModel::class.java)){
                return repository2?.let { SelectFoodViewModel(it) } as T
            }
            else if( modelClass.isAssignableFrom(FoodDetailViewModel::class.java)){
                return repository2?.let { FoodDetailViewModel(it) } as T
            }
            else if( modelClass.isAssignableFrom(AddOnViewModel::class.java)){
                return repository3?.let { AddOnViewModel(it) } as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}