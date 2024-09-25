package com.example.unicanteen.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "addOn",
    foreignKeys = [ForeignKey(
        entity = FoodList::class,
        parentColumns = ["foodId"],
        childColumns = ["foodId"],
        onDelete = ForeignKey.CASCADE // Deletes add-ons if the associated food item is deleted
    )],
    indices = [Index(value = ["foodId"])]
)
data class AddOn(
    @PrimaryKey(autoGenerate = true) val addOnId: Int = 0, // Primary key
    @ColumnInfo(name = "foodId") val foodId: Int, // Foreign key to FoodList entity
    @ColumnInfo(name = "description") val description: String, // Description of the add-on
    @ColumnInfo(name = "price") val price: Double // Price of the add-on
)
