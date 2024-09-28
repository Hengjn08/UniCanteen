package com.example.unicanteen.HengJunEn

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.unicanteen.R
import com.example.unicanteen.data.Datasource
import com.example.unicanteen.model.Food
import coil.compose.AsyncImage
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.ChiaLiHock.AddOnViewModel
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.database.AddOn
import com.example.unicanteen.database.AddOnRepository
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme
import kotlinx.coroutines.launch
import java.lang.Error
import java.text.SimpleDateFormat

object AddFoodDestination : NavigationDestination {
    override val route = "add_food"
    override val title = "Add Food"
    const val foodIdArg = "foodId"
    val routeWithArgs = "$route/{$foodIdArg}"
}

@Composable
fun AddFoodScreen(
    application: Application, // Pass application context
    sellerId: Int,
    navigateBack: () -> Unit,
    foodListRepository: FoodListRepository,
    addOnRepository: AddOnRepository,
    modifier: Modifier = Modifier
){

    val addFoodViewModel: AddFoodViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,foodListRepository = foodListRepository)
    )
    val addOnViewModel: AddOnViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,addOnRepository = addOnRepository)
    )
    var foodName by remember { mutableStateOf("")}
    var foodDes by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf ("")}
    var foodPrice by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null)}
    var wordCount by remember { mutableStateOf(0) }
    var isDescriptionValid by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launchImagePicker(imagePickerLauncher)
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Snackbar Host State
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val descriptionWordLimit = 10  // Word limit for the description

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UniCanteenTopBar()
        }
    ){ innerPadding ->
        AddFoodBody(
            modifier = modifier.padding(innerPadding),
            foodName = foodName,
            foodDes = foodDes,
            foodPrice = foodPrice,
            selectedType = selectedType,
            imageUri = imageUri,
            onFoodNameChange = { foodName = it },
            onFoodDescChange = {
                foodDes = it

                // Calculate the word count and update the state
                wordCount = getWordCount(it)
                isDescriptionValid = isValidDescription(it, descriptionWordLimit)

                // Show a snackbar if the word limit is exceeded
                if (!isDescriptionValid) {
                    scope.launch {
                        snackbarHostState.showSnackbar("You have reached the $descriptionWordLimit limit!")
                    }
                }
           },
            onFoodPriceChange = { foodPrice = it },
            onTypeSelected = { type -> selectedType = type  },
            onImageClick = {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) -> {
                        launchImagePicker(imagePickerLauncher)
                    }
                    else -> {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            },
            onCancelButtonClicked = { navigateBack() },
            onSaveButtonClicked = {
                Toast.makeText(context, "Food item saved successfully!", Toast.LENGTH_SHORT).show()
                navigateBack()
            },
            navigateBack = navigateBack,
            sellerId = sellerId,
            isDescriptionValid = isDescriptionValid,
            wordCount = wordCount,
            descriptionWordLimit = descriptionWordLimit,
            addFoodViewModel = addFoodViewModel,
            addOnViewModel = addOnViewModel
        )
    }
}

