package com.example.unicanteen

data class CartItem(
    val name: String,
    val imageRes: Int, // Image resource ID
    val price: Double,
    var quantity: Int
)
