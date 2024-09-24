package com.example.unicanteen.model

data class User(
    val id: Int,
    val userName: String,
    val email: String,
    val pw: String,
    val userType: String = "Normal",
    var available: Boolean = false,
    )
