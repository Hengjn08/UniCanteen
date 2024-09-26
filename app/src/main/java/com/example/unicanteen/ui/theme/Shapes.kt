package com.example.unicanteen.ui.theme

// Shapes.kt
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), // Only top corners rounded

)
