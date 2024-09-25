package com.example.unicanteen.Pierre

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    // New state flow for order details
    private val _orderDetailsData = MutableStateFlow<List<OrderListDao.OrderDetailsData>>(emptyList())
    val orderDetailsData: StateFlow<List<OrderListDao.OrderDetailsData>> = _orderDetailsData  // Expose order details data

    private var sellerId: Int? = null  // Store sellerId when restaurant is selected
    var updateStatusMessage by mutableStateOf<String?>(null)
        private set


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

    // New function to load order details by orderId and userId
    fun loadOrderDetails(orderId: Int, userId: Int) {
        viewModelScope.launch {
            // Fetch order details using the repository method
            pierreAdminRepository.getOrderDetailsByOrderIdAndUserId(orderId, userId).observeForever { orderDetails ->
                _orderDetailsData.value = orderDetails  // Update the order details data
            }
        }
    }

    // Modify the updateTableNo function to take a callback
//    fun updateTableNo(userId: Int, orderId: Int, tableNo: Int, onResult: (Boolean) -> Unit) {
//        viewModelScope.launch {
//            try {
//                // Call the repository method to update table number
//                pierreAdminRepository.updateOrderTableNo(1, 1, 3)
//                // Success logic
//            } catch (e: Exception) {
//                // Handle the exception, possibly logging it
//            }
//        }
//    }
    // Function to update the table number in the database
    fun updateTableNo(userId: Int, orderId: Int, tableNo: Int) {
        viewModelScope.launch {
            try {
                // Call the repository method to update the table number
                pierreAdminRepository.updateOrderTableNo(userId, orderId, tableNo)
                updateStatusMessage = "Table number updated successfully" // Success logic
            } catch (e: Exception) {
                updateStatusMessage = "Failed to update table number" // Handle the exception
                // Optionally log the error here
                e.printStackTrace()
            }
        }
    }

    fun updateOrderType(orderId: Int, userId: Int, orderType: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                pierreAdminRepository.updateOrderType(orderId, userId, orderType)
                onComplete(true)  // Notify success
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)  // Notify failure
            }
        }
    }


}
