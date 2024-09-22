package com.example.unicanteen.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    tableName = "orderList",
    foreignKeys = [
        ForeignKey(entity = Seller::class, parentColumns = ["sellerId"], childColumns = ["sellerId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = FoodList::class, parentColumns = ["foodId"], childColumns = ["foodId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Order::class, parentColumns = ["orderId"], childColumns = ["orderId"], onDelete = ForeignKey.CASCADE) // Link to Order entity

    ],
    indices = [Index(value = ["sellerId"]), Index(value = ["foodId"]), Index(value = ["userId"]), Index(value = ["orderId"])]
)
data class OrderList(
    @PrimaryKey(autoGenerate = true) val orderListId: Long = 0, // Primary key
    @ColumnInfo(name = "sellerId") val sellerId: Long, // Foreign key to Seller entity
    @ColumnInfo(name = "foodId") val foodId: Long, // Foreign key to FoodList entity
    @ColumnInfo(name = "userId") val userId: Long, // Foreign key to User entity
    @ColumnInfo(name = "orderId") val orderId: Long, // Foreign key to Order entity

    @ColumnInfo(name = "qty") val qty: Int, // Quantity of the food item ordered
    @ColumnInfo(name = "totalPrice") val totalPrice: Double, // Total price for this order item

    @ColumnInfo(name = "status") val status: String, // E.g., "Waiting", "Preparing", "Complete"
    @ColumnInfo(name = "createDate") val createDate: Date // Date the order was created
)