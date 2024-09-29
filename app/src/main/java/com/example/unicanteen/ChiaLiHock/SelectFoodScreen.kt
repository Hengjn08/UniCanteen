package com.example.unicanteen
import android.app.Application
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.wear.compose.material.ChipDefaults
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.ChiaLiHock.CartViewModel
import com.example.unicanteen.ChiaLiHock.OrderListViewModel
import com.example.unicanteen.ChiaLiHock.SelectFoodViewModel
import com.example.unicanteen.ChiaLiHock.SellerDetailsDestination
import com.example.unicanteen.HengJunEn.SellerFoodDetailsViewModel
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
    application: Application,
    userId: Int,
    orderRepository: OrderRepository,
    orderListRepository: OrderListRepository,
    foodListRepository: FoodListRepository,
    sellerId: Int,
    navController: NavController,
    currentDestination: NavDestination?
) {
    // ViewModel and state observation
    val viewModel: SelectFoodViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application, foodListRepository = foodListRepository)
    )
    val cartViewModel: CartViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application, orderRepository = orderRepository, orderListRepository = orderListRepository)
    )
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // State observation
    val foods by viewModel.filteredFoods.collectAsState()
    val cartItemCount by cartViewModel.cartItemCount.observeAsState(0)
    val selectedFoodType by viewModel.selectedFoodType.collectAsState()
    val foodTypes by viewModel.foodTypes.collectAsState()
    val shopName by viewModel.shopName.collectAsState()

    LaunchedEffect(sellerId) {
        viewModel.loadFoodTypeBySellerId(sellerId)
        viewModel.getShopNameBySellerId(sellerId)
        viewModel.loadFoodsBySellerId(sellerId)
    }

    LaunchedEffect(userId) {
        cartViewModel.fetchCartItemsCount(userId)
    }

    if (isPortrait) {
        // Portrait layout
        Column {
                UniCanteenTopBar(
                    title = shopName,
                    onTitleClick = { navController.navigate("${SellerDetailsDestination.route}/${sellerId}") }
                )

                SearchAndCartBar(
                    onSearch = { query -> viewModel.searchFoodsByName(query) },
                    onCartClick = { navController.navigate("${CartDestination.route}") },
                    cartItemCount = cartItemCount
                )

                FoodTypeFilterBar(
                    selectedType = selectedFoodType,
                    onFoodTypeSelected = { type -> viewModel.filterFoodsByType(type) },
                    foodTypes = foodTypes
                )
            Column(modifier = Modifier.weight(1f)) {
                FoodList(foods = foods, navController = navController, isPortrait = true, viewModel=cartViewModel)
            }
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination,
                    isSeller = false
                )


        }
    } else {
        // Landscape layout
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                SearchAndCartBar(
                    onSearch = { query -> viewModel.searchFoodsByName(query) },
                    onCartClick = { navController.navigate("${CartDestination.route}") },
                    cartItemCount = cartItemCount
                )

                FoodTypeFilterBar(
                    selectedType = selectedFoodType,
                    onFoodTypeSelected = { type -> viewModel.filterFoodsByType(type) },
                    foodTypes = foodTypes
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .padding(8.dp)
            ) {
                FoodList(foods = foods, navController = navController, isPortrait = false, viewModel = cartViewModel)
            }
        }
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
        horizontalArrangement = Arrangement.spacedBy(2.dp)
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
                        fontWeight = if (selectedType == type) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedType == type) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Justify
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .wrapContentSize(align = Alignment.Center)
                    .height(40.dp)
                    .wrapContentSize(align = Alignment.Center),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = if (selectedType == type) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    labelColor = if (selectedType == type) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                ),
                shape = AppShapes.small
            )
        }
    }
}

@Composable
fun FoodCard(food: FoodList, onClick: () -> Unit,viewModel: CartViewModel) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                food.description?.let {
                    Text(
                        text = it, // Display description in landscape mode
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            LoadFoodImage(
                foodImagePath = food.imageUrl,
                viewModel = viewModel,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
fun FoodCardLandscape(food: FoodList, onClick: () -> Unit,viewModel:CartViewModel) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            LoadFoodImage(
                foodImagePath = food.imageUrl,
                viewModel = viewModel,
                modifier = Modifier
                    .size(120.dp) // Larger image in landscape mode
                    .clip(RoundedCornerShape(16.dp))
            )
            //Spacer(modifier = Modifier.width(8.dp))
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
fun FoodList(foods: List<FoodList>, navController: NavController, isPortrait: Boolean,viewModel:CartViewModel) {
    if (isPortrait) {
        // Vertical list for portrait mode
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(foods) { food ->
                FoodCard(food = food, onClick = {
                    navController.navigate("${FoodDetailCustomerDestination.route}/${food.foodId}")
                },viewModel=viewModel)
            }
        }
    } else {
        // Grid layout for landscape mode
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Two columns for landscape mode
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(foods) { food ->
                FoodCardLandscape(food = food, onClick = {
                    navController.navigate("${FoodDetailCustomerDestination.route}/${food.foodId}")
                },viewModel=viewModel)
            }
        }
    }
}
@Composable
private fun LoadFoodImage(
    foodImagePath: String,
    viewModel: CartViewModel,
    modifier: Modifier = Modifier
) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    Log.d("FoodImagePath", "FoodImagePath: $foodImagePath")
    // Fetch the latest image URL from Firebase when the Composable is first launched
    LaunchedEffect(foodImagePath) {
        viewModel.getLatestImageUrl(foodImagePath) { url ->
            imageUrl = url
        }
    }

    // Display the image when the URL is available
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Food Image",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}


