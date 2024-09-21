package com.example.assgntmobile0921.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    tableName = "paymentDetails",
    foreignKeys = [
        ForeignKey(entity = Order::class, parentColumns = ["orderId"], childColumns = ["orderId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = OrderList::class, parentColumns = ["orderListId"], childColumns = ["orderListId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Seller::class, parentColumns = ["sellerId"], childColumns = ["sellerId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Payment::class, parentColumns = ["paymentId"], childColumns = ["paymentId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["orderId"]), Index(value = ["orderListId"]), Index(value = ["sellerId"]), Index(value = ["paymentId"])]
)
data class PaymentDetails(
    @PrimaryKey(autoGenerate = true) val paymentDetailsId: Long = 0, // Primary key
    @ColumnInfo(name = "orderId") val orderId: Long, // Foreign key to Order entity
    @ColumnInfo(name = "orderListId") val orderListId: Long, // Foreign key to OrderList entity
    @ColumnInfo(name = "sellerId") val sellerId: Long, // Foreign key to Seller entity
    @ColumnInfo(name = "paymentId") val paymentId: Long, // Foreign key to Payment entity

    @ColumnInfo(name = "amount") val amount: Double, // Amount paid to the seller
    @ColumnInfo(name = "createDate") val createDate: Date, // Date the payment detail was created
    @ColumnInfo(name = "status") val status: String // E.g., "Pending", "Completed"
)