package com.example.unicanteen.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DividerDefaults.color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.unicanteen.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary =Color(0XFF123566),
    background = Color(0xFF121212),  // Dark background
    surface = Color(0xFF1E1E1E),     // Surface color for dark mode
    onPrimary = Color.Gray,         // Text color on primary
    onSecondary = Color.White,       // Text color on secondary
    onBackground = Color.White,      // Text color on background
    onSurface = Color.White          // Text color on surface
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Color(0XFFF6AD44),
    background = Color(0xFFFFFBFE),  // Light background
    surface = Color(0xFFFFFBFE),     // Surface color for light mode
    onPrimary = Color.White,         // Text color on primary
    onSecondary = Color.Black,       // Text color on secondary
    onBackground = Color.Black,      // Text color on background
    onSurface = Color.Black,          // Text color on surface

)

@Composable
fun UniCanteenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}