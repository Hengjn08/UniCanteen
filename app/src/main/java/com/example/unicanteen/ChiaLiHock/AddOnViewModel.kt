package com.example.unicanteen.ChiaLiHock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.AddOn
import com.example.unicanteen.database.AddOnRepository
import kotlinx.coroutines.launch

class AddOnViewModel(private val repository: AddOnRepository) : ViewModel() {
    private val _addOnList = MutableLiveData<List<AddOn>>()
    val addOnList: LiveData<List<AddOn>> = _addOnList

    fun fetchAddOns(foodId: Int) {
        viewModelScope.launch {
            try {
                val addOns = repository.getAddOnsForFood(foodId)
                _addOnList.postValue(addOns)
            } catch (e: Exception) {
                // Handle any errors, e.g., logging or setting an error state
            }
        }
    }

    fun insertAddOns(addOn: AddOn){
        viewModelScope.launch {
            repository.insert(addOn)
        }
    }

    // Delete add-ons (can be used when removing unwanted add-ons)
    private suspend fun deleteAddOns(addOns: List<AddOn>) {
        viewModelScope.launch {
            addOns.forEach { repository.delete(it) }
        }
    }

    // Update the add-ons list when editing a food item
    fun updateAddOnsForFood(newAddOns: List<AddOn>, foodId: Int) {
        viewModelScope.launch {
                // Get existing add-ons for the food item
                val existingAddOns = repository.getAddOnsForFood(foodId)

                // Identify which add-ons to delete (no longer part of the new add-ons list)
                val addOnsToDelete = existingAddOns.filter { existing ->
                    existing.description !in newAddOns.map { it.description }
                }

                // Delete removed add-ons
                deleteAddOns(addOnsToDelete)

                // Insert new add-ons that are not in the existing list
                newAddOns.forEach { newAddOn ->
                    if (newAddOn.description !in existingAddOns.map { it.description }) {
                        insertAddOns(newAddOn)
                    }
                }
            }

        }
}

