package com.example.unicanteen.HengJunEn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.OrderList
import com.example.unicanteen.database.OrderListDao
import com.example.unicanteen.database.OrderListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SellerOrderListViewModel(
    private val orderListRepository: OrderListRepository,
):ViewModel()
{
    private val _orderListDetails = MutableStateFlow<List<OrderListDao.OrderListItemDetails>>(emptyList())
    val orderListDetails: MutableStateFlow<List<OrderListDao.OrderListItemDetails>> get() = _orderListDetails

    fun loadOrderListDetails(sellerId: Int) {
        viewModelScope.launch {
            val orderList = orderListRepository.getOrderListDetailsBySellerId(sellerId)
            _orderListDetails.value = orderList
        }
    }

    // Update order status (either "Completed" or "Cancelled")
    fun updateOrderStatus(orderListItemDetails: OrderListDao.OrderListItemDetails, newStatus: String) {
        viewModelScope.launch {
            // Call repository to update order status based on orderListId (use a unique identifier)
            orderListRepository.updateOrderStatus(orderListItemDetails.orderListId, newStatus)

            loadOrderListDetails(orderListItemDetails.sellerId)
        }
    }
}