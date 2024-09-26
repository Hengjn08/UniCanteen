package com.example.unicanteen.database

import com.example.unicanteen.ChiaLiHock.CartItem
import com.example.unicanteen.ChiaLiHock.CartViewModel

interface OrderRepository {
    suspend fun insertOrder(order: Order): Long
    suspend fun updateOrder(order: Order)
    suspend fun deleteOrder(order: Order)
    suspend fun getOrderById(orderId: Int): Order?
    suspend fun getOrdersByUserId(userId: Int): List<Order>
    suspend fun getAllOrders(): List<Order>
    suspend fun getOrdersByUserIdAndStatus(userId: Int, orderStatus: String): List<Order>
    suspend fun getPendingOrderItems(orderId: Int): List<CartItem>
    suspend fun updateOrderPrice(orderId: Int, Price: Double)
    suspend fun deleteOrderById(orderId: Int)
    suspend fun deleteOrderByUserId(userId: Int)

}