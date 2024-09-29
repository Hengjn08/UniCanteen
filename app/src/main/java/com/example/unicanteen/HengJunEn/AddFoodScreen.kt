package com.example.unicanteen.HengJunEn

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import androidx.compose.ui.unit.sp
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
    application: Application,
    sellerId: Int,
    navigateBack: () -> Unit,
    foodListRepository: FoodListRepository,
    addOnRepository: AddOnRepository,
    modifier: Modifier = Modifier
) {
    // Initialize the ViewModels
    val addFoodViewModel: AddFoodViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application, foodListRepository = foodListRepository)
    )
    val addOnViewModel: AddOnViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application, addOnRepository = addOnRepository)
    )

    // Define mutable states for various fields
    var foodName by remember { mutableStateOf("") }
    var foodDes by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var foodPrice by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var wordCount by remember { mutableStateOf(0) }
    var isDescriptionValid by remember { mutableStateOf(true) }
    val descriptionWordLimit = 10  // Word limit for the description

    // Get the current context and handle permissions
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

    // Manage permissions based on API level
    fun requestStoragePermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // API 33+: Request READ_MEDIA_IMAGES for Android 13 and above
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
            else -> {
                // API 32 and below: Request READ_EXTERNAL_STORAGE
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    // Define the scaffold and body layout
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { UniCanteenTopBar() }
    ) { innerPadding ->
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
                wordCount = getWordCount(it)
                isDescriptionValid = isValidDescription(it, descriptionWordLimit)
            },
            onFoodPriceChange = { foodPrice = it },
            onTypeSelected = { type -> selectedType = type },
            onImageClick = {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(context, getStoragePermission()) -> {
                        launchImagePicker(imagePickerLauncher)
                    }
                    else -> {
                        requestStoragePermission()
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
) {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val date = formatter.format(java.util.Date())

    val addOnOptions = getAddOnOptions()
    val selectedAddOns = remember { mutableStateListOf<AddOnOption>() }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        NavigateBackIconWithTitle(
            title = AddFoodDestination.title,
            navigateBack = navigateBack,
            modifier = Modifier.padding(top = 16.dp)
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Image Upload Box
            ImageUploadBox(
                imageUri = imageUri,
                onImageClick = onImageClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Food Name Input
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

            // Food Description Input
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
                modifier = Modifier.fillMaxWidth(),
                isError = !isDescriptionValid
            )

            Text(
                text = "Words: $wordCount/$descriptionWordLimit",
                color = if (isDescriptionValid) Color.Gray else Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Food Price Input
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

            // Food Type Dropdown
            FoodTypeDropdown(
                selectedType = selectedType,
                onTypeSelected = onTypeSelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add-ons Selection
            Text(
                text = "Add-Ons:",
                color = Color.Blue,
                fontSize = 16.sp
            )

            addOnOptions.forEach { addOnOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
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
                        createDate = date
                    )

                    addFoodViewModel.viewModelScope.launch {
                        val foodId = addFoodViewModel.addFoodItem(newFood)
                        selectedAddOns.forEach { addOn ->
                            val addOnRecord = AddOn(
                                foodId = foodId.toInt(),
                                description = addOn.option,
                                price = addOn.price
                            )
                            addOnViewModel.insertAddOns(addOnRecord)
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


// Helper function to launch image picker
fun launchImagePicker(launcher: ActivityResultLauncher<String>) {
    launcher.launch("image/*")
}

// Function to get the correct storage permission based on API level
fun getStoragePermission(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
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
        placeholder = {Text(placeholder, color = Color.LightGray)},
        keyboardOptions = keyboardOptions,
        isError = isError,
        modifier = modifier
    )
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