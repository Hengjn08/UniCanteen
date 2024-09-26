package com.example.unicanteen.Pierre

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.unicanteen.R
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.ui.theme.UniCanteenTheme

import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.database.PierreAdminRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider

object choosePayment : NavigationDestination {
    override val route = "choosePayment/{userId}/{orderId}"
    override val title = "choosePayment"
    fun routeWithArgs(userId: Int, orderId: Int): String {
        return "choosePayment/$userId/$orderId"
    }
}

@Composable
fun PaymentSelectionScreen(
    navController: NavController,
    currentDestination: NavDestination?,
    sellerAdminRepository: PierreAdminRepository,
    userId: Int,
    orderId: Int,
    modifier: Modifier = Modifier
){
    // State for showing messages after payment selection
    var updateMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentDestination = currentDestination,  // Handle destination as needed
                isSeller = true
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),  // Apply padding to avoid overlapping with bottom bar
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UniCanteenTopBar()

            // Display message if there is one
            updateMessage?.let {
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    content = {
                        Text(text = it, color = if (it.startsWith("Updated")) Color.Green else Color.Red)
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose Payment Method",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OptionButton2(
                    text = "Pay by Touch Ngo",
                    icon = R.drawable.touch_ngo_icon, // Replace with your actual icon resource
                    onClick = {
                        // Handle the payment via Touch Ngo
                        updateMessage = "Selected payment method: Pay by Touch Ngo"
                        navController.navigate("Order_List_Status/$userId/$orderId")
                    }
                )

                OptionButton2(
                    text = "Pay at Counter",
                    icon = R.drawable.counter_icon, // Replace with your actual icon resource
                    onClick = {
                        // Handle the payment at counter
                        updateMessage = "Selected payment method: Pay at Counter"
                        navController.navigate("Order_List_Status/$userId/$orderId")
                    }
                )

                BackButton(
                    onClick = { navController.navigateUp() }
                )
            }
        }
    }
}

@Composable
fun OptionButton2(text: String, icon: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(16.dp, bottom = 36.dp)
            .height(180.dp)
            .width(300.dp)
            .shadow(elevation = 4.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Icon on the left
            Image(
                painter = painterResource(id = icon),
                contentDescription = text,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
            // Text next to the icon
            Text(
                text = text,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}