@Composable
fun AddFoodBody(
    modifier: Modifier = Modifier,
    foodName: String,
    foodDes: String,
    foodPrice: String,
    selectedType: String,
    imageUri: Uri?,
    onFoodNameChange: (String) -> Unit,
    onFoodDescChange: (String) -> Unit,
    onFoodPriceChange: (String) -> Unit,
    onTypeSelected: (String) -> Unit,
    onImageClick: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit,
    navigateBack: () -> Unit,
    sellerId: Int,
    isDescriptionValid: Boolean,
    wordCount: Int,
    descriptionWordLimit: Int,
    addFoodViewModel: AddFoodViewModel,
    addOnViewModel: AddOnViewModel
){
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val date = formatter.format(java.util.Date())

    val addOnOptions = getAddOnOptions()
    val selectedAddOns = remember { mutableStateListOf<AddOnOption>() }

    Column (
        modifier = modifier.verticalScroll(rememberScrollState()),
    ){
        NavigateBackIconWithTitle(
            title = AddFoodDestination.title ,
            navigateBack = navigateBack,
            modifier = Modifier.padding(top = 16.dp)
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            ImageUploadBox(
                imageUri = imageUri,
                onImageClick = onImageClick,
            )

            Spacer(modifier = Modifier.height(16.dp))
            EditTextField(
                value = foodName,
                onValueChange = onFoodNameChange,
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
                onValueChange = onFoodDescChange,
                label = "Description",
                placeholder = "Food Description",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth(),
                isError = !isDescriptionValid
            )
            Text(
                text = "Words: $wordCount/$descriptionWordLimit",
                color = if (isDescriptionValid) Color.Gray else Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            EditTextField(
                value = foodPrice,
                onValueChange = onFoodPriceChange,
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
                onTypeSelected = onTypeSelected
            )

            Spacer(modifier = Modifier.height(8.dp))
            // Add-On Options with Checkboxes
            Text(text = "Add-Ons:")

            addOnOptions.forEach { addOnOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = selectedAddOns.contains(addOnOption),
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                selectedAddOns.add(addOnOption)
                            } else {
                                selectedAddOns.remove(addOnOption)
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
                    val newFood = FoodList(
                        sellerId = sellerId,
                        foodName = foodName,
                        description = foodDes,
                        price = foodPrice.toDouble(),
                        imageUrl = imageUri?.toString() ?: "",
                        type = selectedType,
                        createDate = date,
                    )
                    // Use a coroutine to handle the asynchronous call and get the foodId after saving the new item
                    addFoodViewModel.viewModelScope.launch {
                        // Insert the new food item and get the generated foodId
                        val foodId = addFoodViewModel.addFoodItem(newFood)

                        // Now, save each selected add-on for this food item
                        selectedAddOns.forEach { addOn ->
                            val addOnRecord = AddOn(
                                foodId = foodId.toInt(),
                                description = addOn.option,
                                price = addOn.price
                            )
                            addOnViewModel.insertAddOns(addOnRecord)  // Insert add-on record
                        }
                        onSaveButtonClicked()
                    }
                },
                enabled = selectedType.isNotEmpty() && foodName.isNotEmpty() && foodDes.isNotEmpty() && foodPrice.isNotEmpty() && isDescriptionValid,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Save")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodTypeDropdown(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
) {
    val foodTypes = listOf(
        "Rice",
        "Mee",
        "Main Course",
        "Appetizer",
        "Salad",
        "Desset",
        "Bakery"
    )
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = it},
    ){
        OutlinedTextField(
            value = selectedType,
            onValueChange = {},
            readOnly = true,  // Make the field read-only
            label = { Text("Select Food Type") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            foodTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        onTypeSelected(type)  // Update the selected type
                        expanded = false  // Close the dropdown after selection
                    },
                )
            }
        }
    }
}


data class AddOnOption(val option: String, val price: Double)

fun getAddOnOptions(): List<AddOnOption> {
    return listOf(
        AddOnOption("Big portion", 1.00),
        AddOnOption("Egg", 1.00),
        AddOnOption("Meat", 2.00)
    )
}

@Composable
fun EditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    singleLine: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text(label)},
        singleLine = singleLine,
        placeholder = {Text(placeholder)},
        keyboardOptions = keyboardOptions,
        isError = isError,
        modifier = modifier
    )
}

fun launchImagePicker(launcher: ActivityResultLauncher<String>) {
    launcher.launch("image/*")
}

@Composable
fun ImageUploadBox(
    imageUri: Uri?,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .clickable(onClick = onImageClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Selected food image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_upload_24),
                    contentDescription = "Upload icon",
                    modifier = Modifier.size(50.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Upload picture",
                    color = Color.Gray
                )
            }
        }
    }
}

// Validation function to check the word count of the description
fun isValidDescription(description: String, wordLimit: Int): Boolean {
    val wordCount = description.trim().split("\\s+".toRegex()).size
    return wordCount <= wordLimit
}

// Function to count words in the description
fun getWordCount(description: String): Int {
    return description.trim().split("\\s+".toRegex()).size
}

@Preview(showBackground = true)
@Composable
fun AddFoodPreview() {
//    UniCanteenTheme {
//        //val food = Datasource.foods.get(0)
//        AddFoodScreen(navigateBack = {})
//    }
}