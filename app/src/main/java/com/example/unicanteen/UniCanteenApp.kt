package com.example.unicanteen

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.unicanteen.navigation.UniCanteenNavHost

@Composable
fun UniCanteenApp(navController: NavHostController = rememberNavController()) {
    UniCanteenNavHost(navController = navController)
}

//Top Navigation Bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniCanteenTopBar(
    modifier: Modifier = Modifier,
    title: String? = "UniCanteen",
){
    CenterAlignedTopAppBar(
        title = { Text(text = title ?: "UniCanteen",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxHeight()
                .wrapContentHeight(Alignment.CenterVertically)
                .padding(20.dp))
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = colorResource(id = R.color.orange_500)
        ),
        modifier = modifier.height(120.dp)
    )
}

//Bottom navigation bar
@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentDestination: NavDestination?,
    isSeller: Boolean
){
    val items = if(isSeller){
        listOf(
            BottomBarScreen.SellerHome,
            BottomBarScreen.SellerOrderList,
            BottomBarScreen.SellerProfile
        )
    }
    else{
        listOf(
            BottomBarScreen.CustomerHome,
            BottomBarScreen.CustomerOrderList,
            BottomBarScreen.CustomerProfile
        )
    }

    Surface(
        color = colorResource(R.color.orange_500),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {

        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)) // Clip the shape
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp) // Adjust icon size
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp) // Adjust font size
                        )
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.DarkGray,
                        unselectedTextColor = Color.DarkGray
                    )
                )
            }
        }
    }
}