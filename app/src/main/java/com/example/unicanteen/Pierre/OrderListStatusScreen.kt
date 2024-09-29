package com.example.unicanteen.Pierre

import android.app.Application
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.HengJunEn.SellerHomeViewModel
import com.example.unicanteen.R
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.database.OrderListDao
import com.example.unicanteen.database.PierreAdminRepository
import com.example.unicanteen.database.UserRepository
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
    application: Application, // Pass application context
    navController: NavController,
    currentDestination: NavDestination?,
    sellerAdminRepository: PierreAdminRepository,
    userId: Int,  // Accept sellerId as a parameter
    modifier: Modifier = Modifier,
) {

    val viewModel: AdminViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,pierreAdminRepository = sellerAdminRepository)
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
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isPortrait) {
                UniCanteenTopBar()

            }
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
                            text = if (orderType == "Delivery") {
                                "Order Type: $orderType, Table No: $tableNo"
                            } else {
                                "Order Type: $orderType"
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSecondary  // Set text color
                        )
                    }
                }

                // Display status options with indicators
                OrderStatusHeader()

                // Call ListStatusColumn or ListStatusRow based on screen orientation
                if (isPortrait) {
                    ListStatusColumn(orderListData = orderListData,viewModel)
                } else {
                    ListStatusRow(orderListData = orderListData,viewModel)
                }
            }
        }
    )
}


@Composable
fun ListStatusColumn(orderListData: List<OrderListDao.OrderDetailsData>, viewModel: AdminViewModel) {
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
                orderId = orderDetails.orderListId,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ListStatusRow(orderListData: List<OrderListDao.OrderDetailsData>, viewModel: AdminViewModel) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orderListData) { orderDetails ->
            // Wrap each item in a Box or Card with increased width
            Card(
                modifier = Modifier
                    .width(300.dp)  // Increase width for better visibility of text
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                // Custom OrderItem composable for horizontal view
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    LoadFoodImage(
                        foodImagePath = orderDetails.foodImage,
                        viewModel = viewModel,
                        modifier = Modifier
                            .size(80.dp)  // Set a larger size for the image
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))  // Add spacing between image and text

                    Column(
                        modifier = Modifier
                            .weight(1f)  // Use remaining width for text content
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = orderDetails.sellerShopName,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis  // Truncate text if too long
                        )
                        Text(
                            text = orderDetails.foodName,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis  // Truncate text if too long
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Status: ${orderDetails.orderStatus}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
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
    viewModel: AdminViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(8.dp))
            .border(0.5.dp, MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp)) // Add a black border
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        LoadFoodImage(
            foodImagePath = foodImageUrl,
            viewModel =  viewModel,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = shopName, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color.Black)
            Text(text = foodName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSecondary)
            // Display the orderId below the food name
            Text(text = "Order ID: $orderId", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondary)
        }

        // Show only the icon for the order status
        val iconResId = when (orderStatus) {
            "Pending" -> R.drawable.pending
            "Preparing" -> R.drawable.processing
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
@Composable
private fun LoadFoodImage(
    foodImagePath: String,
    viewModel: AdminViewModel,
    modifier: Modifier = Modifier
) {
    var imageUrl by remember { mutableStateOf<String?>(null) }

    // Fetch the latest image URL from Firebase when the Composable is first launched
    LaunchedEffect(foodImagePath) {
        viewModel.getLatestImageUrl(foodImagePath) { url ->
            imageUrl = url
        }
    }

    // Display the image when the URL is available
    AsyncImage(
        model = imageUrl,
        contentDescription = "Food Image",
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}

