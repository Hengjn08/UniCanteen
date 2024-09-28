package com.example.unicanteen.ChiaLiHock
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.draw.scale
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
import com.example.unicanteen.database.Seller
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppShapes
import com.example.unicanteen.ui.theme.AppViewModelProvider
import kotlin.math.round
import android.app.Application

object SellerDetailsDestination : NavigationDestination {
    override val route = "sellerDetails"
    override val title = ""
    const val sellerIdArg = "sellerId" // This should refer to the seller's ID
    val routeWithArgs = "$route/{$sellerIdArg}" // Full route with arguments
}
@Composable
fun SellerDetailScreen(
    application: Application,
    sellerId: Int,
    sellerRepository: SellerRepository,
    navController: NavController
) {
    val viewModel: SelectRestaurantViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,sellerRepository = sellerRepository)
    )

    LaunchedEffect(sellerId) {
        viewModel.getSellerById(sellerId)
    }

    val sellerDetails by viewModel.singleSeller.collectAsState()
    var userRating by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        UniCanteenTopBar(title = "Seller Details")

        sellerDetails?.let { seller ->
            SellerDetailsSection(seller = seller)

            RatingSection(
                userRating = userRating,
                onRatingChange = { newRating -> userRating = newRating }
            )

            SubmitRatingButton(
                onSubmit = {
                    if (userRating > 0) {
                        viewModel.submitRating(sellerId, userRating.toDouble())
                        navController.popBackStack()
                    }
                }
            )
        } ?: run {
            // Loading state if seller details are still being fetched
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun SellerDetailsSection(seller: Seller) {
    Card(
        shape = AppShapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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

            Text(
                text = seller.shopName,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
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

            Text(
                text = "Created on: ${seller.createdDate ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSecondary
            )

            Text(
                text = "Rating: ${round(seller.shopRating)}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
fun RatingSection(
    userRating: Float,
    onRatingChange: (Float) -> Unit
) {
    Card(
        shape = AppShapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp).padding(bottom = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Section Title
            Text(
                text = "Rate the Shop",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Stars Rating Bar (custom UI component or material rating bar)
            RatingBar(
                rating = userRating,
                onRatingChange = onRatingChange,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(48.dp) // Ensure enough space for larger rating bars
            )

            // Show selected rating value
            if (userRating > 0) {
                Text(
                    text = "You rated: $userRating stars",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                Text(
                    text = "Please rate the shop",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SubmitRatingButton(onSubmit: () -> Unit) {
    Button(
        onClick = { onSubmit() },
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Text(
            text = "Submit Rating",
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
}





@Composable
fun RatingBar(rating: Float, onRatingChange: (Float) -> Unit, modifier: Modifier = Modifier) {
    // Define the number of stars in the rating bar
    val starCount = 5

    Row(
        modifier = Modifier
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp) // Space between the stars
    ) {
        // Loop through the stars from 1 to starCount
        for (i in 1..starCount) {
            // Apply a subtle scale animation when a star is clicked
            val scale = animateFloatAsState(if (rating >= i) 1.2f else 1f)

            IconButton(
                onClick = { onRatingChange(i.toFloat()) },
                modifier = Modifier.scale(scale.value)  // Apply scaling effect to stars
            ) {
                // Show filled or outlined star based on the rating
                Icon(
                    imageVector = if (rating >= i) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = if (rating >= i) "Rated $i star" else "Rate $i star",
                    tint = if (rating >= i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(32.dp) // Increase icon size for better visual feedback
                )
            }
        }
    }
}
