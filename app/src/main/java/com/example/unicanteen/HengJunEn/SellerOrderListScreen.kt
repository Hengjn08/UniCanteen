package com.example.unicanteen.HengJunEn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.UniCanteenTheme

//object SellerOrderListDestination : NavigationDestination {
//    override val route = "seller_order_list"
//    override val title = "Order List"
//    //const val foodIdArg = "foodId"
//    //val routeWithArgs = "$route/{$foodIdArg}"
//}

@Composable
fun OrderListScreen(
    navController: NavController,
    currentDestination: NavDestination?
){
    Scaffold(
        topBar = {
            UniCanteenTopBar(
            )
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
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun OrderListBody(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp) // Adding padding to the entire column
            .verticalScroll(rememberScrollState()) // Making the list scrollable
    ) {
        // Dummy data for demonstration, replace with actual data from ViewModel or state
        val orderList = listOf(
            OrderListItem(quantity = 2, foodName = "Vegan Burger", addOn = "Extra Cheese", totalPrice = 12.99),
            OrderListItem(quantity = 1, foodName = "Spicy Chicken", addOn = "Extra Sauce", totalPrice = 8.50)
        )

        // Loop through each order item and create a card
        orderList.forEach { orderItem ->
            OrderItemCard(orderItem)
            Spacer(modifier = Modifier.height(16.dp)) // Add space between cards
        }
    }
}

@Composable
fun OrderItemCard(
    orderItem: OrderListItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        //elevation = 4.dp // Gives some shadow to the card
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // First Row: Quantity and Food Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${orderItem.quantity}x ${orderItem.foodName}")
                //Text(text = orderItem.foodName, style = MaterialTheme.typography.h6)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Add-ons (Optional)
            //Text(text = "Add-ons: ${orderItem.addOn}", style = MaterialTheme.typography.body1)

            Spacer(modifier = Modifier.height(8.dp))

            // Total Price
            Text(
                text = "Total Price: $${orderItem.totalPrice}",
                //style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Done and Cancel Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Handle Done action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Done")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { /* Handle Cancel action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

// Sample data class for order items, replace with your actual data models
data class OrderListItem(
    val quantity: Int,
    val foodName: String,
    val addOn: String?,
    val totalPrice: Double
)

@Preview(showBackground = true)
@Composable
fun OrderListPreview() {
    UniCanteenTheme {
        //OrderListScreen()
    }
}