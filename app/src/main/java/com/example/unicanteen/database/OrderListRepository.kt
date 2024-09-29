package com.example.unicanteen.database

import com.example.unicanteen.database.OrderListDao.OrderListItemDetails

interface OrderListRepository {
    suspend fun getOrderListBySellerId(sellerId: Int): List<OrderList>
    suspend fun updateOrderList(orderList: OrderList)
    suspend fun getOrderListDetailsBySellerId(sellerId: Int): List<OrderListItemDetails>
    suspend fun updateOrderStatus(orderListId: Int, newStatus: String)
    suspend fun insertOrder(orderList: OrderList): Long
    suspend fun getExistingOrderIdForUser(userId: Int, status: String): Int?
    suspend fun getOrderListItem(orderId: Int, sellerId: Int, foodId: Int): List<OrderList>
    suspend fun updateOrder(orderList: OrderList)
    suspend fun updateOrderListItem(orderListId: Int, newQuantity: Int, newTotalPrice: Double)
    suspend fun deleteOrderListById(orderListId: Int)
    suspend fun getOrderListByOrderListId(orderListId: Int): OrderList?
}