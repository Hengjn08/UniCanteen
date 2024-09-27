package com.example.unicanteen.database

import com.example.unicanteen.database.OrderListDao.OrderListItemDetails

class OrderListRepositoryImpl( private val orderListDao: OrderListDao): OrderListRepository {
    override suspend fun getOrderListBySellerId(sellerId: Int): List<OrderList>{
        return orderListDao.getOrderListBySellerId(sellerId)
    }
    override suspend fun updateOrderList(orderList: OrderList){
        orderListDao.updateOrderList(orderList)
    }
    override suspend fun getOrderListDetailsBySellerId(sellerId: Int): List<OrderListItemDetails>{
        return orderListDao.getOrderListDetailsBySellerId(sellerId)
    }
    override suspend fun updateOrderStatus(orderListId: Int, newStatus: String){
        orderListDao.updateOrderStatus(orderListId, newStatus)
    }
}