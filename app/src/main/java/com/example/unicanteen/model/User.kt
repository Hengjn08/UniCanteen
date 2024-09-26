package com.example.unicanteen.model

data class User(
    val id: Int,
    val userName: String,
    val email: String,
    val pw: String,
    val isSeller: Boolean = false,
    var available: Boolean = false,
    )
