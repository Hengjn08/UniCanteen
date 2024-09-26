package com.example.unicanteen.HengJunEn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                //title = "UniCanteen",
                //canNavigateBack = false,
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
){
    Column(modifier = modifier) {
        Text(
            text = "Order list"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OrderListPreview() {
    UniCanteenTheme {
        //OrderListScreen()
    }
}