package com.example.unicanteen.HengJunEn

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.ChiaLiHock.FoodDetailViewModel
import com.example.unicanteen.R
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.data.Datasource
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.model.Food
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme
import kotlinx.coroutines.coroutineScope

object FoodDetailsDestination : NavigationDestination {
    override val route = "food_details"
    override val title = "Food Details"
    const val foodIdArg = "foodId"
    val routeWithArgs = "$route/{$foodIdArg}"
}


@Composable
fun FoodDetailsScreen(
    foodId: Int,
    foodListRepository: FoodListRepository,
    navigateBack: () -> Unit,
    onEditClick: () -> Unit,
) {
    val sellerFoodDetailsViewModel: SellerFoodDetailsViewModel = viewModel(
        factory = AppViewModelProvider.Factory(null, foodListRepository)
    )

    val food by sellerFoodDetailsViewModel.foodDetails.collectAsState()

    LaunchedEffect(foodId) {
        sellerFoodDetailsViewModel.loadFoodDetails(foodId)
    }

    Scaffold(
        topBar = {
            UniCanteenTopBar(
                title = "UniCanteen\nNoodles"
            )
        }
    ) {innerpadding ->
        foodDetailsBody(
            food = food,
            onDelete = {
                sellerFoodDetailsViewModel.deleteFood()
                navigateBack()
            },
            onEditClick = onEditClick,
            navigateBack = navigateBack,
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
    modifier: Modifier = Modifier
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        NavigateBackIconWithTitle(
            title = FoodDetailsDestination.title,
            navigateBack = navigateBack
        )
        FoodDetails(
            food = food,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )

        Button(
            onClick = onEditClick,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
        ) {
            Text("Edit")
        }
        OutlinedButton(
            onClick = { showDialog = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text("Delete")
        }
        if (showDialog) {
            DeleteConfirmationDialog(
                onConfirm = {
                    onDelete()
                    showDialog = false // Dismiss dialog
                },
                onCancel = {
                    showDialog = false // Dismiss dialog
                },
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
fun FoodDetails(
    food: FoodList?,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.purple_80)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            if (food != null) {
//                Image(
//                    painter = painterResource(food.foodImage.toInt()),
//                    contentDescription = null,
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                        .clip(RoundedCornerShape(8.dp))
//                )

                FoodDetailsBody(
                    label = "Name",
                    foodDetails = food.foodName,
                    modifier = Modifier.padding(16.dp)
                )

                FoodDetailsBody(
                    label = "Description",
                    foodDetails = food.description,
                    modifier = Modifier
                        .padding(16.dp)
                )

                FoodDetailsBody(
                    label = "Price",
                    foodDetails = stringResource(R.string.rm, food.price),
                    modifier = Modifier
                        .padding(16.dp)
                )
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
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Delete Confirmation") },
        text = { Text("Are you sure you want to delete this food item?") },
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