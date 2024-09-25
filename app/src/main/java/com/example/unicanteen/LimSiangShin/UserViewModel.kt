package com.example.unicanteen.LimSiangShin

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private var _registerResult = MutableStateFlow<Boolean>(false)  // For login success/failure
    val registerResult: StateFlow<Boolean> = _registerResult  // Expose login result to UI

    private var _currentUserId = MutableStateFlow<Int?>(1)  // Store the current logged-in user ID
    val currentUserId: StateFlow<Int?> = _currentUserId  // Expose the userId to UI for navigation

    private var _isSellerId = MutableStateFlow<Int?>(null)  // Store the current logged-in user ID
    val isSellerId: StateFlow<Int?> = _isSellerId  // Expose the userId to UI for navigation

    private var _isSeller = MutableStateFlow<Boolean>(false)  // For login success/failure
    val isSeller: StateFlow<Boolean> = _isSeller  // Expose login result to UI


    var userNameError by mutableStateOf("")
    var emailError by mutableStateOf("")
//    var passwordValue by mutableStateOf("")
    var passwordError by mutableStateOf("")
    var phoneNumberError by mutableStateOf("")

//
//    fun setEmail(value: String){
//        emailValue = value
//    }
//
//    fun setPassword(value: String){
//        passwordValue = value
//    }

    fun login(userName: String, password: String){
        viewModelScope.launch {
            val userLogin = userRepository.getUserForLogin(userName, password)

            if (userLogin != null && userLogin.userId != null) {
                _currentUserId.value = userLogin.userId
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

    fun register(userName: String, email: String, password: String,phoneNumber: String,onResult: (Boolean, String?) -> Unit) {
        // Launches a coroutine in the ViewModel's scope
        viewModelScope.launch {
            // Asynchronous operation: Check if the email is already registered
            val existingUser = userRepository.getUserByEmail(email)
            if (existingUser != null) {
                onResult(false, "Email already registered.")
                return@launch
            }

            // Asynchronous operation: Insert the new user into the database
            val newUser = User(0, userName, password, "", email,phoneNumber)
            try {
                userRepository.insertUser(newUser)
                onResult(true, null)  // Registration successful
            } catch (e: Exception) {
                onResult(false, "Registration failed: ${e.message}")
            }
        }
    }


    //Validation for each type of text field
    private fun validateUserName(userName: String): Boolean {
        val email = userName.trim()
        var isValid = true
        var errorMessage = ""
        if (email.isBlank() || email.isEmpty()) {
            errorMessage = "Please fill userName field"
            isValid = false
        }
        userNameError = errorMessage
        return isValid
    }

    private fun validateEmail(email: String): Boolean {
        val email = email.trim()
        var isValid = true
        var errorMessage = ""
        if (email.isBlank() || email.isEmpty()) {
            errorMessage = "Please fill email field"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Wrong email Format"
            isValid = false
        }
        emailError = errorMessage
        return isValid
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        val phoneNumber = phoneNumber.trim()
        var isValid = true
        var errorMessage = ""

        if (phoneNumber.isBlank() || phoneNumber.isEmpty()) {
            errorMessage = "Please fill Phone Number field"
            isValid = false
        } else if (!phoneNumber.matches(Regex("^\\d{10,11}$"))) {
            errorMessage = "Phone Number format is wrong"
            isValid = false
        }
        phoneNumberError = errorMessage
        return isValid
    }

    private fun validatePassword(password: String): Boolean {
        val password = password.trim()
        var isValid = true
        var errorMessage = ""

        if (password.isBlank() || password.isEmpty()) {
            errorMessage = "Please fill password field"
            isValid = false
        } else if (password.length < 6) {
            errorMessage = "Password must more than 6 character"
            isValid = false
        }
        passwordError = errorMessage
        return isValid
    }

    fun validateRegistrationForm(userName: String, email: String,phoneNumber:String, password: String):Boolean {
        var correct = false
        if (validateUserName(userName) && validateEmail(email) && validatePhoneNumber(phoneNumber) && validatePassword(password) ) {
            correct = true
        }
        return correct
    }
}