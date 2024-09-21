package com.example.unicanteen

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.unicanteen.ui.theme.UniCanteenTheme
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController



//enum class UniCanteenScreen(@StringRes val title: Int) {
//    Start(title = R.string.app_name),
//    Food(title = R.string.food),
//    FoodDetails(title = R.string.food_details),
//    Cart(title = R.string.cart)
//}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TopAppBar(
//    currentScreen: UniCanteenScreen,
//    modifier: Modifier = Modifier
//) {
//    CenterAlignedTopAppBar(
//        title = { Text(stringResource(currentScreen.title),
//            style = MaterialTheme.typography.headlineMedium,
//            textAlign = TextAlign.Justify,
//            color = Color.White,
//            modifier = Modifier.padding(35.dp))},
//        colors = TopAppBarDefaults.mediumTopAppBarColors(
//            containerColor = colorResource(id = R.color.orange_500)
//        ),
//        modifier = modifier.height(100.dp)
//
//    )
//}


@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = { query = it },
         //   label = { Text("Search by name", style = MaterialTheme.typography.bodyMedium,color = Color.Black)},
            placeholder = { Text("Search by name", style = MaterialTheme.typography.bodyMedium, color = Color.Gray) },
            modifier = Modifier
                .weight(1f)
                .clip(MaterialTheme.shapes.medium) // Rounded corners
                .background(Color.Black), // Background color
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                } else {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.LightGray,
                unfocusedContainerColor = colorResource(id = R.color.search_bar_background),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.LightGray,
            )
        )
        IconButton(onClick = { /* Handle bag click */ }) {
            Icon(Icons.Default.ShoppingCart, contentDescription = "Bag", modifier = Modifier.size(36.dp)) // Larger bag icon
        }
    }
}
//@Composable
//fun UniCanteenApp(
//    viewModel: UniCanteenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
//    navController: NavHostController = rememberNavController()
//) {
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen = UniCanteenScreen.valueOf(
//        backStackEntry?.destination?.route ?: UniCanteenScreen.Start.name
//    )
//
//    Scaffold{ innerPadding ->
//        NavHost(
//            navController = navController,
//            startDestination = UniCanteenScreen.Start.name,
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            composable(UniCanteenScreen.Start.name) {
//                SelectRestaurantScreen(
//                    onRestaurantSelected = { restaurantName ->
//                        viewModel.selectRestaurant(restaurantName)
//                        navController.navigate(UniCanteenScreen.Food.name)
//                    }
//                )
//            }
//            composable(UniCanteenScreen.Food.name) {
//                FoodScreen(
//                    onFoodSelected = { foodItem ->
//                        viewModel.selectFood(foodItem)
//                        navController.navigate(UniCanteenScreen.FoodDetails.name)
//                    }
//                )
//            }
//            composable(UniCanteenScreen.FoodDetails.name) {
//                val selectedFood = viewModel.selectedFood.value
//                if (selectedFood != null) {
//                    FoodDetailsScreen(
//                        foodItem = selectedFood,
//                        onAddToCart = {
//                            viewModel.addToCart(selectedFood)
//                            navController.navigate(UniCanteenScreen.Cart.name)
//                        }
//                    )
//                }
//            }
//            composable(UniCanteenScreen.Cart.name) {
//                val cartItems = viewModel.cartItems.value ?: emptyList()
//                CartScreen(
//                    cartItems = cartItems
//                )
//            }
//        }
//    }
//}
//@Composable
//fun SelectRestaurantScreen(onRestaurantSelected: (String) -> Unit) {
//    // Your UI code for selecting a restaurant
//    // When a restaurant is selected, call:
//    // onRestaurantSelected("Restaurant Name")
//}
//@Composable
//fun FoodScreen(onFoodSelected: (FoodItem) -> Unit) {
//    // Your UI code for displaying food items
//    // When a food item is selected, call:
//    // onFoodSelected(FoodItem(1, "Pizza", 12.99))
//}
//@Composable
//fun FoodDetailsScreen(foodItem: FoodItem, onAddToCart: () -> Unit) {
//    Column {
//        Text("Food: ${foodItem.name}")
//        Text("Price: ${foodItem.price}")
//        Button(onClick = { onAddToCart() }) {
//            Text("Add to Cart")
//        }
//    }
//}
//@Composable
//fun CartScreen(cartItems: List<FoodItem>) {
//    LazyColumn {
//        items(cartItems) { item ->
//            Text("Item: ${item.name}, Price: ${item.price}")
//        }
//    }
//}



//@Composable
//fun BottomAppBar(
//    onHomeClick: () -> Unit,
//    onOrderListClick: () -> Unit,
//    onProfileClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    NavigationBar(
//        containerColor = colorResource(id = R.color.orange_500),
//        modifier = modifier
//            .height(70.dp) // Adjust the height as needed
//            .clip(AppShapes.large) // Apply the large shape
//
//    ) {
//        NavigationBarItem(
//            icon = { Icon(Icons.Filled.Home, contentDescription = "Home", modifier = Modifier.size(30.dp))},
//            label = { Text("Home",style = MaterialTheme.typography.labelMedium,) },
//            selected = false,
//            onClick = onHomeClick,
//            modifier = Modifier.padding(bottom = 0.dp)
//
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Filled.Menu, contentDescription = "Order List", modifier = Modifier.size(30.dp)) },
//            label = { Text("Order List",style = MaterialTheme.typography.labelMedium) },
//            selected = false,
//            onClick = onOrderListClick,
//            modifier = Modifier.padding(bottom = 0.dp)
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile", modifier = Modifier.size(30.dp)) },
//            label = { Text("Profile",style = MaterialTheme.typography.labelMedium) },
//            selected = false,
//            onClick = onProfileClick,
//            modifier = Modifier.padding(bottom = 0.dp)
//        )
//    }
//}

