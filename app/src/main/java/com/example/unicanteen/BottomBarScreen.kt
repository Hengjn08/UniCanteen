package com.example.unicanteen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.unicanteen.Pierre.OrderListStatusDestination
import com.example.unicanteen.Pierre.reportSaleCheck

sealed class BottomBarScreen(
    val route: String,
     val title: String,
     val icon: ImageVector,
){
    object SellerHome: BottomBarScreen(
        route = "Home",
        title = "Home",
        icon = Icons.Filled.Home
    )
    object SellerOrderList: BottomBarScreen(
        route = "Order List",
        title = "Order List",
        icon = Icons.AutoMirrored.Filled.List
    )
    object SellerReport: BottomBarScreen(
        route = reportSaleCheck.route,
        title = "Report",
        icon = Icons.Filled.Info
    )
    object SellerProfile: BottomBarScreen(
        route = "Profile",
        title = "Profile",
        icon = Icons.Filled.Person
    )

        object CustomerHome : BottomBarScreen(
        route = SelectRestaurantDestination.route,
        title = "Home",
        icon = Icons.Filled.Home
    )
    object CustomerOrderList : BottomBarScreen(
        route = OrderListStatusDestination.route,
        title = "Order List",
        icon = Icons.AutoMirrored.Filled.List
    )
    object CustomerProfile : BottomBarScreen(
        route = "Customer Profile",
        title = "Profile",
        icon = Icons.Filled.Person
    )
}