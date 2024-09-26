package com.example.unicanteen.database

class OrderListRepositoryImpl( private val OrderListDao: OrderListDao): OrderListRepository {
    override suspend fun getOrderListBySellerId(sellerId: Int): List<OrderList>{
        return OrderListDao.getOrderListBySellerId(sellerId)
    }
}