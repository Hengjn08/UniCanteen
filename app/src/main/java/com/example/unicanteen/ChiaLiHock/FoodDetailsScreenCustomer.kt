package com.example.unicanteen

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.ChiaLiHock.AddOnViewModel
import com.example.unicanteen.ChiaLiHock.FoodDetailViewModel
import com.example.unicanteen.ChiaLiHock.OrderListViewModel
import com.example.unicanteen.HengJunEn.SellerFoodDetailsViewModel
import com.example.unicanteen.database.AddOn
import com.example.unicanteen.database.AddOnRepository
import com.example.unicanteen.database.FoodList
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppShapes
import com.example.unicanteen.ui.theme.AppViewModelProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        factory = AppViewModelProvider.Factory(application = application, foodListRepository = foodListRepository)
    )
    val addOnViewModel: AddOnViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application, addOnRepository = addOnRepository)
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

        val configuration = LocalConfiguration.current
        val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        if (isPortrait) {
            // Portrait layout
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    FoodDetailsCard(food = food,viewModel = orderListViewModel)
                    totalAddOnPrice = AddOnSection(addOns = addOns, onPriceChange = { price ->
                        totalAddOnPrice = price
                    })
                    remarks = RemarksSection()
                }

                AddToCartButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    totalPrice = food.price + totalAddOnPrice,
                    remarks = remarks,
                    food = food,
                    orderListViewModel = orderListViewModel,
                    userId = userId,
                    navController = navController
                )
            }
        } else {
            // Landscape layout
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Left side (food details and add-ons)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    FoodDetailsCard(food = food,viewModel = orderListViewModel,isPortrait)
                    totalAddOnPrice = AddOnSection(addOns = addOns, onPriceChange = { price ->
                        totalAddOnPrice = price
                    })
                }

                // Right side (remarks and add-to-cart button)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    remarks = RemarksSection()
                    Row{
                        AddToCartButton(
                            modifier = Modifier.weight(1f).wrapContentSize(align = Alignment.BottomEnd),
                            totalPrice = food.price + totalAddOnPrice,
                            remarks = remarks,
                            food = food,
                            orderListViewModel = orderListViewModel,
                            userId = userId,
                            navController = navController
                        )
                        Spacer(
                            modifier = Modifier.width(16.dp)
                        )
                        Button(onClick = {navController.popBackStack()},colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                            modifier = Modifier
                                .width(150.dp)
                                .height(80.dp),
                            shape = AppShapes.medium
                            ,
                        ){
                            Text(text = "Back"
                            )
                        }
                    }

                }
            }
        }
    } ?: run {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
}





@Composable
fun FoodDetailsCard(food: FoodList,viewModel: OrderListViewModel,isPortrait: Boolean = true) {
    Card(
        shape = if(isPortrait){
            RoundedCornerShape(0.dp)
        }else{
            RoundedCornerShape(16.dp)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            LoadFoodImage(
                foodImagePath = food.imageUrl,
                viewModel=viewModel,
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
fun RemarksSection(): String {
    var remarks by rememberSaveable { mutableStateOf("") } // Save remarks across rotation
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
                ),
            )
        }
    }
    return remarks
}


@Composable
fun AddToCartButton(
    modifier: Modifier = Modifier,
    totalPrice: Double,
    remarks: String,
    food: FoodList,
    orderListViewModel: OrderListViewModel,
    userId: Int,
    navController: NavController
) {
    var showDialog by rememberSaveable { mutableStateOf(false) } // Save dialog state across rotation
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    if (isPortrait){
        Button(
            onClick = {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
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
                showDialog = true // Show confirmation dialog
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            modifier = modifier
                .fillMaxWidth()
                .height(65.dp),
            shape = AppShapes.large
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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
                Spacer(modifier = Modifier.width(55.dp))
                Text(
                    text = "RM ${"%.2f".format(totalPrice)}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
    else{
        Button(
            onClick = {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
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
                showDialog = true // Show confirmation dialog
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            modifier = modifier
                .width(150.dp)
                .height(80.dp),
            shape = AppShapes.medium
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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
            }
        }
    }


    // Show confirmation dialog when order is added
    if (showDialog) {
        ConfirmationDialog(
            onDismiss = { showDialog = false },
            onVisitCart = {
                showDialog = false
                navController.navigate(CartDestination.route) // Navigate to cart
            },
            onContinueShopping = {
                showDialog = false
                navController.navigate(SelectRestaurantDestination.route) // Navigate to restaurant selection
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
@Composable
private fun LoadFoodImage(
    foodImagePath: String,
    viewModel: OrderListViewModel,
    modifier: Modifier = Modifier
) {
    var imageUrl by remember { mutableStateOf<String?>(null) }
    Log.d("FoodImagePath", "FoodImagePath: $foodImagePath")
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

