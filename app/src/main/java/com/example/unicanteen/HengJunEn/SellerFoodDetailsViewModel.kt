package com.example.unicanteen.HengJunEn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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
    // Function to delete the current food item and image from Firebase
    fun deleteFoodAndImage(onComplete: () -> Unit) {
        viewModelScope.launch {
            _foodDetails.value?.let { food ->
                try {
                    // First, delete the food image
                    food.imageUrl?.let { imageUrl ->
                        deleteFoodImage(imageUrl)
                    }

                    // Then delete the food item
                    foodListRepository.deleteFood(food)

                    // Clear food details after deletion
                    _foodDetails.value = null

                    // Call the completion callback
                    withContext(Dispatchers.Main) {
                        onComplete()
                    }

                } catch (e: Exception) {
                    Log.e("ViewModel", "Error deleting food or image", e)
                }
            }
        }
    }
//    fun deleteFood() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _foodDetails.value?.let { food ->
//                foodListRepository.deleteFood(food)
//                // Optionally clear the food details after deletion
//                _foodDetails.value = null
//            }
//        }
//    }

    fun getLatestImageUrl(filePath: String, onSuccess: (String) -> Unit) {
        val storageRef = Firebase.storage.reference.child(filePath)

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            onSuccess(uri.toString())  // Get the latest valid URL
        }.addOnFailureListener { exception ->
            Log.e("FirebaseStorage", "Error getting updated image URL", exception)
        }
    }

    private suspend fun deleteFoodImage(imageUrl: String) {
        val storageRef = Firebase.storage.reference.child(imageUrl)
        try {
            storageRef.delete().await()
            Log.d("FirebaseStorage", "Image deleted successfully from Firebase Storage")
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Error deleting image from Firebase Storage", e)
        }
    }
}