package com.example.unicanteen.ChiaLiHock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import kotlinx.coroutines.launch

class CartViewModel(
    private val orderRepository: OrderRepository,
    private val orderListRepository: OrderListRepository
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    fun getCartItems(userId: Int) {
        viewModelScope.launch {
            val items = orderRepository.getPendingOrderItems(userId)
            _cartItems.value = items
        }
    }

    fun updateOrderItem(orderListId: Int, newQuantity: Int, unitPrice: Double,userId: Int) {
        val newTotalPrice = newQuantity * unitPrice  // Calculate new total price

        viewModelScope.launch {
            orderListRepository.updateOrderListItem(orderListId, newQuantity, newTotalPrice)
        }
        getCartItems(userId)
    }
    fun updateOrderPrice(orderId: Int, Price: Double) {
        viewModelScope.launch {
            orderRepository.updateOrderPrice(orderId, Price)
        }
    }
    fun deleteOrderItem(orderListId: Int,userId: Int) {
        viewModelScope.launch {
            orderListRepository.deleteOrderListById(orderListId)
        }
        getCartItems(userId)
    }
    fun deleteOrderByUserId(userId: Int) {
        viewModelScope.launch {
            orderRepository.deleteOrderByUserId(userId)
        }
    }
}

data class CartItem(
    @ColumnInfo(name = "orderId") val orderId: Int,
    @ColumnInfo(name = "orderListId") val orderListId: Int,
    @ColumnInfo(name = "foodName") val name: String,
    @ColumnInfo(name = "imageUrl") val imageRes: String,
    @ColumnInfo(name = "totalPrice") var price: Double,
    @ColumnInfo(name = "qty") var quantity: Int,
    )