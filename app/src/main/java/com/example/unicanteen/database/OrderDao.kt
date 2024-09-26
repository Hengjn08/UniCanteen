package com.example.unicanteen.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.unicanteen.ChiaLiHock.CartItem
import com.example.unicanteen.ChiaLiHock.CartViewModel

@Dao
interface OrderDao {

    // Insert a new order
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    // Update an existing order
    @Update
    suspend fun updateOrder(order: Order)

    @Query("UPDATE orders SET totalPrice = :Price WHERE orderId = :orderId")
    suspend fun updateOrderPrice(orderId: Int, Price: Double)

    // Delete an order
    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("DELETE FROM orders WHERE orderId = :orderId")
    suspend fun deleteOrderById(orderId: Int)

    @Query("DELETE FROM orders WHERE userId = :userId AND status = 'Pending'")
    suspend fun deleteOrderByUserId(userId: Int)

    // Fetch an order by orderId
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    fun getOrderById(orderId: Int): Order?

    // Fetch all orders for a specific user by userId
    @Query("SELECT * FROM orders WHERE userId = :userId")
    fun getOrdersByUserId(userId: Int): List<Order>

    // Fetch all orders
    @Query("SELECT * FROM orders")
    fun getAllOrders(): List<Order>

    //Fetch order by user id and orderstauts
    @Query("SELECT * FROM orders WHERE userId = :userId AND status = :orderStatus")
    fun getOrdersByUserIdAndStatus(userId: Int, orderStatus: String): List<Order>



    @Query("""
        SELECT 
            o.orderId,
            ol.orderListId,
            f.foodName, 
            f.imageUrl, 
            ol.totalPrice, 
            ol.qty
        FROM 
            foodList AS f
        JOIN 
            orderList AS ol ON f.foodId = ol.foodId
        JOIN 
            orders AS o ON ol.orderId = o.orderId
        WHERE 
            o.userId = :userId AND o.status = 'Pending'
    """)
        suspend fun getPendingOrderItems(userId: Int): List<CartItem>

}