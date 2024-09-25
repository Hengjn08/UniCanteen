package com.example.unicanteen.LimSiangShin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.database.User
import com.example.unicanteen.database.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
): ViewModel(){
    val userName: String? = null
    val email:String? = null
    val password: String? = null
    private var currentUser: Int? = null

    fun Login(userName: String, password: String){
        viewModelScope.launch {
            val userLogin = userRepository.getUserForLogin(userName, password)
            if(userLogin?.userId !=null){
                currentUser = userLogin.userId
            }else{

            }
        }
    }

    fun Registration(userName: String, email: String, password: String){
        viewModelScope.launch {
            val user = User(0,userName,password,"",email)
            val CreateUser = userRepository.insertUser(user)
        }
    }
}