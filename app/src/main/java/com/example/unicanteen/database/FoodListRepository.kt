package com.example.unicanteen.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

interface FoodListRepository {

    suspend fun insertFoodItem(foodItem: FoodList): Long
    suspend fun updateFoodItem(foodItem: FoodList)
    suspend fun deleteFoodItem(foodItem: FoodList)
    fun getFoodItemById(foodId: Long): FoodList?
    fun getFoodItemsBySellerId(sellerId: Long): List<FoodList>
    fun getAvailableFoodItems(): List<FoodList>
    fun getFoodItemsByType(type: String): List<FoodList>
    fun getFoodItemsByPriceRange(minPrice: Double, maxPrice: Double): List<FoodList>
    fun getFoodItemsWithHighRating(rating: Double): List<FoodList>
    fun getAllFoodItems(): List<FoodList>
}