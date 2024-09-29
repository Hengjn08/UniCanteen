package com.example.unicanteen.HengJunEn

import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.ChiaLiHock.SelectFoodViewModel
import com.example.unicanteen.R
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.data.Datasource
import com.example.unicanteen.database.AppDatabase
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.FoodListRepositoryImpl
import com.example.unicanteen.database.Seller
import com.example.unicanteen.model.Food
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppShapes
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme

object SellerHomeDestination : NavigationDestination {
    override val route = "seller_home"
    override val title = "Food List"
    const val sellerIdArg = "sellerId" // This should refer to the seller's ID
    fun routeWithArgs(sellerId: Int?): String {
        return "seller_home?sellerId=$sellerId"
    }
}

@Composable
fun SellerHomeScreen(
    application: Application, // Pass application context
    onFoodClick: (Int) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    currentDestination: NavDestination?,
    sellerId: Int,
    foodListRepository: FoodListRepository,
){
    val viewModel: SellerHomeViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,foodListRepository = foodListRepository)
    )

    // Observe the food list from the ViewModel
    val foods by viewModel.foods.collectAsState()
    val shopName by viewModel.shopName.collectAsState()

    // Trigger the ViewModel to fetch food list by sellerId when screen is first launched
    LaunchedEffect(sellerId) {
        viewModel.displayFoodsBySellerId(sellerId)
        viewModel.getShopNameBySellerId(sellerId)
    }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isPortrait) {
                UniCanteenTopBar(
                    title = "UniCanteen\n$shopName"
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentDestination = currentDestination,
                isSeller = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate(AddFoodDestination.route)},
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add icon",
                )
            }
        }
    ){ innerPadding ->
        SellerHomeBody(
            foods = foods,
            onFoodClick = onFoodClick,
            onAvailableChanged = { food, newStatus ->
                viewModel.updateFoodStatus(food, newStatus)
            },
            isPortrait = isPortrait,
            viewModel = viewModel,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SellerHomeBody(
    onAvailableChanged: (FoodList,Boolean) -> Unit,
    foods: List<FoodList>,
    onFoodClick: (Int) -> Unit,
    isPortrait: Boolean,
    viewModel: SellerHomeViewModel,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
    ){
        Text(
            text = SellerHomeDestination.title,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 16.dp)
        )

        FoodList(
            onAvailableChanged = onAvailableChanged,
            foods = foods,
            onFoodClick = onFoodClick,
            isPortrait = isPortrait,
            viewModel = viewModel
        )
    }

}

//To display list of food cards
@Composable
private fun FoodList(
    onAvailableChanged: (FoodList, Boolean) -> Unit,
    foods: List<FoodList>,
    onFoodClick: (Int) -> Unit,
    isPortrait: Boolean,
    viewModel: SellerHomeViewModel,
    modifier: Modifier = Modifier
) {
    if (isPortrait) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(foods) { food ->
                FoodCard(
                    food = food,
                    onAvailableChanged = { isAvailable -> onAvailableChanged(food, isAvailable) },
                    isPortrait = true,
                    viewModel = viewModel,
                    modifier = Modifier.clickable { onFoodClick(food.foodId) }
                )
                // Divider between items
                if (foods.indexOf(food) < foods.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
            }
        }
    } else {
        // Landscape with grid layout, showing 2 cards per row
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(foods) { food ->
                FoodCard(
                    food = food,
                    onAvailableChanged = { isAvailable -> onAvailableChanged(food, isAvailable) },
                    isPortrait = false,
                    viewModel = viewModel,
                    modifier = Modifier.clickable { onFoodClick(food.foodId) }
                )
            }
        }
    }
}


@Composable
fun FoodCard(
    food: FoodList,
    onAvailableChanged: (Boolean) -> Unit,
    isPortrait: Boolean,
    viewModel: SellerHomeViewModel,
    modifier: Modifier = Modifier
) {
    if (isPortrait) {
        // Portrait design (same as before)
        Card(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()  // Full width in portrait mode
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Display image
                LoadFoodImage(
                    foodImagePath = food.imageUrl,  // Firebase file path
                    viewModel = viewModel,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .weight(1f)
                )

                Column(modifier = Modifier.weight(2f)) {
                    Text(
                        text = food.foodName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Row() {
                        Text(
                            text = stringResource(R.string.rm, food.price),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                        AvailableFood(
                            available = food.status == "Available",
                            onAvailableChanged = onAvailableChanged,
                        )
                    }
                }
            }
        }
    } else {
        // Landscape design (2 sections: image on the left, details on the right)
        Card(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()  // Full width but structured as a horizontal row
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,  // Align items vertically centered
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                LoadFoodImage(
                    foodImagePath = food.imageUrl,  // Firebase file path
                    viewModel = viewModel,
                    modifier = Modifier
                        .size(100.dp)  // Adjust size for landscape mode
                        .clip(AppShapes.small)
                        .weight(1f)
                )


                Column(
                    modifier = Modifier
                        .weight(2f)  // Take more space for the text and switch
                        .padding(start = 16.dp)
                ) {
                    // Food name at the top
                    Text(
                        text = food.foodName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // Food description in landscape mode
                    food.description?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))  // Pushes the price and availability switch to the bottom

                    // Price and availability switch on the same line
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween  // Ensures price and switch are at opposite sides
                    ) {
                        // Price at the bottom-left
                        Text(
                            text = stringResource(R.string.rm, food.price),
                            fontSize = 16.sp,
                            color = Color.Black
                        )

                        // Availability switch at the bottom-right
                        AvailableFood(
                            available = food.status == "Available",
                            onAvailableChanged = onAvailableChanged
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadFoodImage(
    foodImagePath: String,
    viewModel: SellerHomeViewModel,
    modifier: Modifier = Modifier
) {
    var imageUrl by remember { mutableStateOf<String?>(null) }

    // Fetch the latest image URL from Firebase when the Composable is first launched
    LaunchedEffect(foodImagePath) {
        viewModel.getLatestImageUrl(foodImagePath) { url ->
            imageUrl = url
        }
    }

    // Display the image when the URL is available
    AsyncImage(
        model = imageUrl,
        contentDescription = "Food Image",
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}


//Switch to turn on/off availability of food
@Composable
fun AvailableFood(
    available: Boolean,
    onAvailableChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = available,
        onCheckedChange = onAvailableChanged,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End)
    )
}

@Preview(showBackground = true)
@Composable
fun UniCanteen() {
    UniCanteenTheme {
        //SellerHomeScreen(onFoodClick = {}, navController = rememberNavController(), currentDestination = null, sellerId = 1, foodListRepository = FoodListRepositoryImpl(AppDatabase.getDatabase(navController.context).foodListDao())
        //BottomNavigationBar()
    }
}