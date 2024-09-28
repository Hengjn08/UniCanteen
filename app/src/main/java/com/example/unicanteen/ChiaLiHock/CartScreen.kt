package com.example.unicanteen

import android.app.Application
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.unicanteen.ChiaLiHock.CartItem
import com.example.unicanteen.ChiaLiHock.CartViewModel
import com.example.unicanteen.database.FoodListRepository
import com.example.unicanteen.database.OrderList
import com.example.unicanteen.database.OrderListRepository
import com.example.unicanteen.database.OrderRepository
import com.example.unicanteen.navigation.NavigationDestination


import com.example.unicanteen.ui.theme.AppShapes
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme

object CartDestination : NavigationDestination {
    override val route = "Cart"
    override val title = "Cart"
    const val orderIdArg = "orderId"
    val routeWithArgs = "$route/{$orderIdArg}"
}

@Composable
fun CartScreen(
    application: Application, // Pass application context
    userId: Int, // Pass orderId as a parameter
    orderRepository: OrderRepository,
    orderListRepository: OrderListRepository,
    navController: NavController
) {

    val cartViewModel: CartViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,orderRepository = orderRepository, orderListRepository = orderListRepository)
    )

    var totalPrice by remember { mutableStateOf(0.0) }
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList<CartItem>())

    // Fetch cart items based on orderId when the screen is launched
    LaunchedEffect(userId) {
        cartViewModel.getCartItems(userId)
    }

    // Recalculate the total price whenever cartItems or quantity changes
    LaunchedEffect(cartItems) {
        totalPrice = cartItems.sumOf { it.price}
    }

    Column(
        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween // Ensures the checkout button stays at the bottom
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            UniCanteenTopBar(title = "Cart")
            Spacer(modifier = Modifier.height(16.dp))

            // Cart List
            if (cartItems.isEmpty()) {
                Text(
                    text = "Your cart is empty.",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            } else {
                CartList(cartItems = cartItems.toMutableList(), onCartItemsChanged = { updatedItems ->
                    totalPrice = updatedItems.sumOf { it.price }
                }, cartViewModel,userId)
            }
        }

        CheckOutButton(totalPrice = totalPrice, orderId = cartItems.firstOrNull()?.orderId ?: 0,
            cartViewModel=cartViewModel,navController = navController,userId)
    }
}

@Composable
fun CartList(
    cartItems: List<CartItem>,
    onCartItemsChanged: (List<CartItem>) -> Unit,
    cartViewModel: CartViewModel,
    userId: Int,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier
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

                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}




@Composable
fun CartCard(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    // Remove the local state for quantity
    val selectedQuantity = item.quantity

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(130.dp),
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "RM ${"%.2f".format(item.price)}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                // Image
                Image(
                    painter = rememberAsyncImagePainter(item.imageRes),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
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
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
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
            .padding(2.dp)
            .height(50.dp)
            .width(56.dp)
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
fun CheckOutButton(totalPrice: Double,orderId:Int, cartViewModel: CartViewModel,navController: NavController, userId: Int) {
    Button(
        onClick = {
            cartViewModel.updateOrderPrice(orderId,totalPrice)
            navController.navigate("pickUp/$userId/$orderId")
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        enabled = checkAbility(totalPrice),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Add padding around the row
            verticalAlignment = Alignment.CenterVertically, // Align items vertically
            horizontalArrangement = Arrangement.SpaceBetween // Space items evenly
        ) {
            Text(
                text = "CheckOut",
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
