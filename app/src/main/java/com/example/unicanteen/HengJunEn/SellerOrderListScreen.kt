package com.example.unicanteen.HengJunEn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.ChiaLiHock.AddOnViewModel
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.database.AddOn
import com.example.unicanteen.database.AddOnRepository
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.OrderList
import com.example.unicanteen.database.OrderListDao
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme

object SellerOrderListDestination : NavigationDestination {
    override val route = "seller_order_list"
    override val title = "Order List"
    //const val foodIdArg = "foodId"
    //val routeWithArgs = "$route/{$foodIdArg}"
    fun routeWithArgs(sellerId: Int): String {
        return "$route/$sellerId"
    }
}

@Composable
fun OrderListScreen(
    navController: NavController,
    currentDestination: NavDestination?,
    sellerId: Int,
    orderListRepository: OrderListRepository,
){
    val orderListViewModel: SellerOrderListViewModel = viewModel(
        factory = AppViewModelProvider.Factory(orderListRepository = orderListRepository)
    )

    LaunchedEffect(sellerId) {
        orderListViewModel.loadOrderListDetails(sellerId)
    }

    val orderList by orderListViewModel.orderListDetails.collectAsState()

    Scaffold(
        topBar = {
            UniCanteenTopBar()
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentDestination = currentDestination,
                isSeller = true
            )
        }
    ) { innerPadding ->
        OrderListBody(
            modifier = Modifier.padding(innerPadding),
            orderList = orderList,
            onUpdateStatus = { orderList, newStatus ->
                orderListViewModel.updateOrderStatus(orderList, newStatus)
            }
        )
    }
}

@Composable
fun OrderListBody(
    modifier: Modifier = Modifier,
    orderList: List<OrderListDao.OrderListItemDetails>,
    onUpdateStatus: (OrderListDao.OrderListItemDetails, String) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (orderList.isEmpty()) {
            // Display message when there are no orders
            Text(
                text = "Currently No Order",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        } else {
            // Display order items when the order list is not empty
            orderList.forEach { orderItem ->
                OrderItemCard(
                    orderItem = orderItem,
                    onDoneClick = { onUpdateStatus(orderItem, "Completed") },
                    onCancelClick = { orderItemToCancel ->
                        onUpdateStatus(orderItem, "Cancelled") }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun OrderItemCard(
    orderItem: OrderListDao.OrderListItemDetails,
    onDoneClick: () -> Unit,
    onCancelClick: (OrderListDao.OrderListItemDetails) -> Unit // Change the type to accept the order item
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${orderItem.qty}x ${orderItem.foodName}",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (orderItem.addOns.isNotBlank()) {
                AddOnList(addOns = orderItem.addOns)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (!orderItem.remark.isNullOrEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray,
                    )
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        orderListRow(
                            label = "Remark:",
                            value = "${orderItem.remark}"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            orderListRow(
                label = "Total Price: RM",
                value = "${"%.2f".format(orderItem.totalPrice)}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            orderListRow(
                label = "Order Type: ",
                value = "${orderItem.orderType}"
            )

            if (orderItem.orderType == "Delivery") {
                orderItem.tableNo?.let { tableNo ->
                    orderListRow(
                        label = "Table No:  ",
                        value = "$tableNo"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDoneClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Done")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { showDialog = true }, // Show the dialog on click
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
            }
        }
    }

    // Show confirmation dialog when the cancel button is clicked
    if (showDialog) {
        CancelConfirmationDialog(
            onConfirm = {
                onCancelClick(orderItem) // Pass the order item to update its status
                showDialog = false // Close the dialog
            },
            onCancel = {
                showDialog = false // Just close the dialog
            },
        )
    }
}

@Composable
fun AddOnList(addOns: String) {
    // Split the addOns string by commas (or any other delimiter you use)
    val addOnList = addOns.split(",").map { it.trim() } // Trim any extra whitespace

    if (addOnList.isNotEmpty() && addOns.isNotBlank()) {
        Text(text = "Add-ons:", fontWeight = FontWeight.Bold)

        Column(modifier = Modifier.padding(start = 16.dp)) {
            addOnList.forEach { addOn ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "+")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = addOn)
                }
            }
        }
    }
}

@Composable
fun orderListRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = "$label",
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value
        )
    }
}

@Composable
private fun CancelConfirmationDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Cancel Food Order Confirmation") },
        text = { Text("Are you sure you want to cancel this food order?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text("No")
            }
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun OrderListPreview() {
    UniCanteenTheme {
        //OrderListScreen()
    }
}