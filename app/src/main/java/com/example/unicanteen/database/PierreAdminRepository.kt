package com.example.unicanteen.database

import androidx.lifecycle.LiveData

interface PierreAdminRepository {
    suspend fun getMonthlySalesByFoodType(month: String, sellerId: Int): LiveData<List<OrderListDao.FoodTypeSalesData>>
    suspend fun getSalesByFoodType(foodType: String, sellerId: Int,month: String): LiveData<List<OrderListDao.FoodSalesData>>  // New method
    suspend fun getOrderDetailsByOrderIdAndUserId(orderId: Int, userId: Int): LiveData<List<OrderListDao.OrderDetailsData>>  // New method
}
