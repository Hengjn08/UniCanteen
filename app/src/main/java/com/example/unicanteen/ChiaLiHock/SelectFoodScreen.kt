package com.example.unicanteen
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.wear.compose.material.ChipDefaults
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.ChiaLiHock.CartViewModel
import com.example.unicanteen.ChiaLiHock.SelectFoodViewModel
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppShapes
import com.example.unicanteen.ui.theme.AppViewModelProvider

object SelectFoodDestination : NavigationDestination {
    override val route = "food_select"
    override val title = ""
    const val sellerIdArg = "sellerId" // This should refer to the seller's ID
    val routeWithArgs = "$route/{$sellerIdArg}" // Full route with arguments
}


@Composable
fun SelectFoodScreen(
    userId: Int,
    orderRepository: OrderRepository,
    orderListRepository: OrderListRepository,
    foodListRepository: FoodListRepository,
    sellerId: Int, // Seller ID to filter food by the selected restaurant
    navController: NavController,
    currentDestination: NavDestination?
) {
    // Create the ViewModel
    val viewModel: SelectFoodViewModel = viewModel(
        factory = AppViewModelProvider.Factory(foodListRepository = foodListRepository)
    )
    val cartViewModel: CartViewModel = viewModel(
        factory = AppViewModelProvider.Factory(orderRepository = orderRepository, orderListRepository = orderListRepository)
    )

    // Observe the food list and selected food type from the ViewModel
    val foods by viewModel.filteredFoods.collectAsState() // This will be filtered based on food type
    val cartItemCount by cartViewModel.cartItemCount.observeAsState(0)
    val selectedFoodType by viewModel.selectedFoodType.collectAsState() // To track the selected food type
    val foodTypes by viewModel.foodTypes.collectAsState()
    val shopName by viewModel.shopName.collectAsState()


    LaunchedEffect(sellerId) {
        viewModel.loadFoodTypeBySellerId(sellerId)
    }

    LaunchedEffect(sellerId) {
        viewModel.getShopNameBySellerId(sellerId)
    }

    // Trigger the ViewModel to fetch food list by sellerId when screen is first launched
    LaunchedEffect(sellerId) {
        sellerId?.let {
            viewModel.loadFoodsBySellerId(it)
        }
    }

    LaunchedEffect(userId) {
        cartViewModel.fetchCartItemsCount(userId)
    }

    Column {
        // Top Bar with title
        UniCanteenTopBar(title = shopName)

        // Search and Cart bar with search function and cart action
        SearchAndCartBar(
            onSearch = { query ->
                // Perform food search based on the entered query
                viewModel.searchFoodsByName(query)
            },
            onCartClick = {
                navController.navigate("${CartDestination.route}")
            },
            cartItemCount
        )
        FoodTypeFilterBar(
            selectedType = selectedFoodType,
            onFoodTypeSelected = { type ->
                viewModel.filterFoodsByType(type)
            },
            foodTypes = foodTypes
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
fun FoodTypeFilterBar(
    selectedType: String?,
    onFoodTypeSelected: (String) -> Unit,
    foodTypes: List<String>
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp), // Add space between items
    ) {
        items(foodTypes) { type ->
            FilterChip(
                selected = selectedType == type,
                elevation = FilterChipDefaults.filterChipElevation(disabledElevation = 4.dp),
                onClick = { onFoodTypeSelected(type) },
                label = {
                    Text(
                        text = type,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selectedType == type) FontWeight.Bold else FontWeight.Normal, // Bold the selected chip
                        color = if (selectedType == type) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Justify,
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .wrapContentSize(align = Alignment.Center) // Make each chip have a reasonable minimum size
                    .height(40.dp).wrapContentSize(align = Alignment.Center),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = if (selectedType == type) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    labelColor = if (selectedType == type) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                ),
                shape = AppShapes.small, // Smooth rounded corners

            )
        }
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
        colors = CardDefaults.cardColors(containerColor =  MaterialTheme.colorScheme.onPrimary),
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
                    modifier = Modifier.padding(bottom = 4.dp),
                    color = MaterialTheme.colorScheme.onSecondary
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

