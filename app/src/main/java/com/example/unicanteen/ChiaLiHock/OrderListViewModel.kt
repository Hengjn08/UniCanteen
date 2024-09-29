package com.example.unicanteen.ChiaLiHock

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.Order
import com.example.unicanteen.database.OrderList
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class OrderListViewModel(
    private val orderListRepository: OrderListRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance("https://unicanteen12-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .getReference() // Firebase Database reference
    @SuppressLint("SuspiciousIndentation")
    fun addOrderListItem(
        sellerId: Int,
        foodId: Int,
        userId: Int,
        qty: Int,
        totalPrice: Double,
        remark: String?,
        createDate: String,
        price: Double
    ) {

        viewModelScope.launch {

            // Check if there is an existing "Pending" order for the user
            val existingOrderId = orderListRepository.getExistingOrderIdForUser(userId, "Pending")
            val orderId = existingOrderId ?: createNewOrder(userId, createDate).toInt() // Create a new order if none exists

            // Fetch all existing order items for the order
            val existingOrderItems = orderListRepository.getOrderListItem(orderId, sellerId, foodId)

            // Find the exact match of item based on totalPrice (unit price)
            val existingOrderItem = existingOrderItems?.find { (it.totalPrice / it.qty) == (totalPrice / qty) }

            if (existingOrderItem != null) {
                // Update the quantity if the item already exists
                val updatedQty = existingOrderItem.qty + qty
                val updatedTotalPrice = updatedQty * totalPrice / qty // Calculate the new total price
                val updatedOrderItem = existingOrderItem.copy(qty = updatedQty, totalPrice = updatedTotalPrice)
                orderListRepository.updateOrder(updatedOrderItem)
                updateOrderListInFirebase(orderId,updatedOrderItem)

            } else {
                // Create a new order list item if no match is found
                val orderListItem = OrderList(
                    sellerId = sellerId,
                    foodId = foodId,
                    userId = userId,
                    orderId = orderId,
                    qty = qty,
                    totalPrice = totalPrice,
                    status = "Pending", // Assuming "Pending" is the initial status
                    createDate = createDate,
                    remark = remark
                )
              val orderListId= orderListRepository.insertOrder(orderListItem)
                addOrderListToFirebase(orderId,orderListItem,userId,orderListId)

            }
        }
    }


    private suspend fun createNewOrder(userId: Int, createDate: String): Long {
        // Logic to insert a new order and return the new orderId
        val newOrder = Order(
            userId = userId,
            status = "Pending",
            createDate = createDate,
            orderType = "Pick up",
            totalPrice = 0.0
        )
        // Implement the method in your repository to insert the new order
        val newOrderId = orderRepository.insertOrder(newOrder)
        addNewOrderToFirebase(newOrderId, newOrder,userId)
        return newOrderId
    }

    private fun addNewOrderToFirebase(orderId: Long, order: Order, userId: Int): Task<Void> {
        val orderPath = "users/$userId/orders/$orderId"
        Log.d("FirebaseUpload", "Order: $order")

        val newOrder = Order(
            orderId = orderId.toInt(),
            userId = order.userId,
            status = order.status,
            createDate = order.createDate,
            orderType = order.orderType,
            totalPrice = order.totalPrice
        )
        // Specify the correct path for the data
        val orderRef = databaseReference.child(orderPath)

        // Uploading data to Firebase
        return orderRef.setValue(newOrder).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseUpload", "New ORDER uploaded successfully at path: $orderPath")
            } else {
                Log.e("FirebaseUpload", "Failed to upload NEW ORDER", task.exception)
            }
        }
    }

    private fun addOrderListToFirebase(
        orderId: Int,
        orderListItem: OrderList,
        userId: Int,
        orderListId: Long
    ): Task<Void> {
        val orderListPath = "users/$userId/orders/$orderId/orderList/$orderListId"
        Log.d("FirebaseUpload", "OrderList: $orderListItem")
        val orderListItem = OrderList(
            orderListId= orderListId.toInt(),
            sellerId = orderListItem.sellerId,
            foodId = orderListItem.foodId,
            userId = orderListItem.userId,
            orderId = orderId,
            qty = orderListItem.qty,
            totalPrice = orderListItem.totalPrice,
            status = orderListItem.status, // Assuming "Pending" is the initial status
            createDate = orderListItem.createDate,
            remark = orderListItem.remark
        )
        // Specify the correct path for the  data
        val orderListRef = databaseReference.child(orderListPath)

        // Uploading data to Firebase
        return orderListRef.setValue(orderListItem).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseUpload", "OrderList uploaded successfully at path: $orderListPath")
            } else {
                Log.e("FirebaseUpload", "Failed to upload OrderList", task.exception)
            }
        }
    }

    private fun updateOrderListInFirebase(orderId: Int, orderListItem: OrderList): Task<Void> {
        val orderListPath = "users/${orderListItem.userId}/orders/$orderId/orderList/${orderListItem.orderListId}"
        Log.d("FirebaseUploadAndUpdate", "OrderList: $orderListItem")

        val orderListRef = databaseReference.child(orderListPath)

        // Create a map of the fields you want to update
        val updatedFields = mapOf(
            "qty" to orderListItem.qty,
            "totalPrice" to orderListItem.totalPrice,
            "status" to orderListItem.status,
            "remark" to orderListItem.remark

        )

        // Update only the specified fields
        return orderListRef.updateChildren(updatedFields).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseUpdate", "OrderList item updated successfully at path: $orderListPath")
            } else {
                Log.e("FirebaseUpdate", "Failed to update OrderList item", task.exception)
            }
        }

    }
}

