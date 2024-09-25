package com.example.unicanteen.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete


@Dao
interface AddOnDao {
    @Insert
    suspend fun insertAddOn(addOn: AddOn)

    @Query("SELECT * FROM addOn WHERE foodId = :foodId")
    suspend fun getAddOnsForFood(foodId: Int): List<AddOn>

    // New method for LiveData support
    @Query("SELECT * FROM addOn WHERE foodId = :foodId")
    fun getAddOnsForFoodLive(foodId: Int): LiveData<List<AddOn>>

    @Delete
    suspend fun deleteAddOn(addOn: AddOn)
}
