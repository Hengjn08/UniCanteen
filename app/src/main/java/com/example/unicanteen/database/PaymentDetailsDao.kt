package com.example.unicanteen.database

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
    suspend fun getPaymentDetailsById(paymentDetailsId: Int): PaymentDetails?

    // Fetch all payment details for a specific order by orderId
    @Query("SELECT * FROM paymentDetails WHERE orderId = :orderId")
    fun getPaymentDetailsByOrderId(orderId: Int): List<PaymentDetails>

    // Fetch all payment details for a specific seller by sellerId
    @Query("SELECT * FROM paymentDetails WHERE sellerId = :sellerId")
    fun getPaymentDetailsBySellerId(sellerId: Int): List<PaymentDetails>

    // Fetch all payment details
    @Query("SELECT * FROM paymentDetails")
    fun getAllPaymentDetails(): List<PaymentDetails>
}