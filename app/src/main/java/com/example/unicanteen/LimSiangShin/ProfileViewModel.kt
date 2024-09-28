package com.example.unicanteen.LimSiangShin

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.database.User
import com.example.unicanteen.database.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
): ViewModel(){

    // LiveData for the selected food type
    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    // LiveData for the selected food type
    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

    // LiveData for the selected food type
    private val _phoneNumber = MutableStateFlow<String?>(null)
    val phoneNumber: StateFlow<String?> = _phoneNumber

    // LiveData for the selected food type
    private val _password = MutableStateFlow<String?>(null)
    val password: StateFlow<String?> = _password


    fun getUserName(userId:Int) : String{
        var name = ""
        viewModelScope.launch {
            name = userRepository.getUserName(userId)
            _userName.value = name
        }
        return name
    }

    fun getEmail(userId:Int): String{
        var email = ""
        viewModelScope.launch {
            email = userRepository.getEmail(userId)
            _email.value = email
        }
        return email
    }

    fun getPhoneNumber(userId:Int): String{
        var phoneNumber = ""
        viewModelScope.launch {
            phoneNumber = userRepository.getPhoneNumber(userId)
            _phoneNumber.value = phoneNumber
        }
        return phoneNumber
    }

    fun getPassword(userId:Int): String{
        var password = ""
        viewModelScope.launch {
            password = userRepository.getPassword(userId)
            _password.value = password
        }
        return password
    }
}
