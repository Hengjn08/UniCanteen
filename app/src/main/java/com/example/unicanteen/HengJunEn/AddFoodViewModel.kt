package com.example.unicanteen.HengJunEn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.database.AddOn
import com.example.unicanteen.database.AddOnRepository
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import kotlinx.coroutines.launch

class AddFoodViewModel (
    private val foodListRepository: FoodListRepository,
) : ViewModel(){


    // A function to insert the food item into the database
    suspend fun addFoodItem(
        food: FoodList
    ):Long {
            return foodListRepository.insertFood(food)
    }
}