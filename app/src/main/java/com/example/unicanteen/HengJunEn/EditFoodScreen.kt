package com.example.unicanteen.HengJunEn

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.unicanteen.ChiaLiHock.AddOnViewModel
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.database.AddOn
import com.example.unicanteen.database.AddOnRepository
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListDao
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.OrderListDao
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppShapes
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme
import kotlinx.coroutines.launch

object EditFoodDestination : NavigationDestination {
    override val route = "edit_food"
    override val title = "Edit Food"
    const val foodIdArg = "foodId"
    val routeWithArgs = "$route/{$foodIdArg}"
}

@Composable
fun EditFoodScreen(
    application: Application,
    foodId: Int,
    foodListRepository: FoodListRepository,
    addOnRepository: AddOnRepository,
    navController: NavController
) {
    val editFoodViewModel: EditFoodViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application, foodListRepository = foodListRepository)
    )
    val addOnViewModel: AddOnViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application, addOnRepository = addOnRepository)
    )

    // Collect the state from ViewModel
    val foodDetailsWithAddOns = editFoodViewModel.foodDetailsWithAddOns.collectAsState().value

    // Fetch food details when the composable is loaded
    LaunchedEffect(foodId) {
        editFoodViewModel.fetchFoodDetails(foodId)
    }

    Scaffold(
        topBar = {
            UniCanteenTopBar()
        }
    ) { innerPadding ->
        if (foodDetailsWithAddOns.isNotEmpty()) {
            EditFoodBody(
                foodId = foodId,
                foodDetailsWithAddOns = foodDetailsWithAddOns.first(),
                modifier = Modifier.padding(innerPadding),
                navigateBack = { navController.navigateUp() },
                editFoodViewModel = editFoodViewModel,
                onSaveButtonClicked = { updatedAddOns ->

                    // Update add-ons
                    addOnViewModel.updateAddOnsForFood(updatedAddOns, foodId)

                    // Navigate back after saving
                    navController.navigate(SellerHomeDestination.route)
                },
                onCancelButtonClicked = { navController.navigate(SellerHomeDestination.route) }
            )
        } else {
            // Show a loading message or placeholder
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading food details...")
            }
        }
    }
}


@Composable
fun EditFoodBody(
    modifier: Modifier = Modifier,
    foodId: Int,
    foodDetailsWithAddOns: FoodListDao.FoodDetailsWithAddOns,
    navigateBack: () -> Unit,
    onSaveButtonClicked: (List<AddOn>) -> Unit,
    onCancelButtonClicked: () -> Unit,
    editFoodViewModel: EditFoodViewModel
) {
    // Prepopulate state with current food details
    var foodName by remember { mutableStateOf(foodDetailsWithAddOns.foodName) }
    var foodDes by remember { mutableStateOf(foodDetailsWithAddOns.foodDescription) }
    var foodPrice by remember { mutableStateOf(foodDetailsWithAddOns.price.toString()) }
    var selectedType by remember { mutableStateOf(foodDetailsWithAddOns.type) }
    val selectedAddOns = remember {
        mutableStateListOf<String>().apply {
            addAll(foodDetailsWithAddOns.addOnDescription?.split(", ")?.map { it.trim() } ?: emptyList())
        }
    }

    // For image selection
    var imageUri by remember { mutableStateOf(foodDetailsWithAddOns.imageUrl) }

    // List of available add-on options (same as in AddFoodScreen)
    val availableAddOnOptions = getAddOnOptions()

    // Handle description word limit validation (Optional)
    val descriptionWordLimit = 10
    val wordCount = foodDes.split(" ").size
    val isDescriptionValid = wordCount <= descriptionWordLimit

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        NavigateBackIconWithTitle(
            title = "Edit Food",
            navigateBack = navigateBack,
            modifier = Modifier.padding(top = 16.dp)
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Display existing image with AsyncImage
            AsyncImage(
                model = imageUri,
                contentDescription = "Food Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        // Handle image selection when clicked
                    }
                    .clip(AppShapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Editable Text Fields pre-populated with existing values
            EditTextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = "Name",
                placeholder = "Food Name",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                isError = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            EditTextField(
                value = foodDes,
                onValueChange = { foodDes = it },
                label = "Description",
                placeholder = "Food Description",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = false,
                modifier = Modifier.fillMaxWidth(),
                isError = !isDescriptionValid
            )

            Text(
                text = "Words: $wordCount/$descriptionWordLimit",
                color = if (isDescriptionValid) Color.Gray else Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            EditTextField(
                value = foodPrice,
                onValueChange = { foodPrice = it },
                label = "Price",
                placeholder = "RM",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                isError = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            FoodTypeDropdown(
                selectedType = selectedType,
                onTypeSelected = { selectedType = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add-On Options with Checkboxes
            Text(
                text = "Add-Ons:",
                color = Color.Blue
            )
            availableAddOnOptions.forEach { addOnOption ->
                val isChecked = selectedAddOns.contains(addOnOption.option)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            if (checked) {
                                selectedAddOns.add(addOnOption.option)
                            } else {
                                selectedAddOns.remove(addOnOption.option)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${addOnOption.option} (RM ${addOnOption.price})")
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedButton(
                onClick = onCancelButtonClicked,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Cancel")
            }
            Button(
                onClick = {
                    // Prepare the updated food item
//                    val updatedFood = FoodList(
//                        foodId = foodDetailsWithAddOns.foodId, // Pass foodId to maintain the correct ID
//                        foodName = foodName,
//                        description = foodDes,
//                        price = foodPrice.toDouble(),
//                        imageUrl = imageUri, // Use updated or existing URL
//                        type = selectedType,
//                        createDate =
//                    )
                    // Update food item
                        editFoodViewModel.updateFoodDetails(
                            foodId = foodId,
                            foodName = foodName,
                            description = foodDes,
                            price = foodPrice.toDouble(),
                            type = selectedType,
                            imageUrl = imageUri
                        )


                    // Prepare the list of updated add-ons
                    val updatedAddOns = selectedAddOns.map { addOnDesc ->
                        AddOn(
                            foodId = foodId, // Ensure foodId is passed correctly
                            description = addOnDesc,
                            price = 0.0 // Set the appropriate price or modify this as needed
                        )
                    }

                    // Call the save function with the updated food item and add-ons
                    onSaveButtonClicked(updatedAddOns)
                },
                enabled = selectedType.isNotEmpty() && foodName.isNotEmpty() && foodDes.isNotEmpty() && foodPrice.isNotEmpty() && isDescriptionValid,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Save")
            }
        }
    }
}



//fun hasChanges(
//    foodName: String,
//    foodDes: String,
//    foodPrice: String,
//    selectedType: String,
//    foodDetailsWithAddOns: FoodListDao.FoodDetailsWithAddOns,
//    selectedAddOns: List<String>
//): Boolean {
//    val currentFood = foodDetailsWithAddOns
//    return foodName != currentFood.foodName ||
//            foodDes != currentFood.foodDescription ||
//            foodPrice.toDoubleOrNull() != currentFood.price ||
//            selectedType != currentFood.type ||
//            selectedAddOns != currentFood.addOnDescription?.split(", ")?.map { it.trim() }
//}


//fun selectImage(onImageSelected: (String?) -> Unit) {
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        onImageSelected(uri?.toString())
//    }
//
//    imagePickerLauncher.launch("image/*")
//}



@Preview(showBackground = true)
@Composable
fun EditFoodPreview() {
    UniCanteenTheme {
//        EditFoodScreen(
//            navigateBack = {}
//        )
    }
}