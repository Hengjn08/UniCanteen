package com.example.unicanteen.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface OrderDao {

    // Insert a new order
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    // Update an existing order
    @Update
    suspend fun updateOrder(order: Order)

    // Delete an order
    @Delete
    suspend fun deleteOrder(order: Order)

    // Fetch an order by orderId
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: Long): Order?

    // Fetch all orders for a specific user by userId
    @Query("SELECT * FROM orders WHERE userId = :userId")
    suspend fun getOrdersByUserId(userId: Long): List<Order>

    // Fetch all orders
    @Query("SELECT * FROM orders")
    suspend fun getAllOrders(): List<Order>
}