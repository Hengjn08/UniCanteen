package com.example.unicanteen.HengJunEn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.OrderList
import com.example.unicanteen.database.OrderListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//class SellerOrderListViewModel(private val orderListRepository: OrderListRepository):ViewModel()
//{
//    private val _orderListDetails = MutableStateFlow<OrderList?>(null)
//    val orderListDetails: StateFlow<OrderList?> get() = _foodDetails
//    fun loadOrderListDetails(foodId: Int) {
//        viewModelScope.launch {
//            val food = foodListRepository.getFoodById(foodId)
//            _foodDetails.value = food
//        }
//    }
//}