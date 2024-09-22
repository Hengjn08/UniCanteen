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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unicanteen.R
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.data.Datasource
import com.example.unicanteen.model.Food
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.UniCanteenTheme

object FoodDetailsDestination : NavigationDestination {
    override val route = "food_details"
    override val title = "Food Details"
    const val foodIdArg = "foodId"
    val routeWithArgs = "$route/{$foodIdArg}"
}


@Composable
fun FoodDetailsScreen(
    food: Food?,
    navigateBack: () -> Unit,
    //showDialog: Boolean,
    //navController: NavController,
    onEditClick: () -> Unit,
    //onRemoveClick: () -> Unit,
    //onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            UniCanteenTopBar(
                title = "UniCanteen\nNoodles"
                //canNavigateBack = true,
                //navigateUp = navigateBack
            )
        }
    ) {innerpadding ->
        foodDetailsBody(
            food = food,
            onDelete = {},
            onEditClick = onEditClick,
            navigateBack = navigateBack,
            modifier = Modifier.padding(innerpadding)
        )
    }

//        if (showDialog) {
//            showDeleteConfirmationDialog(
//                onConfirm = {
//                    foodToRemove?.let { food ->
//                        Datasource.foods.remove(food)
//                        navController.navigateUp()
//                    }
//                    showDialog = false // Dismiss dialog
//                },
//                onDismiss = {
//                    showDialog = false // Dismiss dialog
//                }
//            )
//        }
}

@Composable
fun foodDetailsBody(
    food: Food?,
    onEditClick: () -> Unit,
    onDelete: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

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
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
        ) {
            Text("Edit")
        }
        OutlinedButton(
            onClick = { showDialog = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete")
        }
        if (showDialog) {
            DeleteConfirmationDialog(
                onConfirm = {
                    onDelete()
//                        foodToRemove?.let { food ->
//                            Datasource.foods.remove(food)
//                            navController.navigateUp()
//                        }
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
    food: Food?,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            //.fillMaxSize()
            .padding(16.dp)
    ) {
        if (food != null) {
            Image(
                painter = painterResource(food.foodImage.toInt()),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            FoodDetailsRow(
                label = "Name",
                foodDetails = food.foodName,
                modifier = Modifier.padding(16.dp)
            )

            FoodDetailsRow(
                label = "Description",
                foodDetails = food.foodDesc,
                modifier = Modifier
                    .padding(16.dp)
            )

            FoodDetailsRow(
                label = "Price",
                foodDetails = stringResource(R.string.rm, food.price),
                modifier = Modifier
                    .padding(16.dp)
            )
//            Text(
//                text = food.foodName,
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.padding(vertical = 16.dp)
//            )
//
//            Text(
//                text = food.foodDesc,
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            Text(
//                text = stringResource(R.string.rm, food.price),
//                style = MaterialTheme.typography.headlineSmall,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
        }
    }
}

@Composable
fun FoodDetailsRow(
    label: String,          //ltr maybe need to change to Int for stringRes
    foodDetails: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
    ){
        Text(text = label)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = foodDetails,
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Composable
fun DeleteConfirmationDialog(
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
        FoodDetailsScreen(
            food = food,
            navigateBack = { /*TODO*/ }) {

        }
    }
}