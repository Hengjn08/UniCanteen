package com.example.unicanteen.database

import androidx.lifecycle.LiveData
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
    suspend fun getFoodItemById(foodId: Int): FoodList?

    // Fetch all food items for a specific seller by sellerId
    @Query("SELECT * FROM foodList WHERE sellerId = :sellerId")
    suspend fun getFoodItemsBySellerId(sellerId: Int): List<FoodList>

    // Fetch food items by type (e.g., "Vegetarian", "Vegan")
    @Query("SELECT DISTINCT type FROM foodList WHERE sellerId = :sellerId")
    suspend fun getFoodTypeBySellerId(sellerId: Int): List<String>

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

    // search related food items by name based on the seller id
    @Query("SELECT * FROM foodList WHERE sellerId = :sellerId AND foodName LIKE '%' || :query || '%'")
    suspend fun searchFoodItemsByName(sellerId: Int, query: String): List<FoodList>

    // get shopName by sellerId
    @Query("SELECT shopName FROM sellers WHERE sellerId = :sellerId")
    suspend fun getShopNameBySellerId(sellerId: Int): String

    data class FoodDetailsWithAddOns(
        val foodName: String,
        val type: String,
        val foodDescription: String,
        val price: Double,
        var imageUrl: String,
        val addOnDescription: String?
    )

    //display food details with add ons
    @Query("""
        SELECT f.foodName, f.type, f.description AS foodDescription, f.price, f.imageUrl, 
               GROUP_CONCAT(COALESCE(a.description, ''), ', ') AS addOnDescription
        FROM foodList f
        LEFT JOIN addOn a ON f.foodId = a.foodId
        WHERE f.foodId = :foodId
        GROUP BY f.foodId
    """)
    suspend fun getFoodDetailsWithAddOns(foodId: Int): FoodDetailsWithAddOns

    data class UpdatedFoodDetails(
        val foodId: Int,
        val foodName: String,
        val description: String,
        val price: Double,
        val type: String,
        val imageUrl: String,
        val modifyDate: String
    )
    @Query("""
    UPDATE foodList
    SET
        foodName = :foodName,
        description = :description,
        price = :price,
        type = :type,
        imageUrl = :imageUrl,
        modifyDate = :modifyDate
    WHERE foodId = :foodId
""")
    suspend fun updateFoodDetails(
        foodId: Int,
        foodName: String,
        description: String,
        price: Double,
        type: String,
        imageUrl: String,
        modifyDate: String
    )

    // Usage in your DAO or Repository
    suspend fun updateSellerFoodDetails(updatedFoodDetails: UpdatedFoodDetails) {
        updateFoodDetails(
            foodId = updatedFoodDetails.foodId,
            foodName = updatedFoodDetails.foodName,
            description = updatedFoodDetails.description,
            price = updatedFoodDetails.price,
            type = updatedFoodDetails.type,
            imageUrl = updatedFoodDetails.imageUrl,
            modifyDate = updatedFoodDetails.modifyDate
        )
    }
//    @Query("""
//    UPDATE FoodList
//    SET
//        foodName = :foodName,
//        description = :description,
//        price = :price,
//        type = :type,
//        imageUrl = :imageUrl,
//        modifyDate = :modifyDate
//    WHERE foodId = :foodId
//""")
//    suspend fun updateSellerFoodDetails(
//        foodId: Int,
//        foodName: String,
//        description: String,
//        price: Double,
//        type: String,
//        imageUrl: String,
//        modifyDate: String
//    )

}