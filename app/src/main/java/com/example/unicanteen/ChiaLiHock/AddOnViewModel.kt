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

}
