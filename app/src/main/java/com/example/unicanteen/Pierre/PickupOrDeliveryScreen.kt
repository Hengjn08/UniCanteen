package com.example.unicanteen.Pierre

import android.app.Application
import android.content.res.Configuration
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.database.PierreAdminRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider

object pickUpChoose : NavigationDestination {
    override val route = "pickUp/{userId}/{orderId}"
    override val title = "pickUp"
    fun routeWithArgs(userId: Int, orderId: Int): String {
        return "pickUp/$userId/$orderId"
    }
}

@Composable
fun PickupOrDeliveryScreen(
    application: Application, // Pass application context
    navController: NavController,
    currentDestination: NavDestination?,
    sellerAdminRepository: PierreAdminRepository,
    userId: Int,
    orderId: Int,
    modifier: Modifier = Modifier

) {

    // Initialize the ViewModel
    val viewModel: AdminViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,pierreAdminRepository = sellerAdminRepository)
    )
    // State for showing messages after update
    var updateMessage by remember { mutableStateOf<String?>(null) }
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isPortrait) {
                UniCanteenTopBar()
            }
        },
        content = { paddingValues ->
            if (isPortrait) {
                // Portrait layout
                PortraitOrderTypeSelection(
                    viewModel = viewModel,
                    orderId = orderId,
                    userId = userId,
                    updateMessage = updateMessage,
                    onUpdateMessageChange = { updateMessage = it },
                    navController = navController,
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                // Landscape layout
                LandscapeOrderTypeSelection(
                    viewModel = viewModel,
                    orderId = orderId,
                    userId = userId,
                    updateMessage = updateMessage,
                    onUpdateMessageChange = { updateMessage = it },
                    navController = navController,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    )
}

@Composable
fun PortraitOrderTypeSelection(
    viewModel: AdminViewModel,
    orderId: Int,
    userId: Int,
    updateMessage: String?,
    onUpdateMessageChange: (String?) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display message if there is one
        updateMessage?.let {
            Snackbar(
                modifier = Modifier.padding(8.dp),
                content = {
                    Text(text = it, color = if (it.startsWith("Order")) Color.Green else Color.Red)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))  // Spacer between Snackbar and buttons

        // Display buttons vertically
        OptionButton(
            text = "Pickup",
            icon = R.drawable.pickup,
            onClick = {
                viewModel.updateOrderType(orderId, userId, "Pickup") { success ->
                    onUpdateMessageChange(
                        if (success) "Order type updated to Pickup"
                        else "Failed to update order type"
                    )
                    if (success) {
                        navController.navigate("choosePayment/$userId/$orderId")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))  // Spacer between buttons

        OptionButton(
            text = "Delivery",
            icon = R.drawable.delivery,
            onClick = {
                viewModel.updateOrderType(orderId, userId, "Delivery") { success ->
                    onUpdateMessageChange(
                        if (success) "Order type updated to Delivery"
                        else "Failed to update order type"
                    )
                    if (success) {
                        navController.navigate("input_table/$userId/$orderId")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))  // Spacer between buttons

        BackButton(onClick = { navController.navigateUp() })
    }
}

@Composable
fun LandscapeOrderTypeSelection(
    viewModel: AdminViewModel,
    orderId: Int,
    userId: Int,
    updateMessage: String?,
    onUpdateMessageChange: (String?) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display message if there is one
            updateMessage?.let {
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    content = {
                        Text(text = it, color = if (it.startsWith("Order")) Color.Green else Color.Red)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.width(32.dp))  // Spacer between message and buttons

        // Display buttons horizontally in a row
        OptionButton(
            text = "Pickup",
            icon = R.drawable.pickup,
            onClick = {
                viewModel.updateOrderType(orderId, userId, "Pickup") { success ->
                    onUpdateMessageChange(
                        if (success) "Order type updated to Pickup"
                        else "Failed to update order type"
                    )
                    if (success) {
                        navController.navigate("choosePayment/$userId/$orderId")
                    }
                }
            },
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.width(32.dp))  // Spacer between buttons

        OptionButton(
            text = "Delivery",
            icon = R.drawable.delivery,
            onClick = {
                viewModel.updateOrderType(orderId, userId, "Delivery") { success ->
                    onUpdateMessageChange(
                        if (success) "Order type updated to Delivery"
                        else "Failed to update order type"
                    )
                    if (success) {
                        navController.navigate("input_table/$userId/$orderId")
                    }
                }
            },
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.width(32.dp))  // Spacer between buttons and back button

        BackButton(onClick = { navController.navigateUp() })
    }
}

@Composable
fun BackButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(16.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.size(60.dp),
            tint = MaterialTheme.colorScheme.onSurface,

        )

    }
}

@Composable
fun OptionButton(text: String, icon: Int, onClick: () -> Unit,modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(16.dp,bottom = 36.dp)
            .height(180.dp)
            .width(300.dp)
            .shadow(elevation = 4.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = text,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}


