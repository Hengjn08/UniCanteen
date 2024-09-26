package com.example.unicanteen.database

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
}