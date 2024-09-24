package com.example.unicanteen.Pierre

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.unicanteen.database.AppDatabase
import com.example.unicanteen.database.OrderListDao

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    private val orderListDao: OrderListDao

    init {
        val db = AppDatabase.getDatabase(application)
        orderListDao = db.orderListDao()
    }

    // Function to get sales data filtered by month and sellerId
    fun getMonthlySalesByFoodType(month: String, sellerId: Int): LiveData<List<OrderListDao.FoodTypeSalesData>> {
        return orderListDao.getMonthlySalesByFoodType(month, sellerId)
    }
}