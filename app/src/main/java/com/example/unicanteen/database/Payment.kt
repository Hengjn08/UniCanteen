package com.example.unicanteen.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date


@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(entity = Order::class, parentColumns = ["orderId"], childColumns = ["orderId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["orderId"]), Index(value = ["userId"])]
)
data class Payment(
    @PrimaryKey(autoGenerate = true) val paymentId: Int = 0, // Primary key
    @ColumnInfo(name = "orderId") val orderId: Int, // Foreign key to Order entity
    @ColumnInfo(name = "userId") val userId: Int, // Foreign key to User entity

    @ColumnInfo(name = "totalAmt") val totalAmt: Double, // Total amount for the payment
    @ColumnInfo(name = "createDate") val createDate: Date, // Date the payment was created
    @ColumnInfo(name = "status") val status: String // E.g., "Pending", "Completed"
)