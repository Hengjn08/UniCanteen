package com.example.unicanteen.ChiaLiHock
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.SelectRestaurantViewModel
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppShapes
import com.example.unicanteen.ui.theme.AppViewModelProvider
import kotlin.math.round

object SellerDetailsDestination : NavigationDestination {
    override val route = "sellerDetails"
    override val title = ""
    const val sellerIdArg = "sellerId" // This should refer to the seller's ID
    val routeWithArgs = "$route/{$sellerIdArg}" // Full route with arguments
}
@Composable
fun SellerDetailScreen(
    sellerId: Int,
    sellerRepository: SellerRepository,
    navController: NavController
) {
    val viewModel: SelectRestaurantViewModel = viewModel(
        factory = AppViewModelProvider.Factory(sellerRepository = sellerRepository)
    )

    LaunchedEffect(sellerId) {
        viewModel.getSellerById(sellerId)
    }

    val sellerDetails by viewModel.singleSeller.collectAsState()
    var userRating by remember { mutableStateOf(0f) }

    // Use Box to contain the layout
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),

    ) {
        // Main content column
        Column(
            modifier = Modifier
                .fillMaxSize().navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween,

        ) {
            UniCanteenTopBar(title = "Seller Details")

            sellerDetails?.let { seller ->
                // Consistent Card Style
                Card(
                    shape = AppShapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Consistent Image Loading
                        seller.shopImage?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Shop Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Consistent Typography
                        Text(
                            text = seller.shopName,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondary

                        )
                        Text(
                            text = "Shop Type: ${seller.shopType}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                        Text(
                            text = "Description: ${seller.description ?: "No description available"}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                        // Additional details like created date, total orders, etc.
                        Text(
                            text = "Created on: ${seller.createdDate ?: "N/A"}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                        // Consistent rating display
                        Text(
                            text = "Rating: ${round(seller.shopRating)}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondary

                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        )
                        // Rating Section with consistent design
                        Text(
                            text = "Rate the Shop",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(bottom = 6.dp),
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                        // Consistent Rating Bar
                        RatingBar(
                            rating = userRating,
                            onRatingChange = { newRating -> userRating = newRating }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                    }
                }
            } ?: run {
                // Loading state
                Text(text = "Loading...", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            // Submit Button
            Button(
                onClick = {
                    if (userRating > 0) {
                        viewModel.submitRating(sellerId, userRating.toDouble())
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text(text = "Submit Rating",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f))
            }
        }

        // Bottom navigation bar placed at the bottom

    }
}




@Composable
fun RatingBar(rating: Float, onRatingChange: (Float) -> Unit) {
    Row(
        modifier = Modifier
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            IconButton(onClick = { onRatingChange(i.toFloat()) }) {
                Icon(
                    imageVector = if (rating >= i) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = null,
                    tint = if (rating >= i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),

                )
            }
        }
    }
}