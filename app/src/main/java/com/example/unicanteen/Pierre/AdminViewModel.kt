package com.example.unicanteen.Pierre

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.AppDatabase
import com.example.unicanteen.database.OrderListDao
import com.example.unicanteen.database.PierreAdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val pierreAdminRepository: PierreAdminRepository
) : ViewModel() {

    private val _monthlySalesData = MutableStateFlow<List<OrderListDao.FoodTypeSalesData>>(emptyList())
    val monthlySalesData: StateFlow<List<OrderListDao.FoodTypeSalesData>> = _monthlySalesData
    private val _foodSalesData = MutableStateFlow<List<OrderListDao.FoodSalesData>>(emptyList())  // New state flow for food sales data
    val foodSalesData: StateFlow<List<OrderListDao.FoodSalesData>> = _foodSalesData  // Expose the food sales data

    private var sellerId: Int? = null  // Store sellerId when restaurant is selected


    // Function to load monthly sales data
    fun loadMonthlySales(month: String, sellerId: Int) {
        viewModelScope.launch {
            this@AdminViewModel.sellerId = sellerId  // Store sellerId

            // Observe LiveData from the repository
            pierreAdminRepository.getMonthlySalesByFoodType(month, sellerId).observeForever { salesData ->
                _monthlySalesData.value = salesData
            }
        }
    }
    // New function to load sales data by food type and seller ID
    fun loadSalesByFoodType(sellerId: Int, foodType: String, month: String) {
        viewModelScope.launch {
            // Fetch sales data using the repository method that includes sellerId, foodType, and month
            pierreAdminRepository.getSalesByFoodType(foodType, sellerId, month).observeForever { salesData ->
                _foodSalesData.value = salesData  // Update the food sales data
            }
        }
    }


}
