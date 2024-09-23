package com.example.unicanteen.database

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date // Change this to java.util.Date

@Entity(
    tableName = "foodList",
    foreignKeys = [ForeignKey(
        entity = Seller::class,
        parentColumns = ["sellerId"],
        childColumns = ["sellerId"],
        onDelete = ForeignKey.CASCADE // Deletes food items if the seller is deleted
    )],
    indices = [Index(value = ["sellerId"])]
)
data class FoodList(
    @PrimaryKey(autoGenerate = true) val foodId: Int = 0, // Primary key
    @ColumnInfo(name = "sellerId") val sellerId: Int, // Foreign key to Seller entity

    @ColumnInfo(name = "foodName") val foodName: String,
    @ColumnInfo(name = "type") val type: String, // E.g., "Vegetarian", "Non-Vegetarian", "Vegan", etc.
    @ColumnInfo(name = "price") val price: Double,

    @ColumnInfo(name = "createDate") val createDate: String, // Store as ISO 8601 formatted string
    @ColumnInfo(name = "modifyDate") val modifyDate: String? = null, // Nullable field for the last modification date
    @ColumnInfo(name = "status") val status: String = "Available", // Default status

    // Additional recommended fields
    @ColumnInfo(name = "description") val description: String? = null, // Description of the food item
    @ColumnInfo(name = "imageUrl") val imageUrl: String, // URL for the food item's image
    @ColumnInfo(name = "rating") val rating: Double = 0.0, // Average rating for the food item
    @ColumnInfo(name = "calories") val calories: Int? = null // Optional field for calorie count
)
