package com.example.unicanteen.database

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import java.util.Date
import java.util.Locale

@Dao
interface OrderListDao {

    // Insert a new order list item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderList(orderList: OrderList): Long

    // Update an existing order list item
    @Update
    suspend fun updateOrderList(orderList: OrderList)

    // Delete an order list item
    @Delete
    suspend fun deleteOrderList(orderList: OrderList)

    // Fetch an order list item by orderListId
    @Query("SELECT * FROM orderList WHERE orderListId = :orderListId")
    fun getOrderListById(orderListId: Int): OrderList? // Use Long if it matches your schema

    // Fetch all order list items for a specific order by orderId
    @Query("SELECT * FROM orderList WHERE orderId = :orderId")
    fun getOrderListByOrderId(orderId: Int): List<OrderList> // Use Long if it matches your schema

    // Fetch all order list items for a specific user by userId
    @Query("SELECT * FROM orderList WHERE userId = :userId")
    fun getOrderListByUserId(userId: Int): List<OrderList> // Use Long if it matches your schema

    // Fetch all order list items
    @Query("SELECT * FROM orderList")
    fun getAllOrderListItems(): List<OrderList>

    // Fetch total sales for each seller grouped by month
    @Query("""
        SELECT sellerId, strftime('%Y-%m', createDate) AS month, SUM(totalPrice) AS totalSales
        FROM orderList
        WHERE createDate BETWEEN :startDate AND :endDate
        GROUP BY sellerId, month
        ORDER BY month
    """)
    fun getMonthlySalesForSellers(startDate: String, endDate: String): List<SalesData>

    data class SalesData(
        val sellerId: Int,
        val month: String,
        val totalSales: Double
    )

    // Fetch total sales for each food type for a specific month
    @Query("""
        SELECT f.type AS foodType, SUM(o.totalPrice) AS totalSales
        FROM orderList o
        INNER JOIN foodList f ON o.foodId = f.foodId
        WHERE strftime('%Y-%m', o.createDate) = :month
        GROUP BY f.type
        ORDER BY totalSales DESC
    """)
   fun getMonthlySalesByFoodType(month: String): List<FoodTypeSales>

    data class FoodTypeSales(
        val foodType: String,
        val totalSales: Double
    )

    // Fetch daily sales for each seller grouped by food type
    @Query("""
        SELECT o.sellerId, f.type AS foodType, SUM(o.totalPrice) AS totalSales, DATE(o.createDate) AS saleDate
        FROM orderList o
        INNER JOIN foodList f ON o.foodId = f.foodId
        WHERE o.createDate BETWEEN :startDate AND :endDate
        GROUP BY o.sellerId, f.type, saleDate
        ORDER BY saleDate, o.sellerId
    """)
    fun getDailySalesBySellerAndFoodType(startDate: String, endDate: String): List<DailySalesData>

    data class DailySalesData(
        val sellerId: Int,
        val foodType: String,
        val totalSales: Double,
        val saleDate: Date // Format: YYYY-MM-DD
    )

    // Fetch monthly sales growth for each seller
    @Query("""
        SELECT sellerId, strftime('%Y-%m', createDate) AS month, SUM(totalPrice) AS totalSales
        FROM orderList
        WHERE createDate BETWEEN :startDate AND :endDate
        GROUP BY sellerId, month
        ORDER BY month, sellerId
    """)
    fun getMonthlySalesGrowthBySeller(startDate: String, endDate: String): List<MonthlySalesGrowthData>

    data class MonthlySalesGrowthData(
        val sellerId: Int,
        val month: String,
        val totalSales: Double
    )

    @Query("""
    WITH TotalSales AS (
        SELECT SUM(o.totalPrice) AS totalSales
        FROM orderList o
        WHERE strftime('%Y-%m', o.createDate) = :month
        AND o.sellerId = :sellerId
    )
    SELECT f.type AS foodType, 
           strftime('%Y-%m', o.createDate) AS month, 
           SUM(o.totalPrice) AS totalQuantity,
           (SUM(o.totalPrice) * 100.0 / (SELECT totalSales FROM TotalSales)) AS percentage
    FROM orderList o
    JOIN foodList f ON o.foodId = f.foodId
    WHERE strftime('%Y-%m', o.createDate) = :month
    AND o.sellerId = :sellerId  -- Filter by seller ID
    GROUP BY f.type, month
    ORDER BY month
""")
    fun getMonthlySalesByFoodType(month: String, sellerId: Int): LiveData<List<FoodTypeSalesData>>

