package com.example.unicanteen
import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.database.Seller
import com.example.unicanteen.navigation.NavigationDestination
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.ChiaLiHock.CartViewModel
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.ui.theme.AppViewModelProvider
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyRow
import androidx.lifecycle.LiveData
import com.example.unicanteen.LimSiangShin.OrderHistoryViewModel
import com.example.unicanteen.LimSiangShin.UserViewModel
import com.example.unicanteen.database.OrderList
import com.example.unicanteen.database.UserDao
import com.example.unicanteen.database.UserRepository

object OrderHistoryDestination : NavigationDestination {
    override val route = "order_history/{userId}"
    override val title = "order_history"
    fun routeWithArgs(userId: Int): String {
        return "order_history/$userId"
    }
}

@Composable
fun OrderHistoryScreen(
    application: Application, // Pass application context
    userId: Int,
    navController: NavController,
    currentDestination: NavDestination?,
    userRepository: UserRepository
) {
    val userViewModel: UserViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,userRepository = userRepository)
    )

    // Fetch cart item count when userId changes
    LaunchedEffect(userId) {
        userViewModel.fetchOrderDetails(userId)
    }
    val orderDetails by userViewModel.historyorderDetails.collectAsState(emptyList())

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
            // Main content layout with padding
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                when {
                    orderDetails.isEmpty() -> {
                        // Show message if order list is empty
                        Text(
                            text = "No Orders Available",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    else -> {
                        // Show order list in the respective layout (column or row)
                        if (isPortrait) {
                            OrderListColumn(orderList = orderDetails, navController = navController,userId)
                        } else {
                            OrderListRow(orderList = orderDetails, navController = navController,userId)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun OrderListColumn(orderList: List<UserDao.OrderDetails>, navController: NavController, userId: Int) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp) // Padding for the list
    ) {
        items(orderList) { orderItem ->
            OrderCard(
                orderList = orderItem,
                onClick = {
                    navController.navigate("payment_receipt/$userId/${orderItem.orderId}")
                }
            )
        }
    }
}

@Composable
fun OrderListRow(orderList: List<UserDao.OrderDetails>, navController: NavController, userId: Int) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Padding for the row
        contentPadding = PaddingValues(horizontal = 8.dp) // Horizontal padding for the list
    ) {
        items(orderList) { orderItem ->
            OrderCard(
                orderList = orderItem,
                onClick = {
                    navController.navigate("payment_receipt/$userId/${orderItem.orderId}")
                }
            )
        }
    }
}



@Composable
fun OrderCard(orderList: UserDao.OrderDetails, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        if (isPortrait) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = orderList.createDate,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 4.dp),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = orderList.totalAmount.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .defaultMinSize(minWidth = 200.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = orderList.createDate,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 4.dp),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = orderList.totalAmount.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}




