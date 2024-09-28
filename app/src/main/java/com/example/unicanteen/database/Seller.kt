package com.example.unicanteen.database
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date


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
    @PrimaryKey(autoGenerate = true) val sellerId: Int = 0,
    @ColumnInfo(name = "userId") val userId: Int, // Foreign key to User entity
    @ColumnInfo(name = "shopType") val shopType: String,
    @ColumnInfo(name = "shopName") val shopName: String,
    @ColumnInfo(name = "createdDate") val createdDate: String? = null, // Changed to String for TEXT type
    @ColumnInfo(name = "shopStatus") val shopStatus: String, // E.g., "Open", "Closed", "Suspended"
    @ColumnInfo(name = "shopRating") var shopRating: Double = 0.0, // Default rating is 0
    @ColumnInfo(name = "totalOrders") val totalOrders: Int = 0, // Total number of orders
    @ColumnInfo(name = "updatedDate") val updatedDate: String? = null, // Changed to String for TEXT type
    @ColumnInfo(name = "descr") val description: String? = null, // Description of the seller
    @ColumnInfo(name = "shopImage") val shopImage: String? = null, // URL for the seller's image
    @ColumnInfo(name = "ratingNumber") var ratingNumber: Int = 0 // Number of ratings
)