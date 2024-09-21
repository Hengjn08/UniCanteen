package com.example.unicanteen

import androidx.annotation.DrawableRes

data class Seller(
    val name: String,
    val description: String,
    @DrawableRes val imageRes: Int
)
