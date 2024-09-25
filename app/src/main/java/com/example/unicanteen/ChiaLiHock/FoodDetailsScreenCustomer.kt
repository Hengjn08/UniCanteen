package com.example.unicanteen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.ChiaLiHock.AddOnViewModel
import com.example.unicanteen.ChiaLiHock.FoodDetailViewModel
import com.example.unicanteen.database.AddOn
import com.example.unicanteen.database.AddOnRepository
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppShapes
import com.example.unicanteen.ui.theme.AppViewModelProvider

object FoodDetailCustomerDestination : NavigationDestination {
    override val route = "foodDetailCustomer"
    override val title = "Food Details"
    const val foodIdArg = "foodId"
    val routeWithArgs = "$route/{$foodIdArg}"
}

@Composable
fun FoodDetailsScreenCustomer(
    foodListRepository: FoodListRepository,
    addOnRepository: AddOnRepository,
    foodId: Int,
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    val foodDetailViewModel: FoodDetailViewModel = viewModel(
        factory = AppViewModelProvider.Factory(null, foodListRepository)
    )
    val addOnViewModel: AddOnViewModel = viewModel(
        factory = AppViewModelProvider.Factory(null, null, addOnRepository)
    )

    val foodDetails by foodDetailViewModel.foodDetails.collectAsState()
    val addOns by addOnViewModel.addOnList.observeAsState(emptyList())

    // Load food details and add-ons when the screen is first composed
    LaunchedEffect(foodId) {
        foodDetailViewModel.loadFoodDetails(foodId)
        addOnViewModel.fetchAddOns(foodId)
    }

    foodDetails?.let { food ->
        var totalAddOnPrice by remember { mutableStateOf(0.0) }

        Column(modifier = Modifier.fillMaxSize()) {
            FoodDetailsCard(food = food)
            Spacer(modifier = Modifier.height(20.dp))

            // Pass the fetched add-ons to AddOnSection
            totalAddOnPrice = AddOnSection(addOns = addOns, onPriceChange = { price ->
                totalAddOnPrice = price // Update the total add-on price
            })

            Spacer(modifier = Modifier.height(20.dp))
            RemarksSection()
            Spacer(modifier = Modifier.weight(1f))
            AddToCartButton(totalPrice = food.price + totalAddOnPrice)
        }
    } ?: run {
        // Show loading or error state
        CircularProgressIndicator()
    }
}

@Composable
fun FoodDetailsCard(food: FoodList) {
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.orange_500))
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(food.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text(
                    text = food.foodName,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                food.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(id = R.color.purple_grey_40)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "RM ${"%.2f".format(food.price)}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}

@Composable
fun AddOnSection(addOns: List<AddOn>, onPriceChange: (Double) -> Unit): Double {
    var totalAddOnPrice by remember { mutableStateOf(0.0) }
    val selectedAddOns = remember { mutableStateMapOf<String, Boolean>() }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp),
        ) {
            Text(
                text = "Add On (Optional)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )

            // Loop through the add-ons and create checkboxes
            addOns.forEach { addOn ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, start = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isChecked = selectedAddOns[addOn.description] ?: false
                    Checkbox(
                        modifier = Modifier.padding(start = 0.dp),
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            selectedAddOns[addOn.description] = checked
                            totalAddOnPrice += if (checked) {
                                addOn.price // Add the price if checked
                            } else {
                                -addOn.price // Subtract the price if unchecked
                            }
                            onPriceChange(totalAddOnPrice) // Notify the updated total price
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text(text = addOn.description)
                        Text(text = "+RM ${"%.2f".format(addOn.price)}", color = Color.Gray)
                    }
                }
            }
        }
    }

    return totalAddOnPrice // Return the total add-on price (not really needed if using onPriceChange)
}

@Composable
fun RemarksSection() {
    var remarks by remember { mutableStateOf("") }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp)
                .height(160.dp)
        ) {
            Text(
                text = "Remark (Optional)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = remarks,
                onValueChange = { remarks = it },
                placeholder = {
                    Text(
                        text = "Enter remark here...",
                        modifier = Modifier
                            .padding(0.dp)
                            .align(alignment = Alignment.Start)
                    )
                },
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.LightGray,
                )
            )
        }
    }
}

@Composable
fun AddToCartButton(totalPrice: Double) {
    Button(
        onClick = { /* Handle add to cart */ },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange_500)),
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        shape = AppShapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Add To Cart",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(43.dp))
            Text(
                text = "RM ${"%.2f".format(totalPrice)}",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}


