package com.example.unicanteen.Pierre

import android.app.Application
import android.content.res.Configuration
import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalConfiguration
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
    application: Application, // Pass application context
    navController: NavController,
    currentDestination: NavDestination?,
    sellerAdminRepository: PierreAdminRepository,
    userId: Int,
    orderId: Int,
    modifier: Modifier = Modifier
){

    // State for showing messages after payment selection
    var updateMessage by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedPaymentType by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
// Initialize the ViewModel
    val viewModel: AdminViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,pierreAdminRepository = sellerAdminRepository)
    )
    Scaffold(topBar = {
        if (isPortrait) {
            UniCanteenTopBar()
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Payment Method",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (isPortrait) {
                portraitTypeSelection(viewModel, navController, orderId, userId)
            } else {
                landscapeTypeSelection(viewModel, navController, orderId, userId)
            }
        }
    }
}

@Composable
fun portraitTypeSelection(
    viewModel: AdminViewModel, // Replace with your ViewModel type
    navController: NavController,
    orderId: Int,
    userId: Int
) {
    var updateMessage by remember { mutableStateOf<String?>(null) }
    var selectedPaymentType by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }


    // Display message if there is one
    updateMessage?.let {
        Snackbar(
            modifier = Modifier.padding(8.dp),
            content = {
                Text(text = it, color = if (it.startsWith("Success")) Color.Green else Color.Red)
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OptionButton2(
            text = "Pay by Touch Ngo",
            icon = R.drawable.touch_ngo_icon,
            onClick = {
                updateMessage = "Success payment method: Pay by Touch Ngo"
                viewModel.createPayment(orderId = orderId, userId = userId, payType = "TnGo")
                selectedPaymentType = "Pay by Touch Ngo"
                showDialog = true
            }
        )

        OptionButton2(
            text = "Pay at Counter",
            icon = R.drawable.counter_icon,
            onClick = {
                updateMessage = "Success payment method: Pay at Counter"
                viewModel.createPayment(orderId = orderId, userId = userId, payType = "Pay at Counter")
                selectedPaymentType = "Pay at Counter"
                showDialog = true
            }
        )

        if (showDialog) {
            ConfirmationDialog(
                onConfirm = {
                    updateMessage = "Success payment method: $selectedPaymentType"
                    navController.navigate("payment_receipt/$userId/$orderId")
                },
                onDismiss = { showDialog = false },
                message = "Are you sure you want to proceed with $selectedPaymentType?"
            )
        }

        BackButton(
            onClick = { navController.navigateUp() }
        )
    }
}

@Composable
fun landscapeTypeSelection(
    viewModel: AdminViewModel, // Replace with your ViewModel type
    navController: NavController,
    orderId: Int,
    userId: Int
) {
    var updateMessage by remember { mutableStateOf<String?>(null) }
    var selectedPaymentType by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // Display message if there is one
    updateMessage?.let {
        Snackbar(
            modifier = Modifier.padding(8.dp),
            content = {
                Text(text = it, color = if (it.startsWith("Success")) Color.Green else Color.Red)
            }
        )
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        OptionButton2(
            text = "Pay by Touch Ngo",
            icon = R.drawable.touch_ngo_icon,
            onClick = {
                updateMessage = "Success payment method: Pay by Touch Ngo"
                viewModel.createPayment(orderId = orderId, userId = userId, payType = "TnGo")
                selectedPaymentType = "Pay by Touch Ngo"
                showDialog = true
            }
        )

        OptionButton2(
            text = "Pay at Counter",
            icon = R.drawable.counter_icon,
            onClick = {
                updateMessage = "Success payment method: Pay at Counter"
                viewModel.createPayment(orderId = orderId, userId = userId, payType = "Pay at Counter")
                selectedPaymentType = "Pay at Counter"
                showDialog = true
            }
        )

        if (showDialog) {
            ConfirmationDialog(
                onConfirm = {
                    updateMessage = "Success payment method: $selectedPaymentType"
                    navController.navigate("payment_receipt/$userId/$orderId")
                },
                onDismiss = { showDialog = false },
                message = "Are you sure you want to proceed with $selectedPaymentType?"
            )
        }

        BackButton(
            onClick = { navController.navigateUp() }
        )
    }
}

@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    message: String
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Confirm Payment") },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun OptionButton2(text: String, icon: Int, onClick: () -> Unit,modifier: Modifier = Modifier) {
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
