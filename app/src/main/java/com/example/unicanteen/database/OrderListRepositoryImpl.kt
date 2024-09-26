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
    override suspend fun getOrderListItem(orderId: Int, sellerId: Int, foodId: Int): List<OrderList> {
        return OrderListDao.getOrderListItem(orderId, sellerId, foodId)
    }
    override suspend fun updateOrder(orderList: OrderList) {
        OrderListDao.updateOrderList(orderList)
    }
    override suspend fun updateOrderListItem(orderListId: Int, newQuantity: Int, newTotalPrice: Double) {
        return OrderListDao.updateOrderListItem(orderListId, newQuantity, newTotalPrice)
    }
    override suspend fun deleteOrderListById(orderListId: Int) {
        return OrderListDao.deleteOrderListById(orderListId)
    }

}