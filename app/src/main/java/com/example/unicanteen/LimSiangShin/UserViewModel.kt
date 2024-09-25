package com.example.unicanteen.LimSiangShin

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.database.User
import com.example.unicanteen.database.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val sellerRepository: SellerRepository
): ViewModel(){

    private var _loginResult = MutableStateFlow<Boolean>(false)  // For login success/failure
    val loginResult: StateFlow<Boolean> = _loginResult  // Expose login result to UI

    private var _currentUserId = MutableStateFlow<Int?>(null)  // Store the current logged-in user ID
    val currentUserId: StateFlow<Int?> = _currentUserId  // Expose the userId to UI for navigation

    private var _isSellerId = MutableStateFlow<Int?>(null)  // Store the current logged-in user ID
    val isSellerId: StateFlow<Int?> = _isSellerId  // Expose the userId to UI for navigation

    private var _isSeller = MutableStateFlow<Boolean>(false)  // For login success/failure
    val isSeller: StateFlow<Boolean> = _isSeller  // Expose login result to UI

    fun login(userName: String, password: String){
        viewModelScope.launch {
            val userLogin = userRepository.getUserForLogin(userName, password)

            if (userLogin != null && userLogin.userId != null) {
                // Check if the user is a seller using a non-null userId
                val isSellerId = userRepository.checkUserIsSeller(userLogin.userId)
                if (isSellerId != null) {
                    // User is a seller
                    _loginResult.value = true  // Mark login success
                    _isSeller.value = true     // Mark user as a seller
                } else {
                    // User is not a seller
                    _loginResult.value = true  // Mark login success
                    _isSeller.value = false    // Mark user as not a seller
                }
            } else {
                // Login failed
                _loginResult.value = false
                Log.d("UserViewModel", "Login failed for username: $userName")
            }
        }
    }

    fun checkIsSeller(userId: Int){
        viewModelScope.launch {
            val seller = sellerRepository.getSellersByUserId(userId)
            Log.d("CheckSeller", "Seller result: $seller for userId: $userId")
            if(seller != null){
                _isSeller.value = true
            }else{
                _isSeller.value = false
            }
        }
    }

    fun Registration(userName: String, email: String, password: String){
        viewModelScope.launch {
            val user = User(0,userName,password,"",email)
            userRepository.insertUser(user)
        }
    }
}