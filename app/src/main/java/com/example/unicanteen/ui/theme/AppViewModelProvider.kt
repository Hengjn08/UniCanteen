package com.example.unicanteen.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.unicanteen.ChiaLiHock.AddOnViewModel
import com.example.unicanteen.ChiaLiHock.FoodDetailViewModel
import com.example.unicanteen.ChiaLiHock.SelectFoodViewModel
import com.example.unicanteen.LimSiangShin.UserViewModel
import com.example.unicanteen.Pierre.AdminViewModel
import com.example.unicanteen.SelectRestaurantViewModel
import com.example.unicanteen.UniCanteenApp
import com.example.unicanteen.database.AddOnRepository
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
        private val sellerRepository: SellerRepository? = null,
        private val foodListRepository: FoodListRepository? = null,
        private val pierreAdminRepository: PierreAdminRepository? = null,
        private val addOnRepository: AddOnRepository? = null,
        private val userRepository: UserRepository? = null
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SelectRestaurantViewModel::class.java)) {
                return sellerRepository?.let { SelectRestaurantViewModel(it) } as T
            } else if (modelClass.isAssignableFrom(SelectFoodViewModel::class.java)) {
                return foodListRepository?.let { SelectFoodViewModel(it) } as T
            } else if( modelClass.isAssignableFrom(FoodDetailViewModel::class.java)){
                return foodListRepository?.let { FoodDetailViewModel(it) } as T
            }
            else if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
                return pierreAdminRepository?.let {AdminViewModel(it)} as T // Add this line to handle AdminViewModel
            } else if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return userRepository?.let { UserViewModel(it) } as T // Add this line to handle AdminViewModel
            }
            else if( modelClass.isAssignableFrom(AddOnViewModel::class.java)){
                return addOnRepository?.let { AddOnViewModel(it) } as T
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
