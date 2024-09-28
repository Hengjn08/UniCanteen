package com.example.unicanteen
import android.content.res.Configuration
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.database.Seller
import com.example.unicanteen.navigation.NavigationDestination
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.ChiaLiHock.CartViewModel
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.ui.theme.AppViewModelProvider
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyRow

object SelectRestaurantDestination : NavigationDestination {
    override val route = "restaurant_select?userId={userId}"
    override val title = "restaurant_select"
    fun routeWithArgs(userId: Int): String {
        return "restaurant_select?userId=$userId"
    }
}

@Composable
fun SelectRestaurantScreen(
    userId: Int,
    navController: NavController,
    currentDestination: NavDestination?,
    sellerRepository: SellerRepository,
    orderRepository: OrderRepository,
    orderListRepository: OrderListRepository
) {
    val viewModel: SelectRestaurantViewModel = viewModel(
        factory = AppViewModelProvider.Factory(sellerRepository = sellerRepository)
    )
    val cartViewModel: CartViewModel = viewModel(
        factory = AppViewModelProvider.Factory(orderRepository = orderRepository, orderListRepository = orderListRepository)
    )
    val sellers by viewModel.sellers.collectAsState()

    // Observe cart item count
    val cartItemCount by cartViewModel.cartItemCount.observeAsState(0)

    // Fetch cart item count when userId changes
    LaunchedEffect(userId) {
        cartViewModel.fetchCartItemsCount(userId)
    }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Column(modifier = Modifier.fillMaxSize()) {
        if (isPortrait) {
            UniCanteenTopBar()

        }
        SearchAndCartBar(
            onSearch = { query -> viewModel.searchSellersByName(query) },
            onCartClick = { navController.navigate("${CartDestination.route}") },
            cartItemCount
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = if (isPortrait) 10.dp else 0.dp) // Adjust padding based on orientation
        ) {
            // Use LazyColumn for portrait mode and LazyRow for landscape mode
            if (isPortrait) {
                RestaurantListColumn(sellers = sellers, navController = navController)
            } else {
                RestaurantListRow(sellers = sellers, navController = navController)
            }
        }
        BottomNavigationBar(navController = navController, currentDestination = currentDestination, isSeller = false)
    }
}

@Composable
fun RestaurantListColumn(sellers: List<Seller>, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp) // Padding for the list
    ) {
        items(sellers) { seller ->
            RestaurantCard(
                seller = seller,
                onClick = {
                    navController.navigate("${SelectFoodDestination.route}/${seller.sellerId}")
                }
            )
        }
    }
}

@Composable
fun RestaurantListRow(sellers: List<Seller>, navController: NavController) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Padding for the row
        contentPadding = PaddingValues(horizontal = 8.dp) // Horizontal padding for the list
    ) {
        items(sellers) { seller ->
            RestaurantCard(
                seller = seller,
                onClick = {
                    navController.navigate("${SelectFoodDestination.route}/${seller.sellerId}")
                }
            )
        }
    }
}



@Composable
fun RestaurantCard(seller: Seller, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        if (isPortrait) {
            // Use original design for portrait mode
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
                        modifier = Modifier.padding(bottom = 4.dp),
                        color = MaterialTheme.colorScheme.onSecondary
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
        } else {
            // Landscape design
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .defaultMinSize(minWidth = 200.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(seller.shopImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(110.dp) // Fixed size for the image
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.padding(8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = seller.shopName,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 4.dp),
                        color = MaterialTheme.colorScheme.onSecondary
                    )

                }
            }
        }
    }
}



@Composable
fun SearchAndCartBar(onSearch: (String) -> Unit, onCartClick: () -> Unit, cartItemCount: Int) {
    var query by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp), // Add end padding for better spacing
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = {
                query = it
                onSearch(query)
            },
            placeholder = {
                Text(
                    "Search by name",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            modifier = Modifier
                .weight(1f)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface),
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurface)
                    }
                } else {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurface)
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        )
        Box(modifier = Modifier) {
            IconButton(onClick = { onCartClick() }) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            if (cartItemCount > 0) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-12).dp, y = (4).dp),
                    content = {
                        Text(
                            text = cartItemCount.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
