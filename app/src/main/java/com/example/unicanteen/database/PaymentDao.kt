package com.example.assgntmobile0921.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PaymentDao {

    // Insert a new payment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: Payment): Long

    // Update an existing payment
    @Update
    suspend fun updatePayment(payment: Payment)

    // Delete a payment
    @Delete
    suspend fun deletePayment(payment: Payment)

    // Fetch a payment by paymentId
    @Query("SELECT * FROM payments WHERE paymentId = :paymentId")
    suspend fun getPaymentById(paymentId: Long): Payment?

    // Fetch all payments for a specific user by userId
    @Query("SELECT * FROM payments WHERE userId = :userId")
    suspend fun getPaymentsByUserId(userId: Long): List<Payment>

    // Fetch all payments for a specific order by orderId
    @Query("SELECT * FROM payments WHERE orderId = :orderId")
    suspend fun getPaymentsByOrderId(orderId: Long): List<Payment>

    // Fetch all payments
    @Query("SELECT * FROM payments")
    suspend fun getAllPayments(): List<Payment>
}