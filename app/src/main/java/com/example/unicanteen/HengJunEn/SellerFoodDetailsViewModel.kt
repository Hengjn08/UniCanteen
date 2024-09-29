package com.example.unicanteen.HengJunEn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SellerFoodDetailsViewModel(
    private val foodListRepository: FoodListRepository
) : ViewModel() {

    private val _foodDetails = MutableStateFlow<FoodList?>(null)
    val foodDetails: StateFlow<FoodList?> get() = _foodDetails

    // Function to load food details by foodId
    fun loadFoodDetails(foodId: Int) {
        viewModelScope.launch {
            val food = foodListRepository.getFoodById(foodId)
            _foodDetails.value = food
        }
    }

    // Function to delete the current food item
    fun deleteFood() {
        viewModelScope.launch {
            _foodDetails.value?.let { food ->
                foodListRepository.deleteFood(food)
                // Optionally clear the food details after deletion
                _foodDetails.value = null
            }
        }
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