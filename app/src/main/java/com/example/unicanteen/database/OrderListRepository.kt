package com.example.unicanteen.database

interface OrderListRepository {
    suspend fun getOrderListBySellerId(sellerId: Int): List<OrderList>
    suspend fun insertOrder(orderList: OrderList)
    suspend fun getExistingOrderIdForUser(userId: Int, status: String): Int?
    suspend fun getOrderListItem(orderId: Int, sellerId: Int, foodId: Int): List<OrderList>
    suspend fun updateOrder(orderList: OrderList)
    suspend fun updateOrderListItem(orderListId: Int, newQuantity: Int, newTotalPrice: Double)
    suspend fun deleteOrderListById(orderListId: Int)

}