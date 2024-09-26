package com.example.unicanteen.database

interface OrderListRepository {
    suspend fun getOrderListBySellerId(sellerId: Int): List<OrderList>

}