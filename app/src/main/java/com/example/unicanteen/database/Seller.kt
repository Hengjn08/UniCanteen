package com.example.unicanteen.database
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "sellers",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE // Deletes seller when the user is deleted
    )],
    indices = [Index(value = ["userId"])]
)
data class Seller(
    @PrimaryKey(autoGenerate = true) val sellerId: Long = 0,
    @ColumnInfo(name = "userId") val userId: Long, // Foreign key to UserEntity

    @ColumnInfo(name = "shopType") val shopType: String,
    @ColumnInfo(name = "shopName") val shopName: String,
    @ColumnInfo(name = "createdDate") val createdDate: Date,
    @ColumnInfo(name = "shopStatus") val shopStatus: String, // E.g., "Open", "Closed", "Suspended"
    @ColumnInfo(name = "shopRating") val shopRating: Double = 0.0, // Rating of the shop
    @ColumnInfo(name = "totalOrders") val totalOrders: Int = 0, // Track the total number of orders
    @ColumnInfo(name = "updatedDate") val updatedDate: Date? = null // For tracking the last time the shop details were updated
)