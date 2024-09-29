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
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


// Data class definition
data class PierreTest(
    val name: String
)
class AdminViewModel(
    private val pierreAdminRepository: PierreAdminRepository,
    application: Application
) : ViewModel() {
    // Use the specific URL for your Firebase Realtime Database
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance("https://unicanteen12-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .getReference() // Firebase Database reference

    private val _monthlySalesData = MutableStateFlow<List<OrderListDao.FoodTypeSalesData>>(emptyList())
    val monthlySalesData: StateFlow<List<OrderListDao.FoodTypeSalesData>> = _monthlySalesData
    private val _foodSalesData = MutableStateFlow<List<OrderListDao.FoodSalesData>>(emptyList())  // New state flow for food sales data
    val foodSalesData: StateFlow<List<OrderListDao.FoodSalesData>> = _foodSalesData  // Expose the food sales data
    // New state flow for order details
    private val _orderDetailsData = MutableStateFlow<List<OrderListDao.OrderDetailsData>>(emptyList())
    val orderDetailsData: StateFlow<List<OrderListDao.OrderDetailsData>> = _orderDetailsData  // Expose order details data
    //payment receipt
    private val _paymentReceiptData = MutableStateFlow<List<OrderListDao.PaymentDetails>>(emptyList())
    val paymentReceiptData: StateFlow<List<OrderListDao.PaymentDetails>> = _paymentReceiptData
    //paymentorderlist receipt
    private val _paymentOrderDetailsData = MutableStateFlow<List<OrderListDao.paymentOrderDetailsData>>(emptyList())
    val paymentOrderDetailsData: StateFlow<List<OrderListDao.paymentOrderDetailsData>> = _paymentOrderDetailsData
    private val _tableNo = MutableStateFlow<Int>(0)  // State flow for table number
    val tableNo: StateFlow<Int> = _tableNo
    private val _OrderId = MutableStateFlow<Int>(0)  // State flow for table number
    val OrderId: StateFlow<Int> = _OrderId
    private var sellerId: Int? = null  // Store sellerId when restaurant is selected
    // New state flow for Firebase payment receipts
    private val _firebasePaymentReceipt = MutableStateFlow<List<OrderListDao.PaymentDetails>>(emptyList())
    val firebasePaymentReceipt: StateFlow<List<OrderListDao.PaymentDetails>> = _firebasePaymentReceipt

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

    // Function to update the table number in the database
    fun updateTableNo(userId: Int, orderId: Int, tableNo: Int) {
        viewModelScope.launch {
            try {
                updateStatusMessage = "Table number updated successfully" // Success logic
                // Call the repository method to update the table number
                pierreAdminRepository.updateOrderTableNo(userId, orderId, tableNo)
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

    // New function to get the table number based on userId and orderId
    fun getTableNo(userId: Int, orderId: Int) {
        viewModelScope.launch {
            try {
                val tableNumber = pierreAdminRepository.getTableNoByUserAndOrder(userId, orderId)
                _tableNo.value = tableNumber  // Update the state with the fetched table number
            } catch (e: Exception) {
                _tableNo.value = 0  // Reset table number on error
                e.printStackTrace()
            }
        }
    }
    // New function to get the orderId
    fun getOrderId(userId: Int) {
        viewModelScope.launch {
            try {
                val OrderId = pierreAdminRepository.getLatestOrderId(userId)
                _OrderId.value = OrderId  // Update the state with the fetched table number
            } catch (e: Exception) {
                _OrderId.value = 0  // Reset table number on error
                e.printStackTrace()
            }
        }
    }
//    // New function to create a payment record
fun createPayment(orderId: Int, userId: Int, payType: String) {
    viewModelScope.launch {
        try {
            updateStatusMessage = "Processing payment..." // Indicate processing start
            // Step 1: Call the repository method to create a payment
            val paymentCreationResult = pierreAdminRepository.createPayment(orderId, userId, payType)

            // Check if the payment was created successfully
            if (paymentCreationResult) {
                // Step 2: Retrieve the latest payment details
                pierreAdminRepository.getLatestPaymentDetails(userId, orderId).observeForever { paymentData ->
                    if (!paymentData.isNullOrEmpty()) {
                        // Step 3: Upload the payment receipt to Firebase
                        Log.d("CreatePayment", "Uploading payment data to Firebase: $paymentData")
                        uploadPaymentToFirebase(userId, orderId, paymentData).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Load the payment receipt after uploading to Firebase
                                loadPaymentRecipt(orderId, userId)
                            } else {
                                Log.e("CreatePayment", "Failed to upload payment to Firebase")
                                updateStatusMessage = "Failed to upload payment receipt"
                            }
                        }
                    } else {
                        Log.e("CreatePayment", "Failed to retrieve payment details for userId: $userId, orderId: $orderId")
                    }
                }
            } else {
                Log.e("CreatePayment", "Payment creation failed for orderId: $orderId, userId: $userId")
                updateStatusMessage = "Payment creation failed"
            }
        } catch (e: Exception) {
            updateStatusMessage = "Failed to create payment" // Error message
            e.printStackTrace() // Log the exception
        }
    }
}

    // Function to upload payment details to Firebase
    private fun uploadPaymentToFirebase(userId: Int, orderId: Int, paymentDetails: List<OrderListDao.PaymentDetails>): Task<Void> {
        // Create a path for the payment receipt data in Firebase
        val paymentPath = "users/$userId/orders/$orderId/paymentReceipts"
        Log.d("FirebaseUpload", "Payment details: $paymentDetails")

        // Specify the correct path for the payment data
        val paymentRef = databaseReference.child(paymentPath)

        // Uploading data to Firebase
        return paymentRef.setValue(paymentDetails).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseUpload", "Payment receipt uploaded successfully at path: $paymentPath")
            } else {
                Log.e("FirebaseUpload", "Failed to upload payment receipt", task.exception)
            }
        }
    }

    // New function to load payment receipt from Firebase
    fun loadPaymentRecipt(orderId: Int, userId: Int) {

        viewModelScope.launch {
            // Create a path for the payment receipt data in Firebase
            val paymentPath = "users/$userId/orders/$orderId/paymentReceipts"
            val paymentRef = databaseReference.child(paymentPath)

            // Retrieve the data from Firebase
            paymentRef.get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    // Convert the dataSnapshot to a list of PaymentDetails
                    val receiptDetails = dataSnapshot.children.mapNotNull { snapshot ->
                        snapshot.getValue(OrderListDao.PaymentDetails::class.java)
                    }

                    // Update the LiveData with the retrieved payment receipt details
                    _paymentReceiptData.value = receiptDetails
                    updateStatusMessage = "Payment receipt retrieved successfully from Firebase!"
                    Log.d("FirebaseLoad", "Payment receipt retrieved successfully from path: $paymentPath")
                } else {
                    updateStatusMessage = "No payment receipt found in Firebase for userId: $userId, orderId: $orderId"
                    Log.e("FirebaseLoad", "No data found at path: $paymentPath")
                }
            }.addOnFailureListener { exception ->
                updateStatusMessage = "Failed to load payment receipt from Firebase: ${exception.message}"
                Log.e("FirebaseLoad", "Failed to load payment receipt", exception)
            }
        }
    }




    // New function to load order details by orderId and userId
    fun loadOrderListPaymentRecipt(orderId: Int, userId: Int) {
        viewModelScope.launch {
            // Fetch order details using the repository method
            pierreAdminRepository.getPaymentOrderDetails(userId , orderId).observeForever { receiptDetail ->
                _paymentOrderDetailsData.value = receiptDetail  // Update the order details data
            }
        }
    }

    // Function to upload pierreTest data to Firebase
    fun uploadPierreTestData(name: String) {
        val pierreTest = PierreTest(name)

        // Create a path for the pierreTest data in Firebase
        val testPath = "testRecords/${System.currentTimeMillis()}" // Use timestamp as a unique key
        Log.d("Preparing test data firebase", "PierreTest data: $pierreTest")
        // Upload the data to Firebase
        databaseReference.setValue(pierreTest)
        Log.d("FirebaseUpload", "Success setValue")
//            .addOnSuccessListener {
//                updateStatusMessage = "PierreTest data uploaded successfully!"
//                Log.d("FirebaseUpload", "PierreTest data: $pierreTest")
//            }
//            .addOnFailureListener { exception ->
//                updateStatusMessage = "Failed to upload PierreTest data: ${exception.message}"
//                Log.e("FirebaseUpload", "Error uploading PierreTest data", exception)
//            }
    }


}
