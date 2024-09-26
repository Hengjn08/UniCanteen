package com.example.unicanteen.database

interface OrderListRepository {
    suspend fun getOrderListBySellerId(sellerId: Int): List<OrderList>
    suspend fun insertOrder(orderList: OrderList)
    suspend fun getExistingOrderIdForUser(userId: Int, status: String): Int?
}