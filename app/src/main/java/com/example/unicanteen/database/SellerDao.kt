package com.example.unicanteen.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SellerDao {
    // Insert a new seller
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeller(seller: Seller): Long

    // Update an existing seller
    @Update
    suspend fun updateSeller(seller: Seller)

    // Delete a seller
    @Delete
    suspend fun deleteSeller(seller: Seller)

    // Fetch a seller by sellerId
    @Query("SELECT * FROM sellers WHERE sellerId = :sellerId")
    fun getSellerById(sellerId: Int): Seller

    // Fetch all sellers associated with a specific userId
    @Query("SELECT * FROM sellers WHERE userId = :userId")
    fun getSellersByUserId(userId: Int): List<Seller>

    // Fetch all sellers
    @Query("SELECT * FROM sellers")
    fun getAllSellers(): List<Seller>

    // Delete sellers based on userId (if necessary)
    @Query("DELETE FROM sellers WHERE userId = :userId")
    fun deleteSellersByUserId(userId: Int)

    // Fetch sellers by status (e.g., "Open", "Closed")
    @Query("SELECT * FROM sellers WHERE shopStatus = :status")
     fun getSellersByStatus(status: String): List<Seller>

    // Fetch sellers with rating higher than a given threshold
    @Query("SELECT * FROM sellers WHERE shopRating >= :rating")
     fun getSellersWithHighRating(rating: Double): List<Seller>
}