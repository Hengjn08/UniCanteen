package com.example.unicanteen
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.database.Seller
import com.example.unicanteen.navigation.NavigationDestination
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.unicanteen.HengJunEn.SellerHomeViewModel
import com.example.unicanteen.database.SellerDao
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.ui.theme.AppViewModelProvider

import com.example.unicanteen.ui.theme.UniCanteenTheme

object SelectRestaurantDestination : NavigationDestination {
    override val route = "restaurant_select"
    override val title = ""
    const val sellerIdArg = "sellerId"  // Change to sellerId to avoid confusion with foodId
    val routeWithArgs = "$route/{$sellerIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRestaurantScreen(
    navController: NavController,
    currentDestination: NavDestination?,
    onRestaurantClick: (Seller) -> Unit,
    selectRestaurantViewModel: SelectRestaurantViewModel = viewModel(factory = AppViewModelProvider.Factory)
    //sellerRepository: SellerRepository  // Pass repository here
) {
//    val viewModel: SelectRestaurantViewModel = viewModel(
//        factory = AppViewModelProvider.Factory(sellerRepository)
//    )

    //val sellers by viewModel.sellers.collectAsState()
    val selectRestaurantUiState by selectRestaurantViewModel.selectRestaurantUiState.collectAsState()

    Column {
        UniCanteenTopBar()
        SearchBar(onSearch = { query ->
            // You can implement search functionality here if needed
        })
        Column(modifier = Modifier.weight(1f)) {
            RestaurantList(
                sellers = selectRestaurantUiState.itemList,
                navController = navController,
                onRestaurantClick = onRestaurantClick
            )
        }
        BottomNavigationBar(
            navController = navController,
            currentDestination = currentDestination,
            isSeller = false
        )
    }
}

@Composable
fun RestaurantCard(seller: Seller, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick), // Pass the onClick to handle navigation
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
                    text = seller.shopName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = seller.description ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Image(
                painter = rememberAsyncImagePainter(seller.shopImage),
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
fun RestaurantList(
    sellers: List<Seller>,
    navController: NavController,
    onRestaurantClick: (Seller) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(sellers) { seller ->
            RestaurantCard(seller = seller,
                onClick = {onRestaurantClick(seller)}
                // Pass the seller name when navigating to SelectFoodScreen
                //navController.navigate("${SelectFoodScreen.route}/${seller.name}")
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRestaurantList() {



}