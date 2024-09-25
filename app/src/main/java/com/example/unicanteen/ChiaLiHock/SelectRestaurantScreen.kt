package com.example.unicanteen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.database.Seller
import com.example.unicanteen.navigation.NavigationDestination
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.ui.theme.AppViewModelProvider

object SelectRestaurantDestination : NavigationDestination {
    override val route = "restaurant_select"
    override val title = ""
    const val userIdArg = "userId"  // This should refer to the seller's ID
    const val restaurantNameArg = "restaurantName" // Name of the restaurant
}


@Composable
fun SelectRestaurantScreen(
    navController: NavController,
    currentDestination: NavDestination?,
    sellerRepository: SellerRepository, // Pass repository here

) {
    val viewModel: SelectRestaurantViewModel = viewModel(
        factory = AppViewModelProvider.Factory(sellerRepository = sellerRepository)
    )
    val sellers by viewModel.sellers.collectAsState()


    Column() {
        UniCanteenTopBar()
        SearchAndCartBar(onSearch = { query ->
            viewModel.searchSellersByName(query)
        },
            onCartClick = {

            }
        )
        Column(modifier = Modifier.weight(1f)) {
            RestaurantList(
                sellers = sellers,
                navController = navController
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
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(sellers) { seller ->
            RestaurantCard(seller = seller,
                onClick = {
                    navController.navigate("${SelectFoodDestination.route}/${seller.sellerId}")
                }
            )
        }
    }
}
@Composable
fun SearchAndCartBar(onSearch: (String) -> Unit, onCartClick: () -> Unit) {
    var query by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = {
                query = it
                onSearch(query)  // Ensure the search function is called with updated query
            },
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
        IconButton(onClick = {
            //handle cart


        }) {
            Icon(Icons.Default.ShoppingCart, contentDescription = "Bag", modifier = Modifier.size(36.dp)) // Larger bag icon
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewRestaurantList() {



}