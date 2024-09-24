package com.example.unicanteen.Pierre

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.database.OrderListDao
import com.example.unicanteen.database.PierreAdminRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer

object FoodSalesDetailDestination : NavigationDestination {
    override val route = "food_sales_detail/{foodType}/{month}"
    override val title = "Food Sales Detail"
    // Create a function to generate the route with arguments
    fun routeWithArgs(foodType: String, month: String): String {
        return "food_sales_detail/$foodType/$month"
    }
}

@Composable
fun FoodSalesDetailScreen(
    navController: NavController,
    currentDestination: NavDestination?,
    foodType: String, // Food type passed as an argument
    sellerAdminRepository: PierreAdminRepository,
    sellerId: Int,
    month: String?, // Accept month as a nullable parameter
    modifier: Modifier = Modifier
) {
    // Initialize the ViewModel
    val viewModel: AdminViewModel = viewModel(
        factory = AppViewModelProvider.Factory(repository3 = sellerAdminRepository)
    )

    // Set the initial month to "2024-09" if no month is provided
    val initialMonth = month ?: "2024-09"
    val monthsList = generateMonthsList()
    var selectedMonth by remember { mutableStateOf(initialMonth) }

    // Load food sales data based on the food type and selected month
    LaunchedEffect(selectedMonth) {
        viewModel.loadSalesByFoodType(sellerId,foodType, selectedMonth) // Modify your ViewModel method to accept month
    }

    // Collect StateFlow for food sales data
    val foodSalesData by viewModel.foodSalesData.collectAsState()
    // Collect monthly sales data for food type
    val monthlySalesData by viewModel.monthlySalesData.collectAsState() // Assuming you need this data for MonthlySalesList


    Scaffold(
        topBar = { UniCanteenTopBar() },
        bottomBar = { BottomNavigationBar(
            navController = navController,
            currentDestination = currentDestination,
            isSeller = true
        ) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Title: Sales Report for <FoodType>
            Text(
                text = "Sales for $foodType in $selectedMonth", // Display dynamic month in title
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
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterHorizontally)
            )

            // Display the pie chart if there are food sales data
            if (foodSalesData.isNotEmpty()) {
                MonthlySalesPieChart(foodSalesData) // Add pie chart for sales data
            } else {
                Text("No sales data available for this food type.", modifier = Modifier.padding(16.dp))
            }

            // Label row for Food Type and Sale RM
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Food Name", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(2f).padding(start = 25.dp))
                Text(text = "Sale RM", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1.5f))
                Text(text = "%", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
            }
            // Display the list of sales data below the pie chart
            MonthlyFoodTypeSalesList(salesData = foodSalesData, navController = navController,selectedMonth = selectedMonth )

        }
    }
}
@Composable
fun MonthlyFoodTypeSalesList(
    salesData: List<OrderListDao.FoodSalesData>,
    navController: NavController,
    selectedMonth: String // Accept the selected month as a parameter
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(11.dp)
    ) {
        salesData.forEach { data ->
            FoodSalesItemRow(
                foodType = data.foodType,
                totalQuantity = data.totalQuantity,
                percentage = data.percentage,
                color = getColorForItem(data.foodType) // Get color for each item
                // Removed onClick parameter
            )
        }
    }
}
@Composable
fun FoodSalesItemRow(
    foodType: String,
    totalQuantity: Double,
    percentage: Double,
    color: Color, // Assuming you have a Color type for the item color
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(color, shape = RoundedCornerShape(4.dp)) // Background color and rounded corners
            .padding(11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = foodType, style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.weight(2f) )
        Text(text =  String.format("%.2f", totalQuantity), style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.weight(1.5f) )
        Text(text = String.format("%.2f", percentage), style = MaterialTheme.typography.bodyMedium, color = Color.White, modifier = Modifier.weight(1f) )
    }
}


// Assuming you have a function to create a pie chart similar to MonthlySalesPieChart
@Composable
fun MonthlySalesPieChart(salesData: List<OrderListDao.FoodSalesData>) {
    // Calculate total sales
    val totalSales = salesData.sumOf { it.totalQuantity }
    val pieSlices = salesData.map { data ->
        PieChartData.Slice(
            value = data.totalQuantity.toFloat(),
            color = getColorForItem(data.foodType) // Get color for pie chart slice
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
            style = MaterialTheme.typography.labelLarge.copy(color = Color.DarkGray), // Make it more visible
            modifier = Modifier
                .align(Alignment.Center) // Center the text
                .padding(8.dp) // Padding for better spacing
        )
    }
}
