package com.example.unicanteen

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.ChiaLiHock.CartItem
import com.example.unicanteen.ChiaLiHock.CartViewModel
import com.example.unicanteen.ChiaLiHock.OrderListViewModel
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.OrderList
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import com.example.unicanteen.navigation.NavigationDestination


import com.example.unicanteen.ui.theme.AppShapes
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme
import kotlinx.coroutines.delay

object CartDestination : NavigationDestination {
    override val route = "Cart"
    override val title = "Cart"
    const val orderIdArg = "orderId"
    val routeWithArgs = "$route/{$orderIdArg}"
}

@Composable
fun CartScreen(
    application: Application,
    userId: Int,
    orderRepository: OrderRepository,
    orderListRepository: OrderListRepository,
    navController: NavController
) {
    val cartViewModel: CartViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application, orderRepository = orderRepository, orderListRepository = orderListRepository)
    )

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    var totalPrice by remember { mutableStateOf(0.0) }
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList<CartItem>())
    var showEmptyCartDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch cart items based on userId when the screen is launched
    LaunchedEffect(userId) {
        cartViewModel.getCartItems(userId)
        // Introduce a delay after loading the cart items

        isLoading = false // Mark loading as complete after fetching
    }

    // Recalculate the total price whenever cartItems or quantity changes
    LaunchedEffect(cartItems) {
        totalPrice = cartItems.sumOf { it.price }

        // Check if the cart is empty only after loading is complete
        if (!isLoading && totalPrice == 0.0) {
            delay(500) // 0.5 second delay
            showEmptyCartDialog = true // Show dialog if cart is empty
        }
    }

    // Handle the empty cart dialog
    if (showEmptyCartDialog) {

        AlertDialog(
            onDismissRequest = { showEmptyCartDialog = false },
            title = { Text("Empty Cart") },
            text = { Text("Your cart is empty. Returning to the previous screen.") },
            confirmButton = {
                Button(onClick = {
                    navController.popBackStack() // Navigate back
                    showEmptyCartDialog = false
                }) {
                    Text("OK")
                }
            }
        )
    } else {
        // Main UI
        if (isPortrait) {
            // Portrait mode layout
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                UniCanteenTopBar(title = "Cart")
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (cartItems.isEmpty()) {
                        // Optionally, you can also show a message in the UI
                    } else {
                        CartList(
                            cartItems = cartItems.toMutableList(),
                            onCartItemsChanged = { updatedItems ->
                                totalPrice = updatedItems.sumOf { it.price }
                            },
                            cartViewModel = cartViewModel,
                            userId = userId
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CheckOutButton(
                    totalPrice = totalPrice,
                    orderId = cartItems.firstOrNull()?.orderId ?: 0,
                    cartViewModel = cartViewModel,
                    navController = navController,
                    userId = userId
                )
            }
        } else {
            // Landscape mode layout
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Cart items list
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                        .padding(end = 8.dp)
                ) {
                    if (cartItems.isEmpty()) {
                        // Optionally, you can also show a message in the UI
                    } else {
                        CartList(
                            cartItems = cartItems.toMutableList(),
                            onCartItemsChanged = { updatedItems ->
                                totalPrice = updatedItems.sumOf { it.price }
                            },
                            cartViewModel = cartViewModel,
                            userId = userId
                        )
                    }
                }

                // Total price and checkout section
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Total Price:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "RM ${"%.2f".format(totalPrice)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        CheckOutButton(
                            totalPrice = totalPrice,
                            orderId = cartItems.firstOrNull()?.orderId ?: 0,
                            cartViewModel = cartViewModel,
                            navController = navController,
                            userId = userId
                        )
                    }
                }
            }
        }
    }
}




