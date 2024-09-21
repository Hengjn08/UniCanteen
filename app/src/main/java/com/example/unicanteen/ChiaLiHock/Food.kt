package com.example.unicanteen

import androidx.annotation.DrawableRes

data class Food(
    val name: String,
    val description: String,
    @DrawableRes val imageRes: Int,
    val price: Double,
    val addOns: List<AddOn> = emptyList()
)

data class AddOn(
    val description: String,
    val price: Double
)