    data class FoodTypeSalesData(
        val foodType: String,
        val month: String,
        val totalQuantity: Double,
        val percentage: Double  // New field for percentage
    )

    @Query("""
    WITH TotalSales AS (
        SELECT SUM(o.totalPrice) AS totalSales
        FROM orderList o
        JOIN foodList f ON o.foodId = f.foodId
        WHERE f.type = :foodType
        AND o.sellerId = :sellerId
        AND strftime('%Y-%m', o.createDate) = :month  -- Filter by month
    )
    SELECT f.foodName AS foodType,
           strftime('%Y-%m', o.createDate) AS month, 
           SUM(o.totalPrice) AS totalQuantity,
           (SUM(o.totalPrice) * 100.0 / (SELECT totalSales FROM TotalSales)) AS percentage
    FROM orderList o
    JOIN foodList f ON o.foodId = f.foodId
    WHERE f.type = :foodType
    AND o.sellerId = :sellerId  -- Filter by seller ID
    AND strftime('%Y-%m', o.createDate) = :month  -- Filter by month
    GROUP BY f.foodName, month
    ORDER BY month
""")
    fun getSalesByFoodType(foodType: String, sellerId: Int, month: String): LiveData<List<FoodSalesData>>

    data class FoodSalesData(
        val foodType: String,
        val month: String,
        val totalQuantity: Double,
        val percentage: Double  // New field for percentage
    )

    @Query("""
    SELECT fl.foodName AS foodName,
           fl.imageUrl AS foodImage,
           s.shopName AS sellerShopName,
           ol.status AS orderStatus,
           o.orderType AS orderType,
           ol.orderListId AS orderListId
           
    FROM orderList ol
    JOIN foodList fl ON ol.foodId = fl.foodId
    JOIN sellers s ON ol.sellerId = s.sellerId
    JOIN orders o ON ol.orderId = o.orderId
    WHERE ol.orderId = :orderId
    AND ol.userId = :userId
    AND ol.status != 'Cancelled'
""")
    fun getOrderDetailsByOrderIdAndUserId(orderId: Int, userId: Int): LiveData<List<OrderDetailsData>>

    data class OrderDetailsData(
        val foodName: String,
        val foodImage: String,
        val sellerShopName: String,
        val orderStatus: String,
        val orderType: String,
        val orderListId: Int
    )

    @Query("UPDATE orders SET tableNo = :tableNo WHERE userId = :userId AND orderId = :orderId")
    suspend fun updateOrderTableNo(userId: Int, orderId: Int, tableNo: Int)

    @Query("UPDATE orders SET orderType = :orderType WHERE orderId = :orderId AND userId = :userId")
    suspend fun updateOrderType(orderId: Int, userId: Int, orderType: String)

    @Query("SELECT tableNo FROM orders WHERE userId = :userId AND orderId = :orderId")
    suspend fun getTableNoByUserAndOrder(userId: Int, orderId: Int): Int
    @Query("SELECT totalPrice FROM orders WHERE orderId = :orderId LIMIT 1")
    suspend fun getOrderTotalAmount(orderId: Int): Double

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: Payment)

    @Transaction
    suspend fun createPaymentRecord(orderId: Int, userId: Int, payType: String) {
        // Step 1: Get the total price from the `orders` table
        val totalAmt = getOrderTotalAmount(orderId)

        // Step 2: Create the payment entity with required details
        val newPayment = Payment(
            orderId = orderId,
            userId = userId,
            totalAmt = totalAmt,
            createDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()), // Current date-time
            status = "Completed", // Set status as Completed
            payType = payType
        )

        // Step 3: Insert the new payment record
        insertPayment(newPayment)
    }

    @Query("""
        SELECT u.userName, p.orderId, p.totalAmt, p.createDate, p.payType, p.status
        FROM payments p
        JOIN user u ON p.userId = u.userId
        WHERE p.userId = :userId AND p.orderId = :orderId
        ORDER BY p.paymentId DESC
        LIMIT 1
    """)
    suspend fun getLatestPaymentDetails(userId: Int, orderId: Int): PaymentDetails?




}
