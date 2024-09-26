package com.example.unicanteen.database

import androidx.lifecycle.LiveData

class PierreAdminRepositoryImpl(private val orderListDao: OrderListDao) : PierreAdminRepository {
    override suspend fun getMonthlySalesByFoodType(month: String, sellerId: Int): LiveData<List<OrderListDao.FoodTypeSalesData>> {
        return orderListDao.getMonthlySalesByFoodType(month, sellerId)
    }

    override suspend fun getSalesByFoodType(foodType: String, sellerId: Int, month: String): LiveData<List<OrderListDao.FoodSalesData>> {
        return orderListDao.getSalesByFoodType(foodType, sellerId, month)
    }
    override suspend fun getOrderDetailsByOrderIdAndUserId(orderId: Int, userId: Int): LiveData<List<OrderListDao.OrderDetailsData>> {
        return orderListDao.getOrderDetailsByOrderIdAndUserId(orderId, userId)
    }
    override suspend fun updateOrderTableNo(userId: Int, orderId: Int, tableNo: Int) {
        return orderListDao.updateOrderTableNo(userId, orderId, tableNo)
    }
    override suspend fun updateOrderType(orderId: Int, userId: Int, orderType: String) {
        orderListDao.updateOrderType(orderId, userId, orderType)
    }

    override suspend fun getTableNoByUserAndOrder(userId: Int, orderId: Int): Int {
        return orderListDao.getTableNoByUserAndOrder(userId, orderId)
    }

}