package com.example.unicanteen.ChiaLiHock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.Order
import com.example.unicanteen.database.OrderList
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import kotlinx.coroutines.launch

class OrderListViewModel(
    private val orderListRepository: OrderListRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    fun addOrderListItem(
        sellerId: Int,
        foodId: Int,
        userId: Int,
        qty: Int,
        totalPrice: Double,
        remark: String?,
        createDate: String,
        price: Double
    ) {

        viewModelScope.launch {
            // Check if there is an existing "Pending" order for the user
            val existingOrderId = orderListRepository.getExistingOrderIdForUser(userId, "Pending")
            val orderId = existingOrderId ?: createNewOrder(userId, createDate).toInt() // Create a new order if none exists

            // Fetch all existing order items for the order
            val existingOrderItems = orderListRepository.getOrderListItem(orderId, sellerId, foodId)

            // Find the exact match of item based on totalPrice (unit price)
            val existingOrderItem = existingOrderItems?.find { (it.totalPrice / it.qty) == (totalPrice / qty) }

            if (existingOrderItem != null) {
                // Update the quantity if the item already exists
                val updatedQty = existingOrderItem.qty + qty
                val updatedTotalPrice = updatedQty * totalPrice / qty // Calculate the new total price
                val updatedOrderItem = existingOrderItem.copy(qty = updatedQty, totalPrice = updatedTotalPrice)
                orderListRepository.updateOrder(updatedOrderItem)
            } else {
                // Create a new order list item if no match is found
                val orderListItem = OrderList(
                    sellerId = sellerId,
                    foodId = foodId,
                    userId = userId,
                    orderId = orderId,
                    qty = qty,
                    totalPrice = totalPrice,
                    status = "Pending", // Assuming "Pending" is the initial status
                    createDate = createDate,
                    remark = remark
                )
                orderListRepository.insertOrder(orderListItem)
            }
        }
    }


    private suspend fun createNewOrder(userId: Int, createDate: String): Long {
        // Logic to insert a new order and return the new orderId
        val newOrder = Order(
            userId = userId,
            status = "Pending",
            createDate = createDate,
            orderType = "Pick up",
            totalPrice = 0.0
        )
        // Implement the method in your repository to insert the new order
        val newOrderId = orderRepository.insertOrder(newOrder)
        return newOrderId
    }
}

