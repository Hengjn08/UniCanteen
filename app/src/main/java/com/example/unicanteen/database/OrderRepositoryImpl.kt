package com.example.unicanteen.database

import com.example.unicanteen.ChiaLiHock.CartItem
import com.example.unicanteen.ChiaLiHock.CartViewModel

class OrderRepositoryImpl(private val orderDao: OrderDao): OrderRepository {
    override suspend fun insertOrder(order: Order):Long{
        return orderDao.insertOrder(order)
    }
    override suspend fun updateOrder(order: Order){
        return orderDao.updateOrder(order)
    }
    override suspend fun deleteOrder(order: Order){
        return orderDao.deleteOrder(order)
    }
    override suspend fun getOrderById(orderId: Int): Order?{
        return orderDao.getOrderById(orderId)
    }
    override suspend fun getOrdersByUserId(userId: Int): List<Order>{
        return orderDao.getOrdersByUserId(userId)
    }
    override suspend fun getAllOrders(): List<Order> {
        return orderDao.getAllOrders()
    }
    override suspend fun getOrdersByUserIdAndStatus(userId: Int, orderStatus: String): List<Order> {
        return orderDao.getOrdersByUserIdAndStatus(userId, orderStatus)
    }
    override suspend fun getPendingOrderItems(orderId: Int): List<CartItem> {
        return orderDao.getPendingOrderItems(orderId)
    }
    override suspend fun updateOrderPrice(orderId: Int, Price: Double) {
        return orderDao.updateOrderPrice(orderId, Price)
    }
    override suspend fun deleteOrderById(orderId: Int) {
        return orderDao.deleteOrderById(orderId)
    }
    override suspend fun deleteOrderByUserId(userId: Int) {
        return orderDao.deleteOrderByUserId(userId)
    }




}