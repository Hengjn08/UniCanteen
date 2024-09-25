package com.example.unicanteen.database

import androidx.lifecycle.LiveData
import com.example.unicanteen.database.OrderListDao.FoodTypeSalesData

interface SellerRepository {
    suspend fun insertSeller(seller: Seller): Long
    suspend fun updateSeller(seller: Seller)
    suspend fun deleteSeller(seller: Seller)
    suspend fun getSellerById(sellerId: Int): Seller
    suspend fun getSellersByUserId(userId: Int): List<Seller>
    suspend fun getAllSellers(): List<Seller>
    suspend fun deleteSellersByUserId(userId: Int)
    suspend fun getSellersByStatus(status: String): List<Seller>
    suspend fun getSellersWithHighRating(rating: Double): List<Seller>
    suspend fun searchSellersByName(query: String): List<Seller>
}