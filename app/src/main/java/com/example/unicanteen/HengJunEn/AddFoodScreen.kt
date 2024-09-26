package com.example.unicanteen.HengJunEn

import android.Manifest
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.text.font.FontWeight
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.UniCanteenTheme

object AddFoodDestination : NavigationDestination {
    override val route = "add_food"
    override val title = "Add Food"
    const val foodIdArg = "foodId"
    val routeWithArgs = "$route/{$foodIdArg}"
}

@Composable
fun AddFoodScreen(
    food: Food? = null,
    onCancelButtonClicked: () -> Unit = {},
    onSaveButtonClicked: () -> Unit = {},
    //navController: NavController,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
){
    var foodName by remember { mutableStateOf(food?.foodName?:"")}
    var foodDes by remember { mutableStateOf(food?.foodDesc?:"") }
    var foodPrice by remember { mutableStateOf(food?.price?.toString()?:"") }
    var selectedType by remember { mutableStateOf(food?.type?:"") }
    var imageUri by remember { mutableStateOf<Uri?>(null)}

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UniCanteenTopBar(
                //canNavigateBack = true,
                //navigateUp = navigateBack,
            )
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
            onFoodDescChange = { foodDes = it },
            onFoodPriceChange = { foodPrice = it },
            onTypeSelected = { selectedType = it },
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
            onCancelButtonClicked = onCancelButtonClicked,
            onSaveButtonClicked = {
                val updatedFood = food?.copy(
                    foodName = foodName,
                    foodDesc = foodDes,
                    price = foodPrice.toDouble(),
                    foodImage = imageUri?.toString() ?: food.foodImage,
                    type = selectedType
                ) ?: Food(
                    id = Datasource.foods.size + 1,
                    foodName = foodName,
                    foodDesc = foodDes,
                    price = foodPrice.toDouble(),
                    foodImage = imageUri?.toString() ?: "",
                    type = selectedType
                )
                if (food == null) {
                    Datasource.foods.add(updatedFood)
                }
                onSaveButtonClicked()
                Toast.makeText(context, "Food item saved successfully!", Toast.LENGTH_SHORT).show()
            },
            navigateBack = navigateBack
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
    navigateBack: () -> Unit
){
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
//                {
//                    when (PackageManager.PERMISSION_GRANTED) {
//                        ContextCompat.checkSelfPermission(
//                            context,
//                            Manifest.permission.READ_EXTERNAL_STORAGE
//                        ) -> {
//                            launchImagePicker(imagePickerLauncher)
//                        }
//
//                        else -> {
//                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//                        }
//                    }
//                }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            //FoodTypeRadioButton()
            val options = listOf(
                "Rice",
                "Mee"
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Type:"
                )
                options.forEach { item ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .selectable(
                                selected = selectedType == item,
                                onClick = { onTypeSelected(item) }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RadioButton(
                            selected = selectedType == item,
                            onClick = { onTypeSelected(item) },
                        )
                        Text(item)
                    }
                }
            }
        }
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(16.dp),
//            verticalAlignment = Alignment.Bottom,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//
//            OutlinedButton(
//                onClick = onCancelButtonClicked,
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(text = "Cancel")
//            }
//            Button(
//                onClick = {
//                    //Log.d("AddFoodScreen","imageUri: $imageUri")
//                    //if(imageUri != null) {
//                    //                    val newFood = Food(
//                    //                        foodName = foodName,
//                    //                        foodDesc = foodDes,
//                    //                        price = foodPrice.toDouble(),
//                    //                        available = false,
//                    //                        foodImage = R.drawable.curry_mee.toString(),//imageUri?.toString() ?: ""       //<-- got problem
//                    //                        type = selectedType
//                    //                    )
//                    val updatedFood = food?.copy(
//                        foodName = foodName,
//                        foodDesc = foodDes,
//                        price = foodPrice.toDouble(),
//                        foodImage = imageUri?.toString() ?: food.foodImage,
//                        type = selectedType
//                    ) ?: Food(
//                        id = Datasource.foods.size + 1,
//                        foodName = foodName,
//                        foodDesc = foodDes,
//                        price = foodPrice.toDouble(),
//                        foodImage = imageUri?.toString() ?: "",
//                        type = selectedType
//                    )
//                    if (food == null) {
//                        Datasource.foods.add(updatedFood)
//                    }
//                    //Datasource.foods.add(newFood)
//                    onSaveButtonClicked()
//                    Toast.makeText(context, "Food item saved successfully!", Toast.LENGTH_SHORT)
//                        .show()
//                    //}else {
//                    //Toast.makeText(context, "Please select an image.", Toast.LENGTH_SHORT).show()
//                    //}
//                },
//                enabled = selectedType.isNotEmpty() && foodName.isNotEmpty() && foodDes.isNotEmpty() && foodPrice.isNotEmpty(),
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(text = if (food == null) "Save" else "Update")
//            }
//        }
    }
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

@Composable
fun EditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text(label)},
        singleLine = true,
        placeholder = {Text(placeholder)},
        keyboardOptions = keyboardOptions,
        //isError = value.isNullOrEmpty(),                          <--- need to do back
        modifier = modifier
    )
}

//@Composable
//fun FoodTypeRadioButton(
//    modifier: Modifier = Modifier
//){
//    val options = listOf(
//        "Rice",
//        "Mee"
//    )
//
//    var selected by remember { mutableStateOf("") }
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth()
//    ){
//        Text(
//            text = "Type:"
//        )
//        options.forEach {item ->
//            Row(
//                modifier = Modifier
//                    .weight(1f)
//                    .selectable(
//                        selected = selected == item,
//                        onClick = {
//                            selected = item
//                        }
//                    ),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ){
//                RadioButton(
//                    selected = selected == item,
//                    onClick = { selected = item },
//                )
//                Text(item)
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun AddFoodPreview() {
    UniCanteenTheme {
        //val food = Datasource.foods.get(0)
        AddFoodScreen(navigateBack = {})
    }
}