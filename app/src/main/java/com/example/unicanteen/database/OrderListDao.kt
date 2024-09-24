package com.example.unicanteen.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.util.Date

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
        val totalQuantity: Int,
        val percentage: Double  // New field for percentage
    )


}
