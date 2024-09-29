package com.example.unicanteen.HengJunEn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListDao
import com.example.unicanteen.database.FoodListRepository
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
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
//    fun fetchFoodDetails(foodId: Int) {
//        viewModelScope.launch {
//            val foodDetails = foodListRepository.getFoodDetailsWithAddOns(foodId)
//            _foodDetailsWithAddOns.value = foodDetails
//        }
//    }
    fun fetchFoodDetails(foodId: Int) {
        viewModelScope.launch {
            val foodDetails = foodListRepository.getFoodDetailsWithAddOns(foodId)

            // Fetch the latest image URL
            getLatestImageUrl(foodDetails.imageUrl) { latestImageUrl ->
                // Update the imageUrl property of foodDetails with the latest URL
                foodDetails.imageUrl = latestImageUrl

                // Update the state with the modified food details
                _foodDetailsWithAddOns.value = listOf(foodDetails)  // Wrap in a list if needed
            }
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

            foodListRepository.updateSellerFoodDetails(updatedFoodDetails)
        }
    }

    private fun getCurrentDateTimeString(): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        return LocalDateTime.now().format(formatter)
    }

    fun getLatestImageUrl(filePath: String, onSuccess: (String) -> Unit) {
        val storageRef = Firebase.storage.reference.child(filePath)

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            onSuccess(uri.toString())  // Get the latest valid URL
        }.addOnFailureListener { exception ->
            Log.e("FirebaseStorage", "Error getting updated image URL", exception)
        }
    }

}
