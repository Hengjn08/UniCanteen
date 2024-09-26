package com.example.unicanteen.HengJunEn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import kotlinx.coroutines.launch

class AddFoodViewModel (private val foodListRepository: FoodListRepository
) : ViewModel(){
    // A function to insert the food item into the database
    fun insertFoodItem(food: FoodList) {
        viewModelScope.launch {
            foodListRepository.insertFood(food)
        }
    }
}