package com.example.unicanteen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unicanteen.ui.theme.AppShapes

@Composable
fun FoodDetailsScreen(food: Food) {
    var addOnPrice by remember { mutableStateOf(0.0) } // For calculating the total price

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        FoodDetailsCard(food = food) // Pass the food object here
        Spacer(modifier = Modifier.height(20.dp))
        addOnPrice = AddOnSection(food = food)
        Spacer(modifier = Modifier.height(20.dp))
        RemarksSection()
        Spacer(modifier = Modifier.weight(1f)) // Push the Add to Cart button to the bottom
        AddToCartButton(totalPrice = food.price + addOnPrice) // Calculate the total price with add-ons
    }
}

@Composable
fun FoodDetailsCard(food: Food) {
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6AD44))
    ) {
        Column {
            Image(
                painter = painterResource(id = food.imageRes), // Use image from Food data class
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()) {
                Text(text = food.name, color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom =5.dp))
                Text(text = food.description,
                    style = MaterialTheme.typography.bodyMedium, color = colorResource(id = R.color.purple_grey_40)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(modifier = Modifier.fillMaxWidth(),
                    text = "RM ${"%.2f".format(food.price)}"
                    ,fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium, color = Color.White,
                    textAlign = TextAlign.Right)
            }
        }
    }
}

@Composable
fun AddOnSection(food: Food): Double {
    var totalAddOnPrice by remember { mutableStateOf(0.0) }
    val selectedAddOns = remember { mutableStateMapOf<String, Boolean>() }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
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

            food.addOns.forEach { addOn ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, start = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isChecked = selectedAddOns[addOn.description] ?: false
                    Column(modifier = Modifier.padding(0.dp)
                    ) {
                        Checkbox(
                            modifier = Modifier
                                .padding(start = 0.dp)
                                .align(alignment = Alignment.Start),
                            checked = isChecked,
                            onCheckedChange = {
                                selectedAddOns[addOn.description] = it
                                totalAddOnPrice = if (it) {
                                    totalAddOnPrice + addOn.price
                                } else {
                                    totalAddOnPrice - addOn.price
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text(text = "${addOn.description}")
                        Text(text ="+RM ${"%.2f".format(addOn.price)}", color = Color.Gray)
                    }

                }
            }
        }

    }

    return totalAddOnPrice
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

@Preview(showBackground = true)
@Composable
fun PreviewFoodDetailsScreen() {
    // Sample food object for preview
    val sampleFood = Food(
        name = "Japanese Ramen",
        description = "Ramen with Japan flavour",
        imageRes = R.drawable.pan_mee,
        price = 7.90,
        addOns = listOf(
            AddOn(description = "Noodle", price = 1.00),
            AddOn(description = "Egg", price = 1.00)
        )
    )

    FoodDetailsScreen(food = sampleFood)
}
