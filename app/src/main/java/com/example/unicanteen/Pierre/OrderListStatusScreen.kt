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
import androidx.compose.foundation.shape.RoundedCornerShape
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

object OrderListStatusDestination : NavigationDestination {
    override val route = "Order_List_Status?userId={userId}"
    override val title = "Order_List_Status"
    // Create a function to generate the route with arguments
    fun routeWithArgs(userId: Int): String {

        return "Order_List_Status?userId=$userId"

    }
}

@Composable
fun OrderListStatusScreen(
    navController: NavController,
    currentDestination: NavDestination?,
    sellerAdminRepository: PierreAdminRepository,
    userId: Int,  // Accept sellerId as a parameter
    modifier: Modifier = Modifier,
) {
    val viewModel: AdminViewModel = viewModel(
        factory = AppViewModelProvider.Factory(pierreAdminRepository = sellerAdminRepository)
    )
    LaunchedEffect(userId) {
        viewModel.getOrderId(userId)

    }
    val orderId by viewModel.OrderId.collectAsState()
    // Load the order details using the orderId and userId
    LaunchedEffect(orderId, userId) {
        viewModel.loadOrderDetails(orderId, userId)
        viewModel.getTableNo(userId, orderId)
    }

// Collect the table number state from the ViewModel
    val tableNo by viewModel.tableNo.collectAsState()
    // Collect StateFlow for monthly sales data
    val orderListData by viewModel.orderDetailsData.collectAsState()

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Title: Order Status
                Text(
                    text = "Order Status",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.CenterHorizontally)
                )
                // Check if the list is not empty before displaying the orderType
                if (orderListData.isNotEmpty()) {
                    // Extract orderType from the first record
                    val orderType = orderListData.first().orderType

                    // Display the orderType below the order status with a border around it
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Gray,  // Set the border color
                                shape = RoundedCornerShape(8.dp)  // Add rounded corners
                            )
                            .padding(12.dp)  // Padding inside the border
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = if(orderType == "Delivery"){"Order Type: $orderType, Table No: $tableNo "}
                            else {
                                "Order Type: $orderType"
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = Color.Black  // Set text color
                        )
                    }
                }
                // Display status options with indicators
                OrderStatusHeader()

                // List of orders with food images and status
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(orderListData) { orderDetails ->
                        OrderItem(
                            foodImageUrl = orderDetails.foodImage,
                            shopName = orderDetails.sellerShopName,
                            foodName = orderDetails.foodName,
                            orderStatus = orderDetails.orderStatus,
                            orderId = orderDetails.orderListId
                        )
                    }
                }
            }
        }
    )
}



@Composable
fun OrderStatusHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // Status indicators with text and icon beside each other
        StatusIndicator(status = "Pending", iconResId = R.drawable.pending, showText = true)
        StatusIndicator(status = "Processing", iconResId = R.drawable.processing, showText = true)
        StatusIndicator(status = "Complete", iconResId = R.drawable.complete, showText = true)
    }
}

@Composable
fun StatusIndicator(status: String, @DrawableRes iconResId: Int, showText: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Use Image instead of Icon for drawable resources
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = "$status indicator",
            modifier = Modifier.size(24.dp)
        )
        if (showText) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = status)
        }
    }
}

@Composable
fun OrderItem(
    foodImageUrl: String,
    shopName: String,
    foodName: String,
    orderStatus: String,
    orderId: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = foodImageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = shopName, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color.Black)
            Text(text = foodName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.Gray)
            // Display the orderId below the food name
            Text(text = "Order ID: $orderId", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }

        // Show only the icon for the order status
        val iconResId = when (orderStatus) {
            "Pending" -> R.drawable.pending
            "Processing" -> R.drawable.processing
            "Completed" -> R.drawable.complete
            else -> R.drawable.complete  // Fallback icon if needed
        }

        StatusIndicator(
            status = orderStatus,
            iconResId = iconResId,
            showText = false  // Show only the icon, no text
        )
    }
}
