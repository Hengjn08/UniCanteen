package com.example.unicanteen.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "orderList",
    foreignKeys = [
        ForeignKey(entity = Seller::class, parentColumns = ["sellerId"], childColumns = ["sellerId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = FoodList::class, parentColumns = ["foodId"], childColumns = ["foodId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Order::class, parentColumns = ["orderId"], childColumns = ["orderId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [
        Index(value = ["sellerId"]),
        Index(value = ["foodId"]),
        Index(value = ["userId"]),
        Index(value = ["orderId"])
    ]
)
data class OrderList(
    @PrimaryKey(autoGenerate = true) val orderListId: Int = 0, // Primary key
    @ColumnInfo(name = "sellerId") val sellerId: Int, // Foreign key to Seller entity
    @ColumnInfo(name = "foodId") val foodId: Int, // Foreign key to FoodList entity
    @ColumnInfo(name = "userId") val userId: Int, // Foreign key to User entity
    @ColumnInfo(name = "orderId") val orderId: Int, // Foreign key to Order entity

    @ColumnInfo(name = "qty") val qty: Int, // Quantity of the food item ordered
    @ColumnInfo(name = "totalPrice") val totalPrice: Double, // Total price for this order item

    @ColumnInfo(name = "status") val status: String, // E.g., "Waiting", "Preparing", "Complete"
    @ColumnInfo(name = "createDate") val createDate: String, // Date the order was created (as a string)
    @ColumnInfo(name = "remark") val remark: String? = null // Optional remarks for the order") val status: String,
)
{
    // No-argument constructor (can be an empty constructor)
    constructor() : this(0, 0, 0, 0, 0, 0, 0.0, "", "", null)
}