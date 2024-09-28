package com.example.unicanteen

import android.app.Application
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.ChiaLiHock.AddOnViewModel
import com.example.unicanteen.ChiaLiHock.FoodDetailViewModel
import com.example.unicanteen.ChiaLiHock.OrderListViewModel
import com.example.unicanteen.database.AddOn
import com.example.unicanteen.database.AddOnRepository
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import java.text.SimpleDateFormat

object FoodDetailCustomerDestination : NavigationDestination {
    override val route = "foodDetailCustomer"
    override val title = "Food Details"
    const val foodIdArg = "foodId"
    val routeWithArgs = "$route/{$foodIdArg}"
}

@Composable
fun FoodDetailsScreenCustomer(
    application: Application, // Pass application context
    foodListRepository: FoodListRepository,
    addOnRepository: AddOnRepository,
    orderListRepository: OrderListRepository,
    orderRepository: OrderRepository,
    foodId: Int,
    userId: Int,
    navController: NavController
) {
    val foodDetailViewModel: FoodDetailViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,foodListRepository = foodListRepository)
    )
    val addOnViewModel: AddOnViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,addOnRepository = addOnRepository)
    )
    val orderListViewModel: OrderListViewModel = viewModel(
        factory = AppViewModelProvider.Factory(
            application = application,
            orderListRepository = orderListRepository,
            orderRepository = orderRepository
        )
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
        var remarks = ""
        Scaffold{innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
                FoodDetailsCard(food = food)
                Spacer(modifier = Modifier.height(20.dp))

                // Pass the fetched add-ons to AddOnSection
                totalAddOnPrice = AddOnSection(addOns = addOns, onPriceChange = { price ->
                    totalAddOnPrice = price // Update the total add-on price
                })

                Spacer(modifier = Modifier.height(20.dp))
                remarks=RemarksSection()
                Spacer(modifier = Modifier.weight(1f))
                AddToCartButton(totalPrice = food.price + totalAddOnPrice,remarks = remarks,
                    food = food, orderListViewModel = orderListViewModel, userId, navController)
            }

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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
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
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()) {
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
                        color = MaterialTheme.colorScheme.secondary
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
fun RemarksSection() :String{
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
    return remarks
}

@Composable
fun AddToCartButton(
    totalPrice: Double,
    remarks: String,
    food: FoodList,
    orderListViewModel: OrderListViewModel,
    userId: Int,
    navController: NavController
) {
    // State to manage whether the dialog is visible
    var showDialog by remember { mutableStateOf(false) }

    // Button to add the item to the cart
    Button(
        onClick = {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = formatter.format(java.util.Date())
            orderListViewModel.addOrderListItem(
                sellerId = food.sellerId,
                foodId = food.foodId,
                userId = userId,
                qty = 1,
                totalPrice = totalPrice,
                remark = remarks,
                createDate = date,
                price = totalPrice
            )
            // Show the confirmation dialog after adding to cart
            showDialog = true
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        shape = RoundedCornerShape(24.dp)
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

    // Show confirmation dialog if the item is added to the cart
    if (showDialog) {
        ConfirmationDialog(
            onDismiss = { showDialog = false },
            onVisitCart = {
                // Navigate to the cart screen here
                showDialog = false
                navController.navigate("${CartDestination.route}")
                // Add navigation logic to the cart screen if necessary
            },
            onContinueShopping = {
                showDialog = false
                navController.navigate("${SelectRestaurantDestination.route}")
                // Close the dialog and let the user continue shopping
            }
        )
    }
}
@Composable
fun ConfirmationDialog(onDismiss: () -> Unit, onVisitCart: () -> Unit, onContinueShopping: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Item Added to Cart")
        },
        text = {
            Text("Would you like to visit your cart or continue shopping?")
        },
        confirmButton = {
            TextButton(
                onClick = { onVisitCart() }
            ) {
                Text("Visit Cart")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onContinueShopping() }
            ) {
                Text("Continue Shopping")
            }
        }
    )
}

