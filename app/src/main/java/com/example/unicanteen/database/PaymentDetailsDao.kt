package com.example.assgntmobile0921.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PaymentDetailsDao {

    // Insert a new payment detail
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentDetails(paymentDetails: PaymentDetails): Long

    // Update an existing payment detail
    @Update
    suspend fun updatePaymentDetails(paymentDetails: PaymentDetails)

    // Delete a payment detail
    @Delete
    suspend fun deletePaymentDetails(paymentDetails: PaymentDetails)

    // Fetch a payment detail by paymentDetailsId
    @Query("SELECT * FROM paymentDetails WHERE paymentDetailsId = :paymentDetailsId")
    suspend fun getPaymentDetailsById(paymentDetailsId: Long): PaymentDetails?

    // Fetch all payment details for a specific order by orderId
    @Query("SELECT * FROM paymentDetails WHERE orderId = :orderId")
    suspend fun getPaymentDetailsByOrderId(orderId: Long): List<PaymentDetails>

    // Fetch all payment details for a specific seller by sellerId
    @Query("SELECT * FROM paymentDetails WHERE sellerId = :sellerId")
    suspend fun getPaymentDetailsBySellerId(sellerId: Long): List<PaymentDetails>

    // Fetch all payment details
    @Query("SELECT * FROM paymentDetails")
    suspend fun getAllPaymentDetails(): List<PaymentDetails>
}