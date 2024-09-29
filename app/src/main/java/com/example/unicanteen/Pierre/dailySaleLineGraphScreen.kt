package com.example.unicanteen.Pierre

import android.app.Application
import android.content.res.Configuration
import android.graphics.Paint
import android.util.Log
import android.util.Size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.xaxis.XAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.YAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation

object dailyOrderSaleDestination : NavigationDestination {
    override val route = "daily_sale?sellerId={sellerId}&month={month}"
    override val title = "Daily Sales Detail"
    // Create a function to generate the route with arguments
    fun routeWithArgs(sellerId: Int, month: String): String {
        return "daily_sale?sellerId=$sellerId&month=$month"
    }
}

@Composable
fun DailySalesDetailScreen(
    application: Application, // Pass application context
    navController: NavController,
    currentDestination: NavDestination?,
    sellerAdminRepository: PierreAdminRepository,
    sellerId: Int,
    month: String?, // Accept month as a nullable parameter
    modifier: Modifier = Modifier
) {
    Log.d("RouteDailySalesDetailScreen", "Seller ID: $sellerId, Month: $month")
    // Initialize the ViewModel
    val viewModel: AdminViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,pierreAdminRepository = sellerAdminRepository)
    )

    // Set the initial month to "2024-09" if no month is provided
    val initialMonth = month ?: "2024-09"
    val monthsList = generateMonthsList()
    var selectedMonth by remember { mutableStateOf(initialMonth) }

    // Load food sales data based on the food type and selected month
    LaunchedEffect(selectedMonth) {
        viewModel. loadSalesByDaily(sellerId, selectedMonth) // Modify your ViewModel method to accept month
    }
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    // Collect StateFlow for food sales data
    val dailySaleData by viewModel.dailySaleData.collectAsState()


    Scaffold(
        topBar = { if (isPortrait) {
            UniCanteenTopBar()

        } },
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
                text = "Daily in $selectedMonth", // Display dynamic month in title
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
            // Dropdown for selecting a month
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

            Spacer(modifier = Modifier.height(16.dp))

            // Render line chart if dailySaleData is not empty
            if (dailySaleData.isNotEmpty()) {
                MyDailySalesLineChart(
                    salesData = dailySaleData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(300.dp)
                )
            } else {
                Text(
                    text = "No sales data available for the selected month.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

        }
    }
}
@Composable
fun MyDailySalesLineChart(
    salesData: List<OrderListDao.DailySalesDataBySeller>,
    modifier: Modifier = Modifier
) {
    // Map DailySalesDataBySeller to LineChartData points
    val points = salesData.mapIndexed { index, data ->
        LineChartData.Point(
            value = data.totalQuantity.toFloat(),
            label = data.day // Use `day` as the label for x-axis
        )
    }
    // Extract labels for x-axis
    val labels = salesData.map { it.day.takeLast(2) }
    Log.d("RouteDailySalesDetailScreen", "Sale Data: $salesData")
    // Create LineChartData using the points list
    val lineChartData = LineChartData(
        points = points,
        lineDrawer = SolidLineDrawer() // Customize your line drawer here
    )

    LineChart(
        linesChartData = listOf(lineChartData),
        modifier = modifier,
        animation = simpleChartAnimation(),
        pointDrawer = FilledCircularPointDrawer(), // Customize point drawer if needed
        labels = labels, // Pass labels for the x-axis
        xAxisDrawer = SimpleXAxisDrawer(), // Add x-axis drawer
        yAxisDrawer = SimpleYAxisDrawer(), // Add y-axis drawer
        horizontalOffset = 5f
    )
}




