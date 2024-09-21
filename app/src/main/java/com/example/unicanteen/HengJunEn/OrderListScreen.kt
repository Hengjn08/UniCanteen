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
import com.example.unicanteen.ui.theme.UniCanteenTheme

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
    ) {
            innerPadding ->
        testing1(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun testing1(
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