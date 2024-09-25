package com.example.unicanteen.database

import androidx.lifecycle.LiveData

class PierreAdminRepositoryImpl(private val orderListDao: OrderListDao) : PierreAdminRepository {
    override suspend fun getMonthlySalesByFoodType(month: String, sellerId: Int): LiveData<List<OrderListDao.FoodTypeSalesData>> {
        return orderListDao.getMonthlySalesByFoodType(month, sellerId)
    }

    override suspend fun getSalesByFoodType(foodType: String, sellerId: Int, month: String): LiveData<List<OrderListDao.FoodSalesData>> {
        return orderListDao.getSalesByFoodType(foodType, sellerId, month)
    }
}