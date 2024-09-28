package com.example.unicanteen.HengJunEn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListDao
import com.example.unicanteen.database.FoodListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditFoodViewModel(
    private val foodListRepository: FoodListRepository,
) : ViewModel() {

    // State to hold food details with add-ons
    private val _foodDetailsWithAddOns = MutableStateFlow<List<FoodListDao.FoodDetailsWithAddOns>>(emptyList())
    val foodDetailsWithAddOns: StateFlow<List<FoodListDao.FoodDetailsWithAddOns>> = _foodDetailsWithAddOns

    // Fetch food details along with add-ons based on foodId
    fun fetchFoodDetails(foodId: Int) {
        viewModelScope.launch {
            val foodDetails = foodListRepository.getFoodDetailsWithAddOns(foodId)
            _foodDetailsWithAddOns.value = foodDetails
        }
    }

//    fun updateFoodDetails(
//        foodId: Int,
//        newFoodName: String,
//        newDescription: String,
//        newPrice: Double,
//        newType: String,
//        newImageUrl: String
//    ) {
//        viewModelScope.launch {
//            // Fetch the existing food item
//            val currentFoodItem = foodListRepository.getFoodById(foodId)
//            if (currentFoodItem != null) {
//                // Create a copy of the existing food item with updated values
//                val updatedFoodItem = currentFoodItem.copy(
//                    foodName = newFoodName,
//                    description = newDescription,
//                    price = newPrice,
//                    type = newType,
//                    imageUrl = newImageUrl
//                    // Note: other fields like createDate and sellerId remain unchanged
//                )
//                // Update the food item in the repository
//                foodListRepository.updateFood(updatedFoodItem)
//            }
//        }
//    }

    // Function to update the food item
    fun updateFoodDetails(
        foodId: Int,
        foodName: String,
        description: String,
        price: Double,
        type: String,
        imageUrl: String
    ) {
        viewModelScope.launch {
            val currentDate =
                getCurrentDateTimeString() // Function to get the current date as a string

            // Create an UpdatedFoodDetails object with the provided data
            val updatedFoodDetails = FoodListDao.UpdatedFoodDetails(
                foodId = foodId,
                foodName = foodName,
                description = description,
                price = price,
                type = type,
                imageUrl = imageUrl,
                modifyDate = currentDate
            )

            // Pass the object to the repository method
            foodListRepository.updateSellerFoodDetails(updatedFoodDetails)
        }
    }

    private fun getCurrentDateTimeString(): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        return LocalDateTime.now().format(formatter)
    }

}
