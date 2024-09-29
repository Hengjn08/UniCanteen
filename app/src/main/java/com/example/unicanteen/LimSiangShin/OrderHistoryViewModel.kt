package com.example.unicanteen.LimSiangShin

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.database.OrderList
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.Seller
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.database.User
import com.example.unicanteen.database.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderHistoryViewModel(
    private val orderListRepository: OrderListRepository
): ViewModel(){

    private val _orderList = MutableStateFlow<List<OrderList>>(emptyList())
    val orderList: StateFlow<List<OrderList>> = _orderList.asStateFlow()

    private val _image = MutableStateFlow<List<Seller>>(emptyList())
    val image: StateFlow<List<Seller>> = _image.asStateFlow()

    fun loadOrderList(userId:Int) {
        viewModelScope.launch {
            _orderList.value = orderListRepository.getOrderListByUserId(userId) // This is a suspend function, safely called here
        }
    }

    fun loadShopeImage(orderListId:Int){
        viewModelScope.launch {

        }
    }
}

