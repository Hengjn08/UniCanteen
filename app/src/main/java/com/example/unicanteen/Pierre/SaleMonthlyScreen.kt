package com.example.unicanteen.Pierre

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.UniCanteenTopBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.database.OrderListDao
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import androidx.compose.ui.graphics.Color
import com.example.unicanteen.navigation.NavigationDestination
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import kotlin.random.Random
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.unicanteen.database.PierreAdminRepository
import com.example.unicanteen.ui.theme.AppViewModelProvider


object reportSaleCheck : NavigationDestination {
    override val route = "report_sale"
    override val title = "Report Sale"
    const val sellerIdArg = "sellerId"

    // Create a function to generate the route with arguments
    fun routeWithArgs(sellerId: Int): String {
        return "report_sale?sellerId=$sellerId"
    }
}

@Composable
fun SaleMonthlyScreen(
    navController: NavController,
    currentDestination: NavDestination?,
    sellerAdminRepository: PierreAdminRepository,
    sellerId: Int,  // Accept sellerId as a parameter
    modifier: Modifier = Modifier,
) {
    val viewModel: AdminViewModel = viewModel(
        factory = AppViewModelProvider.Factory(pierreAdminRepository = sellerAdminRepository)
    )
    val month = "2024-09"  // Set your initial month here or make it dynamic

    // Collect StateFlow for monthly sales data
    val salesData by viewModel.monthlySalesData.collectAsState()
    // Generate a list of months for the dropdown
    val monthsList = generateMonthsList()

    // State for selected month
    var selectedMonth by remember { mutableStateOf(month) }
    // Load sales data when the selected month changes
    LaunchedEffect(selectedMonth) {
        viewModel.loadMonthlySales(selectedMonth, sellerId)
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UniCanteenTopBar()
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentDestination = currentDestination,
                isSeller = true
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Title: Sales Report <<Month>>
            Text(
                text = "Sales $selectedMonth",  // Display dynamic month in title
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
            // Month Selection Dropdown
            PierreCustomDropdown(
                selectedItem = selectedMonth,
                onItemSelected = { selectedMonth = it },
                items = monthsList,
                modifier = Modifier
                    .padding(12.dp)                      // Add padding around the dropdown
                    .fillMaxWidth(0.5f)                 // Make the dropdown button half the width of its parent
                    .align(Alignment.CenterHorizontally) // Center align the dropdown

            )
            // Display the pie chart if there are sales data
            if (salesData.isNotEmpty()) {
                MonthlySalesPieChart(salesData = salesData)
            } else {
                Text("No sales data available for this month.", modifier = Modifier.padding(16.dp))
            }
            // Label row for Food Type and Sale RM
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Food Type", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(2f) .padding(start = 15.dp) )
                Text(text = "Sale RM", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1.5f))
                Text(text = "%", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
            }
            // Display the list of sales data below the pie chart
            MonthlySalesList(salesData = salesData, navController = navController,selectedMonth = selectedMonth )
        }
    }
}
@Composable
fun PierreCustomDropdown(
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    // Ensure 'expanded' state is managed correctly
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .wrapContentSize()
            .border(0.5.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
            .padding(2.dp)
            .clickable { expanded = true } // Show dropdown on click
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedItem,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Dropdown arrow")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item) // Call the item selection callback
                        expanded = false // Close the dropdown
                    },
                    text = { Text(text = item, fontSize = 16.sp) }
                )
            }
        }
    }
}

fun generateMonthsList(): List<String> {
    val monthList = mutableListOf<String>()

    // Loop through all months of the year 2024
    for (month in 1..12) {
        monthList.add(String.format("2024-%02d", month)) // Format as "2024-MM"
    }

    return monthList
}
@Composable
fun MonthlySalesList(salesData: List<OrderListDao.FoodTypeSalesData>, navController: NavController,
                     selectedMonth: String // Accept the selected month as a parameter
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(11.dp)
    ) {
        salesData.forEach { data ->
            SalesItemRow(
                foodType = data.foodType,
                totalQuantity = data.totalQuantity,
                percentage = data.percentage,
                color = getColorForItem(data.foodType), // Get color for each item
                onClick = {
                    // Navigate to the food type sales chart screen, including the month
//                    navController.navigate("food_sales_detail/${data.foodType}/$selectedMonth")
                    navController.navigate(FoodSalesDetailDestination.routeWithArgs(data.foodType, selectedMonth))
                }
            )
        }
    }
}

@Composable
fun SalesItemRow(foodType: String, totalQuantity: Double, color: Color, percentage: Double, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(color, shape = RoundedCornerShape(4.dp)) // Background color and rounded corners
            .padding(11.dp) // Inner padding
        .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = foodType, style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.weight(2f) )
        Text(text =  String.format("%.2f", totalQuantity.toDouble()), style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.weight(1.5f) )
        Text(text = String.format("%.2f", percentage), style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.weight(1f) )
    }
}

@Composable
fun MonthlySalesPieChart(salesData: List<OrderListDao.FoodTypeSalesData>) {
    // Calculate total sales
    val totalSales = salesData.sumOf { it.totalQuantity }
    val pieSlices = salesData.map { data ->
        PieChartData.Slice(
            value = data.totalQuantity.toFloat(),
            color = getColorForItem(data.foodType)// Get color for pie chart slice
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Adjust height as needed
            .padding(11.dp)
    ) {
        // Draw the pie chart
        PieChart(
            pieChartData = PieChartData(slices = pieSlices),
            modifier = Modifier.fillMaxSize(), // Fill the entire Box
            animation = simpleChartAnimation(),
            sliceDrawer = SimpleSliceDrawer()
        )

        // Overlay total sales amount
        Text(
            text = "Total: RM ${String.format("%.2f", totalSales.toDouble())}",
            style = MaterialTheme.typography.labelLarge.copy(color = Color.DarkGray),
            color = MaterialTheme.colorScheme.onSecondary,// Make it more visible
            modifier = Modifier
                .align(Alignment.Center) // Center the text
                .padding(8.dp) // Padding for better spacing
        )
    }
}



fun getColorForItem(foodType: String): Color {
    val random = Random(foodType.hashCode()) // Seed random with the foodType's hash code
    return Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)) // Random RGB color
}


