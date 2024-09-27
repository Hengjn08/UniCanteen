package com.example.unicanteen.Pierre

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.R
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.database.PierreAdminRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider

object paymentReceiptDestination : NavigationDestination {
    override val route = "payment_receipt/{userId}/{orderId}"
    override val title = "payment_receipt"
    // Create a function to generate the route with arguments
    fun routeWithArgs(userId: Int, orderId: Int): String {
        return "payment_receipt/$userId/$orderId"
    }
}

@Composable
fun paymentReceiptScreen(
    navController: NavController,
    currentDestination: NavDestination?,
    sellerAdminRepository: PierreAdminRepository,
    userId: Int,  // Accept sellerId as a parameter
    orderId: Int,
    modifier: Modifier = Modifier,
){
    val viewModel: AdminViewModel = viewModel(
        factory = AppViewModelProvider.Factory(pierreAdminRepository = sellerAdminRepository)
    )
    // Load the order details using the orderId and userId
    LaunchedEffect(orderId, userId) {
        viewModel.loadPaymentRecipt(orderId, userId)
        viewModel.loadOrderListPaymentRecipt(orderId, userId)
    }
    // Collect StateFlow for monthly sales data
    val paymentReceiptDatas by viewModel.paymentReceiptData.collectAsState()
    val paymentOrderDetailsData by viewModel.paymentOrderDetailsData.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UniCanteenTopBar()
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentDestination = currentDestination,
                isSeller = false
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Title: Payment Receipt
                    Text(
                        text = "Payment Receipt",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    // Check if the list is not empty before displaying the orderType
                    if (paymentReceiptDatas.isNotEmpty()) {
                        // Outer border for the payment details
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(2.dp, Color.Gray) // Outer border
                                .padding(16.dp) // Inner padding for content
                        ) {
                            // Header Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Food Name",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color.Blue),
                                    modifier = Modifier.weight(2f).padding(8.dp)
                                )
                                Text(
                                    text = "Unit Price (RM)",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color.Blue),
                                    modifier = Modifier.weight(1f).padding(8.dp)
                                )
                                Text(
                                    text = "Total Price (RM)",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color.Blue),
                                    modifier = Modifier.weight(1f).padding(8.dp)
                                )
                            }

                            Divider(color = Color.Gray, thickness = 1.dp) // Divider line

                            LazyColumn {
                                items(paymentOrderDetailsData) { paymentDetails ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // Combining food name and quantity
                                        Text(
                                            text = "${paymentDetails.foodName} (qty x ${paymentDetails.foodQty})",
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.weight(2f).padding(8.dp)
                                        )
                                        Text(
                                            text = String.format("%.2f", paymentDetails.unitPrice),
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.weight(1f).padding(8.dp)
                                        )
                                        Text(
                                            text = String.format("%.2f", paymentDetails.totalPrice),
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.weight(1f).padding(8.dp)
                                        )
                                    }

                                    Divider(color = Color.LightGray, thickness = 0.5.dp) // Divider line for each row
                                }
                            }
                        }

                        // Displaying additional payment receipt data
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(2.dp, Color.Gray) // Outer border
                                .padding(16.dp) // Inner padding for content
                        ) {
                            paymentReceiptDatas.forEach { paymentDetails ->
                                Text(
                                    text = "User Name: ${paymentDetails.userName}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Text(
                                    text = "Order ID: ${paymentDetails.orderId}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Text(
                                    text = "Total Amount: RM ${paymentDetails.totalAmt}",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color.Blue),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Text(
                                    text = "Create Date: ${paymentDetails.createDate}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Text(
                                    text = "Payment Type: ${paymentDetails.payType}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Text(
                                    text = "Status: ${paymentDetails.status}",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color.Blue),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No data available",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = Color.Black  // Set text color
                        )
                    }

                    // Continue Button
                    Button(
                        onClick = {
                            // Handle button click (e.g., navigate to another screen)
//                            navController.navigate("Order_List_Status/$userId")
//                            navController.navigate(OrderListStatusDestination.routeWithArgs(userId))

                        },
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = Color.Blue,
                            contentColor = Color.White, // Text color
                            disabledContainerColor = Color.Gray, // Background color when disabled
                            disabledContentColor = Color.LightGray // Text color when disabled
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp) // Add padding around the button
                    ) {
                        Text(
                            text = "Continue",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    )
}