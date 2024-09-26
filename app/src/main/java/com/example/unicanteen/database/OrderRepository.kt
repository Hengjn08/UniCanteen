package com.example.unicanteen.database

interface OrderRepository {
    suspend fun insertOrder(order: Order): Long
    suspend fun updateOrder(order: Order)
    suspend fun deleteOrder(order: Order)
    suspend fun getOrderById(orderId: Int): Order?
    suspend fun getOrdersByUserId(userId: Int): List<Order>
    suspend fun getAllOrders(): List<Order>

}