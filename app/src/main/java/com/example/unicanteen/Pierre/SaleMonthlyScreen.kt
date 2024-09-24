package com.example.unicanteen.Pierre

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.ui.theme.UniCanteenTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unicanteen.database.OrderListDao
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.unicanteen.navigation.NavigationDestination
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import kotlin.random.Random
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import java.util.Calendar


object reportSaleCheck : NavigationDestination {
    override val route = "report_sale"
    override val title = "Report Sale"
    const val chart = "chartId"
    val routeWithArgs = "$route/{$chart}"
}

@Composable
fun SaleMonthlyScreen(
    navController: NavController,
    currentDestination: NavDestination?,
    month: String,  // Accept month input
    sellerId: Int,  // Accept sellerId input
    modifier: Modifier = Modifier,
) {
    val viewModel: AdminViewModel = viewModel()  // Use AdminViewModel instead of SalesViewModel
    val salesData by viewModel.getMonthlySalesByFoodType(month, sellerId).observeAsState(emptyList())
    // Generate a list of months for the dropdown
    val monthsList = generateMonthsList()

    // State for selected month
    var selectedMonth by remember { mutableStateOf(month) }

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
        ) {
            // Title: Sales Report <<Month>>
            Text(
                text = "Sales Report for $selectedMonth",  // Display dynamic month in title
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
            // Month Selection Dropdown
            PierreCustomDropdown(
                selectedItem = selectedMonth,
                onItemSelected = { selectedMonth = it },
                items = monthsList,
                modifier = Modifier.padding(12.dp)
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
            MonthlySalesList(salesData = salesData)
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
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
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
                color = Color.Black
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
    // Get the current date using Calendar
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH) + 1 // Months are 0-based in Calendar

    val monthList = mutableListOf<String>()

    for (i in 0..2) { // For the current month and the next two months
        var month = currentMonth + i
        var year = currentYear

        if (month > 12) {
            month -= 12
            year += 1
        }

        monthList.add(String.format("%04d-%02d", year, month)) // Format as "YYYY-MM"
    }

    return monthList
}
@Composable
fun MonthlySalesList(salesData: List<OrderListDao.FoodTypeSalesData>) {
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
                color = getColorForItem(data.foodType) // Get color for each item
            )
        }
    }
}

@Composable
fun SalesItemRow(foodType: String, totalQuantity: Int, color: Color, percentage: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(color, shape = RoundedCornerShape(4.dp)) // Background color and rounded corners
            .padding(11.dp), // Inner padding
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = foodType, style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.weight(2f) )
        Text(text =  String.format("%.2f", totalQuantity.toDouble()), style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.weight(1.5f) )
        Text(text = String.format("%.2f", percentage), style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.weight(1f) )
    }
}

@Composable
fun MonthlySalesPieChart(salesData: List<OrderListDao.FoodTypeSalesData>) {
    val pieSlices = salesData.map { data ->
        PieChartData.Slice(
            value = data.totalQuantity.toFloat(),
            color = getColorForItem(data.foodType)// Get color for pie chart slice
        )
    }

    PieChart(
        pieChartData = PieChartData(slices = pieSlices),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Adjust height as needed
            .padding(11.dp),
        animation = simpleChartAnimation(),
        sliceDrawer = SimpleSliceDrawer()
    )
}



fun getColorForItem(foodType: String): Color {
    val random = Random(foodType.hashCode()) // Seed random with the foodType's hash code
    return Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)) // Random RGB color
}




@Preview(showBackground = true)
@Composable
fun PreviewSaleMonthlyScreen() {
    UniCanteenTheme {
        // Use a mock NavController
        val navController = rememberNavController()
        SaleMonthlyScreen(
            navController = navController,
            currentDestination = null,
            month = "2024-09",  // Provide a sample month
            sellerId = 1  // Provide a sample seller ID
        )
    }
}