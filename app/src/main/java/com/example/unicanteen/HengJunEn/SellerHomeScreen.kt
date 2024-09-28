package com.example.unicanteen.HengJunEn

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
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
    onFoodClick: (Int) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    currentDestination: NavDestination?,
    sellerId: Int?,
    foodListRepository: FoodListRepository,
){
    val viewModel: SellerHomeViewModel = viewModel(
        factory = AppViewModelProvider.Factory(null,foodListRepository)
    )

    // Observe the food list from the ViewModel
    val foods by viewModel.foods.collectAsState()

    // Trigger the ViewModel to fetch food list by sellerId when screen is first launched
    LaunchedEffect(sellerId) {
        sellerId?.let {
            viewModel.displayFoodsBySellerId(it)
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UniCanteenTopBar()
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
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SellerHomeBody(
    onAvailableChanged: (FoodList,Boolean) -> Unit,
    foods: List<FoodList>,
    onFoodClick: (Int) -> Unit,
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
            //available = available,
            onAvailableChanged = onAvailableChanged,
            foods = foods,
            onFoodClick = onFoodClick,
        )
    }

}

//To display list of food cards
@Composable
private fun FoodList(
    onAvailableChanged: (FoodList,Boolean) -> Unit,
    foods: List<FoodList>,
    onFoodClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier.fillMaxSize()){
        items(foods) { food ->
            FoodCard(
                food = food,
                modifier = Modifier.clickable{onFoodClick(food.foodId)},
                onAvailableChanged = { isAvailable ->
                    onAvailableChanged(food, isAvailable)
                },
            )
            if (foods.indexOf(food) < foods.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = 12.dp,
                        horizontal = 8.dp
                    ),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun FoodCard(
    food: FoodList,
    onAvailableChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Displaying image from URL using Coil's AsyncImage
            AsyncImage(
                model = food.imageUrl,  // Image URL from FoodList entity
                contentDescription = "Food Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(AppShapes.small)
                    .weight(1f),
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier.weight(2f)) {
                Text(
                    text = food.foodName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.rm, food.price),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            AvailableFood(
                available = food.status == "Available",
                onAvailableChanged = onAvailableChanged,
                modifier = Modifier.weight(1f)
            )
        }
    }
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