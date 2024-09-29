package com.example.unicanteen.HengJunEn

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.database.AddOn
import com.example.unicanteen.database.AddOnRepository
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AddFoodViewModel (
    private val foodListRepository: FoodListRepository,
) : ViewModel(){
    // A function to insert the food item into the database
    suspend fun addFoodItem(
        food: FoodList
    ):Long {
            return foodListRepository.insertFood(food)
    }

    // LiveData for observing loading state
    private val _isUploading = MutableLiveData<Boolean>()
    val isUploading: LiveData<Boolean> = _isUploading

    // LiveData for observing upload success or error
    private val _uploadError = MutableLiveData<String?>()
    val uploadError: LiveData<String?> = _uploadError

    // Function to upload the image and save the food
    fun uploadImageAndSaveFood(
        imageUri: Uri?,
        food: FoodList,
        onSuccess: (String) -> Unit,
    ) {
        if (imageUri != null) {
            viewModelScope.launch {
                _isUploading.value = true
                try {
                    val imagePath = uploadImageToFirebase(imageUri)  // Now returning the path
                    if (imagePath != null) {
                        food.imageUrl = imagePath  // Store the path, not the URL
                        onSuccess(imagePath)       // Pass the path back to onSuccess
                    } else {
                        _uploadError.value = "Image upload failed"
                    }
                } catch (e: Exception) {
                    _uploadError.value = e.localizedMessage
                } finally {
                    _isUploading.value = false
                }
            }
        } else {
            _uploadError.value = "Image URI is null"
        }
    }


    // Firebase upload function
    private suspend fun uploadImageToFirebase(imageUri: Uri): String? {
        val storageRef = Firebase.storage.reference
        val fileName = "Food Images/${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child(fileName)

        return try {
            imageRef.putFile(imageUri).await()
            // Return the file path (not the full URL)
            fileName
        } catch (e: StorageException) {
            Log.e("FirebaseStorage", "Error uploading image", e)
            e.printStackTrace()
            null
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Unknown error occurred", e)
            e.printStackTrace()
            null
        }
    }
}