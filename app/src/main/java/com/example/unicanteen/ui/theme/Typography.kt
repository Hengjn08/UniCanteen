// Typography.kt
package com.example.unicanteen.ui.theme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
)
