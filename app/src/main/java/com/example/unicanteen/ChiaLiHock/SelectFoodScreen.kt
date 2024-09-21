package com.example.unicanteen
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.rememberNavController
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.UniCanteenTheme

object SelectFoodDestination : NavigationDestination {
    override val route = "food_select"
    override val title = ""
    const val restaurantNameArg = "restaurantName"  // Change to restaurantName for clarity
    val routeWithArgs = "$route/{$restaurantNameArg}"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFoodScreen(
    sampleFoods: List<Food>,
    restaurantName: String?,
    navController: NavController // Add NavController to handle navigation
    //currentDestination: NavDestination?
) {
    Column {
        UniCanteenTopBar(
            title = restaurantName
        )
        SearchBar(
            onSearch = {}
        )
        Column(modifier = Modifier.weight(1f)) {
            FoodList(foods = sampleFoods, navController = navController) // Pass NavController
        }
//        BottomNavigationBar(
//            navController = navController,
//            currentDestination = currentDestination,
//            isSeller = false
//        )
    }
}

@Composable
fun FoodCard(food: Food, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick), // Make the card clickable
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
                    text = food.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "RM ${"%.2f".format(food.price)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Image(
                painter = painterResource(food.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp) // Fixed size for the image
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}
@Composable
fun FoodList(foods: List<Food>, navController: NavController, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier.fillMaxSize() // Removed the fixed height for flexibility
    ) {
        items(foods) { food ->
            FoodCard(food = food, onClick = {
                // Navigate or perform an action on food click
                navController.navigate("foodDetail/${food.name}") // Example navigation to food detail
            })
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewFoodList() {
    val sampleFoods = listOf(
        Food(
            name = "Malaysian Traditional Food",
            description = "Authentic Malaysian cuisine.",
            imageRes = R.drawable.pan_mee,
            price = 9.9
        ),
        Food(
            name = "Vegetarian Friendly",
            description = "Delicious vegetarian dishes.",
            imageRes = R.drawable.pan_mee,
            price = 9.9
        ),
        Food(
            name = "Nan Yang Kopitiam",
            description = "Best coffee and snacks.",
            imageRes = R.drawable.pan_mee,
            price = 9.9
        ),
        Food(
            name = "Nan Yang Kopitiam",
            description = "Best coffee and snacks.",
            imageRes = R.drawable.pan_mee,
            price = 0.9
        ),
        Food(
            name = "Nan Yang Kopitiam",
            description = "Best coffee and snacks.",
            imageRes = R.drawable.pan_mee,
            price = 9.9
        ),
        Food(
            name = "Nan Yang Kopitiam",
            description = "Best coffee and snacks.",
            imageRes = R.drawable.pan_mee,
            price = 9.9
        )
    )
    UniCanteenTheme {
        //SelectFoodScreen(sampleFoods,"restaurantName", rememberNavController())

    }


}