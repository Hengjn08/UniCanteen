package com.example.unicanteen.ChiaLiHock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.example.unicanteen.database.OrderList
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch

class CartViewModel(
    private val orderRepository: OrderRepository,
    private val orderListRepository: OrderListRepository
) : ViewModel() {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance("https://unicanteen12-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .getReference() // Firebase Database reference
    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> get() = _cartItems
    private val _cartItemCount = MutableLiveData<Int>()
    val cartItemCount: LiveData<Int> get() = _cartItemCount
    fun getCartItems(userId: Int) {
        viewModelScope.launch {
            val items = orderRepository.getPendingOrderItems(userId)
            _cartItems.value = items
        }
    }

    fun updateOrderItem(orderListId: Int, newQuantity: Int, unitPrice: Double, userId: Int) {
        val newTotalPrice = newQuantity * unitPrice  // Calculate new total price

        viewModelScope.launch {
            orderListRepository.updateOrderListItem(orderListId, newQuantity, newTotalPrice)
            val orderListItem = orderListRepository.getOrderListByOrderListId(orderListId)
            if (orderListItem != null) {
                updateOrderListInFirebase(orderListItem)
            }
            getCartItems(userId)
        }

    }

    fun updateOrderPrice(orderId: Int, Price: Double) {
        viewModelScope.launch {
            orderRepository.updateOrderPrice(orderId, Price)
        }
    }

    fun deleteOrderItem(orderListId: Int, userId: Int) {
        viewModelScope.launch {
            val orderListItem = orderListRepository.getOrderListByOrderListId(orderListId)
            if (orderListItem != null) {
                deleteOrderItemInFirebase(userId, orderListItem.orderId, orderListId)
            }
            orderListRepository.deleteOrderListById(orderListId)
            getCartItems(userId)
        }

    }

    fun deleteOrderByUserId(userId: Int) {
        viewModelScope.launch {
            val order = orderRepository.getOrdersByUserIdAndStatus(userId, "Pending")
            deleteOrderInFirebase(userId, order[0].orderId)

            orderRepository.deleteOrderByUserId(userId)
        }
    }

    fun fetchCartItemsCount(userId: Int) {
        viewModelScope.launch {
            try {
                val count = orderRepository.getCartItemsCount(userId)
                Log.d("CartViewModel", "Cart items count for userId $userId: $count")
                _cartItemCount.postValue(count)
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error fetching cart items count: ${e.message}")
                _cartItemCount.postValue(0) // Default value on error
            }
        }
    }

    private fun updateOrderListInFirebase(orderListItem: OrderList): Task<Void> {
        val orderListPath =
            "users/${orderListItem.userId}/orders/${orderListItem.orderId}/orderList/${orderListItem.orderListId}"
        Log.d("FirebaseUploadAndUpdate", "OrderList: $orderListItem")

        val orderListRef = databaseReference.child(orderListPath)

        // Create a map of the fields you want to update
        val updatedFields = mapOf(
            "qty" to orderListItem.qty,
            "totalPrice" to orderListItem.totalPrice,
        )

        // Update only the specified fields
        return orderListRef.updateChildren(updatedFields).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    "FirebaseUpdate",
                    "OrderList item updated successfully at path: $orderListPath"
                )
            } else {
                Log.e("FirebaseUpdate", "Failed to update OrderList item", task.exception)
            }
        }

    }

    private fun deleteOrderItemInFirebase(userId: Int, orderId: Int, orderListId: Int): Task<Void> {
        val orderListPath = "users/$userId/orders/$orderId/orderList/$orderListId"
        val orderListRef = databaseReference.child(orderListPath)
        return orderListRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(
                    "FirebaseDelete",
                    "OrderList item deleted successfully at path: $orderListPath"
                )
            } else {
                Log.e("FirebaseDelete", "Failed to delete OrderList item", task.exception)
            }

        }
    }

    private fun deleteOrderInFirebase(userId: Int, orderId: Int): Task<Void> {
        val orderPath = "users/$userId/orders/$orderId"
        val orderRef = databaseReference.child(orderPath)
        return orderRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseDelete", "Order deleted successfully at path: $orderPath")
            } else {
                Log.e("FirebaseDelete", "Failed to delete Order", task.exception)
            }
        }
    }
    fun getLatestImageUrl(filePath: String, onSuccess: (String) -> Unit) {
        val storageRef = Firebase.storage.reference.child(filePath)

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            onSuccess(uri.toString())  // Get the latest valid URL
        }.addOnFailureListener { exception ->
            Log.e("FirebaseStorage", "Error getting updated image URL", exception)
        }
    }
}


data class CartItem(
    @ColumnInfo(name = "orderId") val orderId: Int,
    @ColumnInfo(name = "orderListId") val orderListId: Int,
    @ColumnInfo(name = "foodName") val name: String,
    @ColumnInfo(name = "imageUrl") val imageRes: String,
    @ColumnInfo(name = "totalPrice") var price: Double,
    @ColumnInfo(name = "qty") var quantity: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "status") val status: String
    )