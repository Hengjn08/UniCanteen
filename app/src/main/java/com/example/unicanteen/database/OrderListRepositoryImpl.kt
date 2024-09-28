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
    override suspend fun insertOrder(orderList: OrderList){
        return orderListDao.insertOrderList(orderList)
    }
    override suspend fun getExistingOrderIdForUser(userId: Int, status: String): Int? {
        return orderListDao.getExistingOrderIdForUser(userId, status)
    }
    override suspend fun getOrderListItem(orderId: Int, sellerId: Int, foodId: Int): List<OrderList> {
        return orderListDao.getOrderListItem(orderId, sellerId, foodId)
    }
    override suspend fun updateOrder(orderList: OrderList) {
        orderListDao.updateOrderList(orderList)
    }
    override suspend fun updateOrderListItem(orderListId: Int, newQuantity: Int, newTotalPrice: Double) {
        return orderListDao.updateOrderListItem(orderListId, newQuantity, newTotalPrice)
    }
    override suspend fun deleteOrderListById(orderListId: Int) {
        return orderListDao.deleteOrderListById(orderListId)
    }

    override suspend fun getOrderListByUserId(userId: Int): List<OrderList> {
        return orderListDao.getOrderListByUserId(userId)
    }

}