@Composable
fun CartList(
    cartItems: List<CartItem>,
    onCartItemsChanged: (List<CartItem>) -> Unit,
    cartViewModel: CartViewModel,
    userId: Int,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(cartItems.size) { index ->
            var unitPrice = cartItems[index].price / cartItems[index].quantity

            CartCard(
                item = cartItems[index],
                onQuantityChange = { newQuantity ->
                    val updatedItem = cartItems[index].copy(quantity = newQuantity, price = unitPrice * newQuantity)
                    val updatedItems = cartItems.toMutableList().apply {
                        this[index] = updatedItem
                    }
                    onCartItemsChanged(updatedItems)
                    cartViewModel.updateOrderItem(updatedItem.orderListId, newQuantity, unitPrice,userId)
                },
                onDelete = {
                    val updatedItems = cartItems.toMutableList().apply {
                        removeAt(index)
                    }
                    cartViewModel.deleteOrderItem(cartItems[index].orderListId, userId = userId)
                    onCartItemsChanged(updatedItems)
                    if (updatedItems.isEmpty()) {
                        cartViewModel.deleteOrderByUserId(userId)
                    }

                },
                viewModel = cartViewModel
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}




@Composable
fun CartCard(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit,
    viewModel: CartViewModel
) {
    // Remove the local state for quantity
    val selectedQuantity = item.quantity
    var showDialog by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(130.dp).clickable { showDialog = true },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .align(alignment = Alignment.CenterHorizontally),
        ) {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Item Info
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = item.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = item.description.dropLast(
                            if (item.description.length > 20) {
                                25
                            }
                            else if( item.description.length > 15) {
                                20
                            }
                            else if( item.description.length > 10) {
                                15
                            }
                            else if( item.description.length > 5) {
                                10
                            }
                            else {
                                5
                            }
                        ).plus("..."),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "RM ${"%.2f".format(item.price)}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                Row(modifier = Modifier.weight(1.2f)){
                    LoadFoodImage(
                        foodImagePath = item.imageRes,
                        viewModel = viewModel,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(end = 8.dp)
                            .align(Alignment.CenterVertically)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    // Quantity Dropdown (use selectedQuantity from item.quantity)
                    EnhancedQuantityDropdown(
                        quantity = selectedQuantity, // Use item.quantity directly
                        onQuantityChange = { newQuantity ->
                            onQuantityChange(newQuantity) // Trigger parent callback
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    // Delete Button
                    var showDialog by remember { mutableStateOf(false) }

                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Confirm Deletion") },
                            text = { Text("Are you sure you want to remove this item from your cart?") },
                            confirmButton = {
                                Button(onClick = {
                                    onDelete()
                                    showDialog = false
                                }) {
                                    Text("Delete")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showDialog = false }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }

                    IconButton(
                        onClick = { showDialog = true },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete",modifier=Modifier.fillMaxSize(), tint = Color.Gray)
                    }
                }

            }
        }
    }
    // Show the floating window dialog when the user clicks on the card
    if (showDialog) {
        FoodDetailsDialog(
            item = item,
            onDismiss = { showDialog = false }, // Close dialog when dismissed
            cartViewModel = viewModel
            )
    }
}

@Composable
fun FoodDetailsDialog(
    item: CartItem,
    onDismiss: () -> Unit,
    cartViewModel: CartViewModel
) {
    // Get the current configuration to determine orientation
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .then(if (isPortrait) Modifier.height(500.dp) else Modifier.height(300.dp)) // Adjust height based on orientation
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Image of the food item
                LoadFoodImage(
                    foodImagePath = item.imageRes,
                    viewModel = cartViewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (isPortrait) 200.dp else 150.dp) // Adjust image height based on orientation
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Food item name and price
                Text(
                    text = item.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.type,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Text(
                    text = item.status,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Text(
                    text = "RM ${"%.2f".format(item.price)}",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Food description
                Text(
                    text = item.description,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Close button
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Close")
                }
            }
        }
    }
}


@Composable
fun EnhancedQuantityDropdown(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedQuantity by remember { mutableStateOf(quantity) }

    // This ensures the UI updates if the quantity changes from outside (e.g., when an item is deleted).
    LaunchedEffect(quantity) {
        selectedQuantity = quantity
    }

    val quantities = (1..10).toList()

    Box(
        modifier = modifier
            .wrapContentSize()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Add border for better UI
            .padding(5.dp)
            .height(70.dp)
            .width(60.dp)
            .clickable { expanded = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .background(MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedQuantity.toString(),
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Dropdown arrow")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            quantities.forEach { qty ->
                DropdownMenuItem(
                    onClick = {
                        selectedQuantity = qty
                        onQuantityChange(qty)
                        expanded = false
                    },
                    text = { Text(text = qty.toString(), fontSize = 16.sp) } // Use new API
                )
            }
        }
    }
}
fun checkAbility(totalPrice: Double):Boolean{
    return totalPrice > 0
}
@Composable
fun CheckOutButton(totalPrice: Double, orderId: Int, cartViewModel: CartViewModel, navController: NavController, userId: Int) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Button(
        onClick = {
            cartViewModel.updateOrderPrice(orderId, totalPrice)
            navController.navigate("pickUp/$userId/$orderId")
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
        modifier = if (isPortrait) {
            Modifier
                .fillMaxWidth()
                .height(65.dp)
        } else {
            Modifier
                .width(200.dp) // Make button wider in landscape
                .height(80.dp)
        },
        enabled = checkAbility(totalPrice),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Checkout",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1.2f)
            )
            Text(
                text = "RM ${"%.2f".format(totalPrice)}",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.8f)
            )
        }
    }
}
@Composable
private fun LoadFoodImage(
    foodImagePath: String,
    viewModel: CartViewModel,
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
