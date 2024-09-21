package com.example.unicanteen.model

data class Food(
    val id: Int,
    val foodName: String,
    val foodDesc: String,
    val foodImage: String,
    val price: Double,
    var available: Boolean = false,
    val type: String,
)
