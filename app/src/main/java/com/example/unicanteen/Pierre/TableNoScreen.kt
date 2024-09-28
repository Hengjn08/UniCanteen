package com.example.unicanteen.Pierre

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.UniCanteenTopBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.database.OrderListDao
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import androidx.compose.ui.graphics.Color
import com.example.unicanteen.navigation.NavigationDestination
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import kotlin.random.Random
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.unicanteen.database.PierreAdminRepository
import com.example.unicanteen.ui.theme.AppViewModelProvider

object InputTableNoDestination : NavigationDestination {
    override val route = "input_table/{userId}/{orderId}"
    override val title = "input_table"
    fun routeWithArgs(userId: Int, orderId: Int): String {
        return "input_table/$userId/$orderId"
    }
}

@Composable
fun TableNoScreen(
    navController: NavController,
    currentDestination: NavDestination?,
    sellerAdminRepository: PierreAdminRepository,
    userId: Int,
    orderId: Int,
    modifier: Modifier = Modifier
) {
    // Initialize the ViewModel
    val viewModel: AdminViewModel = viewModel(
        factory = AppViewModelProvider.Factory(pierreAdminRepository = sellerAdminRepository)
    )

    var tableNoInput by remember { mutableStateOf("") }  // TextField input state
    var updateMessage by remember { mutableStateOf<String?>(null) }  // State for showing success/fail message
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),  // You can adjust the padding as needed
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UniCanteenTopBar()

        // Show a message on top for success or failure
        updateMessage?.let {
            Snackbar(
                modifier = Modifier.padding(8.dp),
                content = {
                    Text(text = it, color = if (it == "Table number updated successfully") Color.Green else Color.Red)
                }
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
        // Rectangle Box with "Table No" text inside
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(180.dp)
                .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))  // Border around the box
                .background(MaterialTheme.colorScheme.onPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Table No.",  // Title inside the box
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Input Text Field
        OutlinedTextField(
            value = tableNoInput,
            onValueChange = { newValue ->
                // Update input field state only if it's a valid number
                if (newValue.all { it.isDigit() }) {
                    tableNoInput = newValue
                }
            },
            label = { Text("Enter Table Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(0.6f)
        )

        Spacer(modifier = Modifier.height(60.dp))

        ConfirmButton(
            onClick = {
                // Validate the table number input
                if (tableNoInput.isNotEmpty()) {
                    val tableNo = tableNoInput.toIntOrNull() // Safely parse the input

                    // Check if the table number is valid
                    if (tableNo != null && tableNo > 0 && tableNo <= 30) {
                        // Call ViewModel to update table number
                        viewModel.updateTableNo(userId, orderId, tableNo)
                        updateMessage = viewModel.updateStatusMessage

                        // Optionally navigate back if the update was successful
                        if (updateMessage == "Table number updated successfully") {
                            navController.navigate("choosePayment/$userId/$orderId")
                        }
                    } else {
                        updateMessage = "Please enter a valid table number (1-30)"
                    }
                } else {
                    updateMessage = "Please enter a valid table number"
                }
            }
        )


        Spacer(modifier = Modifier.height(70.dp))

        // Back Arrow Button
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }

}

@Composable
fun ConfirmButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Track the click state to update the color
    var isClicked by remember { mutableStateOf(false) }

    // Define button colors based on click state
    val buttonColor = if (isClicked) Color(0xFF388E3C) else Color(0xFF4CAF50) // Change to a darker green when clicked

    Button(
        onClick = {
            isClicked = true  // Change state when clicked
            onClick()  // Execute the onClick action
        },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        modifier = modifier.fillMaxWidth(0.6f) // Adjust width as needed
    ) {
        Text("Confirm", color = Color.White, fontSize = 18.sp)
    }
}
