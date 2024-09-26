package com.example.unicanteen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unicanteen.navigation.NavigationDestination


import com.example.unicanteen.ui.theme.AppShapes
import com.example.unicanteen.ui.theme.UniCanteenTheme

object CartDestination : NavigationDestination {
    override val route = "Cart"
    override val title = "Cart"
    const val orderIdArg = "foodId"
    val routeWithArgs = "$route/{$orderIdArg}"
}
@Composable
fun CartScreen(cartItems: MutableList<CartItem>) {
    var totalPrice by remember { mutableStateOf(0.0) }

    // Recalculate the total price whenever cartItems or quantity changes
    LaunchedEffect(cartItems) {
        totalPrice = cartItems.sumOf { it.price * it.quantity }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween // Ensures the checkout button stays at the bottom
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            UniCanteenTopBar(
                title = "Cart"
            )
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
                CartList(cartItems = cartItems, onCartItemsChanged = { updatedItems ->
                  //  cartItems.clear()
                   // cartItems.addAll(updatedItems)
                    // Recalculate the total price after items are changed
                    totalPrice = cartItems.sumOf { it.price * it.quantity }
                })
            }
        }

        CheckOutButton(totalPrice = totalPrice)
    }
}

@Composable
fun CartList(
    cartItems: MutableList<CartItem>,
    onCartItemsChanged: (MutableList<CartItem>) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.height(670.dp)
    ) {
        items(cartItems.size) { index ->
            CartCard(
                item = cartItems[index],
                onQuantityChange = { newQuantity ->
                    // Update the quantity of the item and notify the change
                    cartItems[index].quantity = newQuantity
                    onCartItemsChanged(cartItems)
                },
                onDelete = {
                    // Remove the item from the list and notify the change
                    cartItems.removeAt(index)
                    onCartItemsChanged(cartItems)
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
          //  contentAlignment = Alignment.Center // Center the content within the Box
        ) {
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                //horizontalArrangement = Arrangement.SpaceBetween // Space items evenly
            ) {
                // Item Info
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp) // Add padding to separate from quantity selector
                ) {
                    Text(
                        text = item.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "RM ${"%.2f".format(item.price)}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                // Image
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 8.dp)
                        .align(Alignment.CenterVertically)
                        .clip(RoundedCornerShape(8.dp))
                )

                // Quantity Dropdown
                EnhancedQuantityDropdown(
                    quantity = item.quantity,
                    onQuantityChange = onQuantityChange,
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
    val quantities = (1..10).toList()
    var selectedQuantity by remember { mutableStateOf(quantity) }
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
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)),
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

@Composable
fun CheckOutButton(totalPrice: Double) {
    Button(
        onClick = { /* Handle add to cart */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF6AD44)),
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        shape = AppShapes.large
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

@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    val cartItems = listOf(
        CartItem(name = "Smoked Duck Ramen", imageRes = R.drawable.pan_mee, price = 8.90, quantity = 1),
        CartItem(name = "Mushroom and Meat Sauce Noodles", imageRes = R.drawable.pan_mee, price = 10.90, quantity = 1),
        CartItem(name = "Minced Meat Lotus Noodles", imageRes = R.drawable.pan_mee, price = 10.90, quantity = 1),
        CartItem(name = "Smoked Duck Ramen", imageRes = R.drawable.pan_mee, price = 8.90, quantity = 1),
        CartItem(name = "Mushroom and Meat Sauce Noodles", imageRes = R.drawable.pan_mee, price = 10.90, quantity = 1),
        CartItem(name = "Minced Meat Lotus Noodles", imageRes = R.drawable.pan_mee, price = 10.90, quantity = 1)
    )

    // Using spread operator to unpack the list into mutableStateListOf
    val cartItem = remember { mutableStateListOf(*cartItems.toTypedArray()) }

    UniCanteenTheme {
        CartScreen(
            cartItems = cartItem
        )
    }
}
