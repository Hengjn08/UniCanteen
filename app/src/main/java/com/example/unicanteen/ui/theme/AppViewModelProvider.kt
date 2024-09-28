package com.example.unicanteen.ui.theme

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unicanteen.ChiaLiHock.AddOnViewModel
import com.example.unicanteen.ChiaLiHock.CartViewModel
import com.example.unicanteen.ChiaLiHock.FoodDetailViewModel
import com.example.unicanteen.ChiaLiHock.OrderListViewModel
import com.example.unicanteen.ChiaLiHock.SelectFoodViewModel
import com.example.unicanteen.HengJunEn.AddFoodViewModel
import com.example.unicanteen.HengJunEn.EditFoodViewModel
import com.example.unicanteen.HengJunEn.SellerFoodDetailsViewModel
import com.example.unicanteen.HengJunEn.SellerHomeViewModel
import com.example.unicanteen.HengJunEn.SellerOrderListViewModel
import com.example.unicanteen.LimSiangShin.UserViewModel
import com.example.unicanteen.Pierre.AdminViewModel
import com.example.unicanteen.SelectRestaurantViewModel
import com.example.unicanteen.database.AddOnRepository
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import com.example.unicanteen.database.PierreAdminRepository
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.database.UserRepository


/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val application: Application, // Application parameter for ViewModel initialization
        private val sellerRepository: SellerRepository? = null,
        private val foodListRepository: FoodListRepository? = null,
        private val pierreAdminRepository: PierreAdminRepository? = null,
        private val addOnRepository: AddOnRepository? = null,
        private val userRepository: UserRepository? = null,
        private val orderListRepository: OrderListRepository? = null,
        private val orderRepository: OrderRepository? = null
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(SelectRestaurantViewModel::class.java) -> {
                    sellerRepository?.let { SelectRestaurantViewModel(it) } as T
                }
                modelClass.isAssignableFrom(SelectFoodViewModel::class.java) -> {
                    foodListRepository?.let { SelectFoodViewModel(it) } as T
                }
                modelClass.isAssignableFrom(FoodDetailViewModel::class.java) -> {
                    foodListRepository?.let { FoodDetailViewModel(it) } as T
                }
                modelClass.isAssignableFrom(AdminViewModel::class.java) -> {
                    pierreAdminRepository?.let { AdminViewModel(it, application) } as T
                }
                modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                    userRepository?.let { UserViewModel(it) } as T
                }
                modelClass.isAssignableFrom(AddOnViewModel::class.java) -> {
                    addOnRepository?.let { AddOnViewModel(it) } as T
                }
                modelClass.isAssignableFrom(SellerHomeViewModel::class.java) -> {
                    foodListRepository?.let { SellerHomeViewModel(it) } as T
                }
                modelClass.isAssignableFrom(SellerFoodDetailsViewModel::class.java) -> {
                    foodListRepository?.let { SellerFoodDetailsViewModel(it) } as T
                }
                modelClass.isAssignableFrom(AddFoodViewModel::class.java) -> {
                    foodListRepository?.let { AddFoodViewModel(it) } as T
                }
                modelClass.isAssignableFrom(SellerOrderListViewModel::class.java) -> {
                    orderListRepository?.let { SellerOrderListViewModel(it) } as T
                }
                modelClass.isAssignableFrom(OrderListViewModel::class.java) -> {
                    orderListRepository?.let { OrderListViewModel(it, orderRepository!!) } as T
                }
                modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                    orderRepository?.let { CartViewModel(it, orderListRepository!!) } as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
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
