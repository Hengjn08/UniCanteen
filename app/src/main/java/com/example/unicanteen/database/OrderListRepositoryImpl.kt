package com.example.unicanteen.database

class OrderListRepositoryImpl( private val OrderListDao: OrderListDao): OrderListRepository {
    override suspend fun getOrderListBySellerId(sellerId: Int): List<OrderList>{
        return OrderListDao.getOrderListBySellerId(sellerId)
    }
    override suspend fun insertOrder(orderList: OrderList){
        return OrderListDao.insertOrderList(orderList)
    }
    override suspend fun getExistingOrderIdForUser(userId: Int, status: String): Int? {
        return OrderListDao.getExistingOrderIdForUser(userId, status)
    }
}