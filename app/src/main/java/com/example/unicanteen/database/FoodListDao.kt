package com.example.unicanteen.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
@Dao
interface FoodListDao {
    // Insert a new food item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItem(foodItem: FoodList): Long

    // Update an existing food item
    @Update
    suspend fun updateFoodItem(foodItem: FoodList)

    // Delete a food item
    @Delete
    suspend fun deleteFoodItem(foodItem: FoodList)

    // Fetch a food item by foodId
    @Query("SELECT * FROM foodList WHERE foodId = :foodId")
    fun getFoodItemById(foodId: Long): FoodList?

    // Fetch all food items for a specific seller by sellerId
    @Query("SELECT * FROM foodList WHERE sellerId = :sellerId")
    fun getFoodItemsBySellerId(sellerId: Long): List<FoodList>

    // Fetch all available food items (status = 'Available')
    @Query("SELECT * FROM foodList WHERE status = 'Available'")
    fun getAvailableFoodItems(): List<FoodList>

    // Fetch food items by type (e.g., "Vegetarian", "Vegan")
    @Query("SELECT * FROM foodList WHERE type = :type")
    fun getFoodItemsByType(type: String): List<FoodList>

    // Fetch food items by price range
    @Query("SELECT * FROM foodList WHERE price BETWEEN :minPrice AND :maxPrice")
    fun getFoodItemsByPriceRange(minPrice: Double, maxPrice: Double): List<FoodList>

    // Fetch food items with a rating above a certain threshold
    @Query("SELECT * FROM foodList WHERE rating >= :rating")
    fun getFoodItemsWithHighRating(rating: Double): List<FoodList>

    // Fetch all food items
    @Query("SELECT * FROM foodList")
    fun getAllFoodItems(): List<FoodList>
}