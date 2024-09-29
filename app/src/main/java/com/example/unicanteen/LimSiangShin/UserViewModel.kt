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
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.OrderListDao
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.database.User
import com.example.unicanteen.database.UserDao
import com.example.unicanteen.database.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
//    private val sellerRepository: SellerRepository
): ViewModel(){

    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance("https://unicanteen12-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .getReference() // Firebase Database reference

//    var userValue by mutableStateOf("")
//    var emailValue by mutableStateOf("")
//    var phoneNumberValue by mutableStateOf("")
//    var passwordValue by mutableStateOf("")
//
//    fun setUserName(value: String){
//        userValue = value
//    }
//
//    fun setEmail(value: String){
//        emailValue = value
//    }
//
//    fun setPhoneNumber(value: String){
//        phoneNumberValue = value
//    }
//
//    fun setPassword(value: String){
//        passwordValue = value
//    }

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

    private val _orderDetail = MutableStateFlow<List<UserDao.OrderDetails>>(emptyList())
    val orderDetail: StateFlow<List<UserDao.OrderDetails>> = _orderDetail

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password


    var userNameError by mutableStateOf("")
    var emailError by mutableStateOf("")
    var passwordError by mutableStateOf("")
    var phoneNumberError by mutableStateOf("")
    var confirmPasswordError by mutableStateOf("")



    fun login(userName: String, password: String){
        viewModelScope.launch {
            val userLogin = userRepository.getUserForLogin(userName, password)

            if (userLogin != null) {
                _currentUserId.value = userLogin.userId
//                _userName.value = userLogin.userName
//                emailValue = userLogin.email
//                phoneNumberValue = userLogin.phoneNumber.toString()
//                passwordValue = userLogin.password
                // Check if the user is a seller using a non-null userId
                val isSellerId = userRepository.checkUserIsSeller(userLogin.userId)
                if (isSellerId != null) {
                    // User is a seller
                    _isSellerId.value = isSellerId
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

    fun register(userName: String, email: String, password: String,phoneNumber: String):Boolean {
        var correct = true
        // Launches a coroutine in the ViewModel's scope
        viewModelScope.launch {
            // Asynchronous operation: Check if the email is already registered
            if(validateRegistrationForm(userName,email,phoneNumber,password)){
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    correct = false
                    return@launch
                }else {
                    // Asynchronous operation: Insert the new user into the database
                    val newUser = User(0, userName, password, "", email, phoneNumber)
                    userRepository.insertUser(newUser)
                    val newUser1 = userRepository.getUserForLogin(userName, password)
                    if (newUser1 != null) {
                        uploadNewUserToFirebase(newUser1.userId,newUser1).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Load the payment receipt after uploading to Firebase
                                Log.e("CreatePayment", "upload  to Firebase")
                            } else {
                                Log.e("CreatePayment", "Failed to upload  to Firebase")
                            }
                        }
                    }
                }
            }else{
                correct = false
            }
        }
        return correct
    }

    // Function to upload payment details to Firebase
    private fun uploadNewUserToFirebase(userId:Int, user: User?): Task<Void> {

        // Create a path for the payment receipt data in Firebase
        val userPath = "users/$userId/details"
        Log.d("FirebaseUpload", "user Id: $userId")

        // Specify the correct path for the payment data
        val userRef = databaseReference.child(userPath)

        // Uploading data to Firebase
        return userRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseUpload", "Payment receipt uploaded successfully at path: details")
            } else {
                Log.e("FirebaseUpload", "Failed to upload payment receipt", task.exception)
            }
        }
    }


    fun updateUserDetail (userId: Int,userName: String,password: String, email: String, phoneNumber: String) {
        viewModelScope.launch() {
                val user = User(userId, userName, password, userName.uppercase(), email, phoneNumber)
                userRepository.updateUser(user)
        }
    }

     fun updateCurrentUserDetail(userId: Int){
        viewModelScope.launch {
        val user = userRepository.getUserById(userId)
            if (user != null) {
            // Update UI state
            _userName.value = user.userName
            _email.value = user.email
            _password.value = user.password
            _phoneNumber.value = user.phoneNumber.toString()
            }
        }
    }



    //Validation for each type of text field
     fun validateUserName(userName: String): Boolean {
        val name = userName.trim()
        var isValid = true
        var errorMessage = ""
        if (name.isBlank() || name.isEmpty()) {
            errorMessage = "Please fill userName field"
            isValid = false
        }
        userNameError = errorMessage
        return isValid
    }

    fun validateEmail(email: String): Boolean {
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

     fun validatePhoneNumber(phoneNumber: String): Boolean {
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

     fun validatePassword(password: String): Boolean {
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

//    fun validateConfirmPassword(confirmPassword: String, password: String): Boolean {
//        val cPW = confirmPassword.trim()
//        var isValid = true
//        var errorMessage = ""
//
//        if (cPW != password) {
//            errorMessage = "Password must more than 6 character"
//            isValid = false
//        }
//        confirmPasswordError = errorMessage
//        return isValid
//    }


    fun validateRegistrationForm(userName: String, email: String,phoneNumber:String, password: String):Boolean {
        var correct = false
        if (validateUserName(userName) && validateEmail(email) && validatePhoneNumber(phoneNumber) && validatePassword(password) ) {
            correct = true
        }
        return correct
    }

//    fun checkEmailExist(email:String):Boolean{
//        var correct = false
//        viewModelScope.launch {
//            val checkUser = userRepository.checkUserEmail(email)
//            if(checkUser != null){
//                correct = true
//            }
//        }
//        return correct
//    }

    fun updateNewPassword(email:String, password: String){
        viewModelScope.launch {
            val checkUser = userRepository.checkUserEmail(email)
            if(checkUser != null){
                userRepository.updateUserPassword(password,checkUser)
            }
        }
    }

    fun getUserOrderHistory(userId: Int){
        viewModelScope.launch {
            val orderList = userRepository.getOrderDetailsByUserId(userId)
            _orderDetail.value = orderList
        }
    }
}