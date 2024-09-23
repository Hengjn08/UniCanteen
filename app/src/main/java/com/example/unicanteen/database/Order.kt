package com.example.unicanteen.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "orders",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE // Deletes order when the user is deleted
    )],
    indices = [Index(value = ["userId"])]
)
data class Order(
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0, // Primary key
    @ColumnInfo(name = "userId") val userId: Int, // Foreign key to User entity

    @ColumnInfo(name = "orderType") val orderType: String, // E.g., "Pick up" or "Delivery"
    @ColumnInfo(name = "tableNo") val tableNo: Int? = null, // Nullable field for table number if dining in

    @ColumnInfo(name = "totalPrice") val totalPrice: Double, // Total price for the entire order
    @ColumnInfo(name = "status") val status: String, // E.g., "Pending", "Completed"
    @ColumnInfo(name = "createDate") val createDate: String // Date the order was created (as a string)
)