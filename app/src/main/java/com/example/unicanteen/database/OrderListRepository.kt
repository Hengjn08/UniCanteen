package com.example.unicanteen.database

import com.example.unicanteen.database.OrderListDao.OrderListItemDetails

interface OrderListRepository {
    suspend fun getOrderListBySellerId(sellerId: Int): List<OrderList>
    suspend fun updateOrderList(orderList: OrderList)
    suspend fun getOrderListDetailsBySellerId(sellerId: Int): List<OrderListItemDetails>
    suspend fun updateOrderStatus(orderListId: Int, newStatus: String)
}