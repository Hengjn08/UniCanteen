package com.example.unicanteen.database

import androidx.lifecycle.LiveData

interface AddOnRepository {

    // Function to insert an AddOn
    suspend fun insert(addOn: AddOn)

    // Function to retrieve all AddOns for a specific food item
    suspend fun getAddOnsForFood(foodId: Int): List<AddOn>

    // Function to delete an AddOn
    suspend fun delete(addOn: AddOn)

    // Optional: LiveData support for observing add-ons
    fun getAddOnsForFoodLive(foodId: Int): LiveData<List<AddOn>>
}
