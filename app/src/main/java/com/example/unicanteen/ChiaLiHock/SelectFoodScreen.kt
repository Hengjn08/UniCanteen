package com.example.unicanteen
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.ChiaLiHock.SelectFoodViewModel
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider

object SelectFoodDestination : NavigationDestination {
    override val route = "food_select"
    override val title = ""
    const val userIdArg = "userId" // This should refer to the seller's ID
    val routeWithArgs = "$route/{$userIdArg}" // Full route with arguments
}


@Composable
fun SelectFoodScreen(
    foodListRepository: FoodListRepository,
    sellerId: Int?, // Seller ID to filter food by the selected restaurant
    navController: NavController,
    currentDestination: NavDestination?
) {
    // Create the ViewModel
    val viewModel: SelectFoodViewModel = viewModel(
        factory = AppViewModelProvider.Factory(foodListRepository=foodListRepository)
    )

    // Observe the food list from the ViewModel
    val foods by viewModel.foods.collectAsState()

    // Trigger the ViewModel to fetch food list by sellerId when screen is first launched
    LaunchedEffect(sellerId) {
        sellerId?.let {
            viewModel.loadFoodsBySellerId(it)
        }
    }

    Column() {
        // Top Bar with title
        UniCanteenTopBar(title = "Food Menu")

        // Search and Cart bar with search function and cart action
        SearchAndCartBar(
            onSearch = { query ->
                // Perform food search based on the entered query
                viewModel.searchFoodsByName(query)
            },
            onCartClick = {
                // Handle cart click logic here (e.g., navigate to the cart screen)
            }
        )

        // Food List Column, using LazyColumn for food items
        Column(modifier = Modifier.weight(1f)) {
            FoodList(foods = foods, navController = navController)
        }

        // Bottom Navigation Bar
        BottomNavigationBar(
            navController = navController,
            currentDestination = currentDestination,
            isSeller = false
        )
    }
}


@Composable
fun FoodCard(food: FoodList, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
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
                    text = food.foodName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "RM ${"%.2f".format(food.price)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Image(
                painter = rememberAsyncImagePainter(food.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
fun FoodList(foods: List<FoodList>, navController: NavController, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(foods) { food ->
            FoodCard(food = food, onClick = {
                navController.navigate("${FoodDetailCustomerDestination.route}/${food.foodId}") // Navigate to food detail with foodId

            })
        }
    }
}

