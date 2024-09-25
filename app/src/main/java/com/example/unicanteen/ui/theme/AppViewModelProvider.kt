package com.example.unicanteen.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.unicanteen.ChiaLiHock.SelectFoodViewModel
import com.example.unicanteen.LimSiangShin.UserViewModel
import com.example.unicanteen.Pierre.AdminViewModel
import com.example.unicanteen.SelectRestaurantViewModel
import com.example.unicanteen.UniCanteenApp
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.PierreAdminRepository
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.database.UserRepository


/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: SellerRepository? = null,
        private val repository2: FoodListRepository? = null,
        private val repository3: PierreAdminRepository? = null,
        private val repository5: UserRepository? = null


    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SelectRestaurantViewModel::class.java)) {
                return repository?.let { SelectRestaurantViewModel(it) } as T
            } else if (modelClass.isAssignableFrom(SelectFoodViewModel::class.java)) {
                return repository2?.let { SelectFoodViewModel(it) } as T
            } else if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
                return repository3?.let {AdminViewModel(it)} as T // Add this line to handle AdminViewModel
            } else if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return repository4?.let { UserViewModel(it) } as T // Add this line to handle AdminViewModel
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    }
//    val Factory = viewModelFactory {
//        initializer {
//            SelectRestaurantViewModel(
//                UniCanteenApplication().container.sellerRepository
//            )
//        }
//        initializer {
//            SelectFoodViewModel(foodListRepository = foodListRepository)
//    }
