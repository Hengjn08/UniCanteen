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
import android.content.res.Configuration
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration

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
        factory = AppViewModelProvider.Factory(application = application, sellerRepository = sellerRepository)
    )

    var userRating by rememberSaveable { mutableStateOf(0f) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(sellerId) {
        viewModel.getSellerById(sellerId)
    }

    val sellerDetails by viewModel.singleSeller.collectAsState()

    // Detect device orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        // Landscape layout
        LandscapeSellerDetailScreen(
            sellerDetails = sellerDetails,
            userRating = userRating,
            onRatingChange = { userRating = it },
            onSubmitRating = {
                if (userRating > 0) {
                    showDialog = true
                }
            },
            showDialog = showDialog,
            onDialogConfirm = {
                viewModel.submitRating(sellerId, userRating.toDouble())
                navController.popBackStack()
                showDialog = false
            },
            onDialogDismiss = { showDialog = false }
        )
    } else {
        // Portrait layout (previous code for portrait mode)
        Column(
            modifier = Modifier
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
                            showDialog = true
                        }
                    }
                )
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            if (showDialog) {
                ConfirmRatingDialog(
                    rating = userRating,
                    onConfirm = {
                        viewModel.submitRating(sellerId, userRating.toDouble())
                        navController.popBackStack()
                        showDialog = false
                    },
                    onDismiss = { showDialog = false }
                )
            }
        }
    }
}

@Composable
fun LandscapeSellerDetailScreen(
    sellerDetails: Seller?,
    userRating: Float,
    onRatingChange: (Float) -> Unit,
    onSubmitRating: () -> Unit,
    showDialog: Boolean,
    onDialogConfirm: () -> Unit,
    onDialogDismiss: () -> Unit
) {
    sellerDetails?.let { seller ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Seller Details on the left side
            SellerDetailsSection(
                seller = seller,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(end=16.dp) // Padding between the sections
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Rating Section on the right side
                RatingSection(
                    userRating = userRating,
                    onRatingChange = onRatingChange,
                    modifier = Modifier.weight(1f)
                )

                SubmitRatingButton(
                    onSubmit = onSubmitRating
                )
            }
        }

        // Show confirmation dialog
        if (showDialog) {
            ConfirmRatingDialog(
                rating = userRating,
                onConfirm = onDialogConfirm,
                onDismiss = onDialogDismiss
            )
        }
    } ?: run {
        // Loading state if seller details are still being fetched
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
@Composable
fun ConfirmRatingDialog(rating: Float, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Confirm Rating")
        },
        text = {
            Text(text = "Are you sure you want to submit $rating stars?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun RatingSection(
    userRating: Float,
    onRatingChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = AppShapes.medium,
        modifier = modifier
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rate the Shop",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            RatingBar(
                rating = userRating,
                onRatingChange = onRatingChange,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(48.dp)
            )

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
        onClick = onSubmit,
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
    val starCount = 5

    Row(
        modifier = Modifier
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 1..starCount) {
            val scale = animateFloatAsState(if (rating >= i) 1.2f else 1f)

            IconButton(
                onClick = { onRatingChange(i.toFloat()) },
                modifier = Modifier.scale(scale.value)
            ) {
                Icon(
                    imageVector = if (rating >= i) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = if (rating >= i) "Rated $i star" else "Rate $i star",
                    tint = if (rating >= i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}


@Composable
fun SellerDetailsSection(seller: Seller, modifier: Modifier = Modifier) {
    Card(
        shape = AppShapes.medium,
        modifier = modifier
            .padding(16.dp),
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



