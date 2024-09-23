package com.example.unicanteen.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

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
    @PrimaryKey(autoGenerate = true) val paymentDetailsId: Int = 0, // Primary key
    @ColumnInfo(name = "orderId") val orderId: Int, // Foreign key to Order entity
    @ColumnInfo(name = "orderListId") val orderListId: Int, // Foreign key to OrderList entity
    @ColumnInfo(name = "sellerId") val sellerId: Int, // Foreign key to Seller entity
    @ColumnInfo(name = "paymentId") val paymentId: Int, // Foreign key to Payment entity

    @ColumnInfo(name = "amount") val amount: Double, // Amount paid to the seller
    @ColumnInfo(name = "createDate") val createDate: Date, // Date the payment detail was created
    @ColumnInfo(name = "status") val status: String // E.g., "Pending", "Completed"
)