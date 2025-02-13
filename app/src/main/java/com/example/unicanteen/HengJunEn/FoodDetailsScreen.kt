package com.example.unicanteen.HengJunEn

import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.unicanteen.R
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.data.Datasource
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme

object FoodDetailsDestination : NavigationDestination {
    override val route = "food_details"
    override val title = "Food Details"
    const val foodIdArg = "foodId"
    val routeWithArgs = "$route/{$foodIdArg}"
}

@Composable
fun FoodDetailsScreen(
    application: Application, // Pass application context
    foodId: Int,
    foodListRepository: FoodListRepository,
    navigateBack: () -> Unit,
    onEditClick: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val sellerFoodDetailsViewModel: SellerFoodDetailsViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,foodListRepository = foodListRepository)
    )

    val food by sellerFoodDetailsViewModel.foodDetails.collectAsState()

    LaunchedEffect(foodId) {
        sellerFoodDetailsViewModel.loadFoodDetails(foodId)
    }

    Scaffold(
        topBar = {
            UniCanteenTopBar(
                title = "UniCanteen"
            )
        }
    ) {innerpadding ->
        foodDetailsBody(
            food = food,
            onDelete = {
                // Call ViewModel's delete function and handle navigation on completion
                sellerFoodDetailsViewModel.deleteFoodAndImage {
                    navigateBack() // Navigate back on the main thread after deletion is complete
                }
            },
            onEditClick = onEditClick,
            navigateBack = navigateBack,
            viewModel = sellerFoodDetailsViewModel,
            isPortrait = isPortrait,
            modifier = Modifier.padding(innerpadding)
        )
    }
}

@Composable
fun foodDetailsBody(
    food: FoodList?,
    onEditClick: () -> Unit,
    onDelete: () -> Unit,
    navigateBack: () -> Unit,
    viewModel: SellerFoodDetailsViewModel,
    isPortrait: Boolean,
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            NavigateBackIconWithTitle(
                title = FoodDetailsDestination.title,
                navigateBack = navigateBack
            )
        }

        item {
            FoodDetails(
                food = food,
                viewModel = viewModel,
                isPortrait = isPortrait, // Pass the orientation state
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
            )
        }

        item {
            if (isPortrait) {
                // Portrait layout: Buttons stacked
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = onEditClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp) // Space between buttons
                    ) {
                        Text("Edit")
                    }

                    OutlinedButton(
                        onClick = { showDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Delete")
                    }
                }
            } else {
                // Landscape layout: Buttons in one row
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onEditClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Edit")
                    }

                    Spacer(modifier = Modifier.width(16.dp)) // Space between buttons

                    OutlinedButton(
                        onClick = { showDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
    if (showDialog) {
        ConfirmationDialog(
            onConfirm = {
                onDelete()
                showDialog = false // Dismiss dialog
            },
            onCancel = {
                showDialog = false // Dismiss dialog
            },
            title = "Delete Confirmation",
            message = "Are you sure you want to delete this food item?"
        )
    }
}

@Composable
fun FoodDetails(
    food: FoodList?,
    viewModel: SellerFoodDetailsViewModel,
    isPortrait: Boolean, // Add isPortrait to manage the layout
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.purple_80)
        )
    ) {
        if (isPortrait) {
            // Portrait layout: Image on top, details below
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                if (food != null) {
                    LoadFoodImage(
                        foodImagePath = food.imageUrl,
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, Color.Black)
                    )

                    FoodDetailsBody(label = "Name", foodDetails = food.foodName)
                    FoodDetailsBody(label = "Description", foodDetails = food.description)
                    FoodDetailsBody(
                        label = "Price",
                        foodDetails = stringResource(R.string.rm, food.price)
                    )
                }
            }
        } else {
            // Landscape layout: Image on left, details on right
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                if (food != null) {
                    LoadFoodImage(
                        foodImagePath = food.imageUrl,
                        viewModel = viewModel,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .weight(1f)
                            .border(2.dp, Color.Black)
                            .fillMaxHeight()
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                    ) {
                        FoodDetailsBody(label = "Name", foodDetails = food.foodName)
                        Spacer(modifier = Modifier.height(32.dp))
                        FoodDetailsBody(label = "Description", foodDetails = food.description)
                        Spacer(modifier = Modifier.height(32.dp))
                        FoodDetailsBody(
                            label = "Price",
                            foodDetails = stringResource(R.string.rm, food.price)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FoodDetailsBody(
    label: String,          //ltr maybe need to change to Int for stringRes
    foodDetails: String?,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ){
        Text(
            text = label,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        if (foodDetails != null) {
            Text(
                text = foodDetails,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun NavigateBackIconWithTitle(
    title: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(onClick = navigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text("No")
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun LoadFoodImage(
    foodImagePath: String,
    viewModel: SellerFoodDetailsViewModel,
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
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Food Image",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FoodDetailsScreenPreview(){
    UniCanteenTheme{
        val food = Datasource.foods.get(0)
//        FoodDetailsScreen(
//            //food = food,
//            //navigateBack = { /*TODO*/ }) {
//
//        }
//        )
    }
}