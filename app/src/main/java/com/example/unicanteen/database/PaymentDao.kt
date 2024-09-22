package com.example.unicanteen.database

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
    suspend fun getPaymentById(paymentId: Int): Payment?

    // Fetch all payments for a specific user by userId
    @Query("SELECT * FROM payments WHERE userId = :userId")
    fun getPaymentsByUserId(userId: Int): List<Payment>

    // Fetch all payments for a specific order by orderId
    @Query("SELECT * FROM payments WHERE orderId = :orderId")
    fun getPaymentsByOrderId(orderId: Int): List<Payment>

    // Fetch all payments
    @Query("SELECT * FROM payments")
    fun getAllPayments(): List<Payment>
}