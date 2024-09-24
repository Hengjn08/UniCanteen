package com.example.unicanteen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.unicanteen.BottomBarScreen
import com.example.unicanteen.Food
import com.example.unicanteen.HengJunEn.AddFoodDestination
import com.example.unicanteen.HengJunEn.AddFoodScreen
import com.example.unicanteen.HengJunEn.EditFoodDestination
import com.example.unicanteen.HengJunEn.EditFoodScreen
import com.example.unicanteen.HengJunEn.FoodDetailsDestination
import com.example.unicanteen.HengJunEn.FoodDetailsScreen
import com.example.unicanteen.HengJunEn.OrderListScreen
import com.example.unicanteen.HengJunEn.SellerHomeScreen
import com.example.unicanteen.HengJunEn.SellerProfileScreen
import com.example.unicanteen.Pierre.PickupOrDeliveryScreen
import com.example.unicanteen.Pierre.SaleMonthlyScreen
import com.example.unicanteen.Pierre.pickUpChoose
import com.example.unicanteen.Pierre.reportSaleCheck
import com.example.unicanteen.R
import com.example.unicanteen.SelectFoodDestination
import com.example.unicanteen.SelectFoodScreen
import com.example.unicanteen.SelectRestaurantDestination
import com.example.unicanteen.SelectRestaurantScreen
import com.example.unicanteen.SelectRestaurantViewModel
import com.example.unicanteen.data.Datasource
import com.example.unicanteen.database.AppDatabase
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.database.SellerRepositoryImpl


@Composable
fun UniCanteenNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination


    NavHost(
        navController = navController,
        startDestination = reportSaleCheck.route,      //应该最后要用login的,因为从那里开始,要test先放你们的第一页
        modifier = modifier
    ) {
//        val sampleSellers = listOf(
//            Seller(
//                name = "Malaysian Traditional Food",
//                description = "Authentic Malaysian cuisine.",
//                imageUrl = ""
//            ),
//            Seller(
//                name = "Vegetarian Friendly",
//                description = "Delicious vegetarian dishes.",
//                imageUrl = ""
//            )
//        )

        composable(route = BottomBarScreen.SellerHome.route) {
            SellerHomeScreen(
                navController = navController,
                currentDestination = currentDestination,
                onFoodClick = { navController.navigate("${FoodDetailsDestination.route}/${it}") }
            )
        }
        composable(route = BottomBarScreen.SellerOrderList.route) {
            OrderListScreen(navController = navController, currentDestination = currentDestination)
        }
        composable(route = BottomBarScreen.SellerOrderList.route) {
            SellerProfileScreen()
        }

        // Customer-specific routes
        val sellerDao = AppDatabase.getDatabase(context = navController.context).sellerDao()
        val sellerRepository = SellerRepositoryImpl(sellerDao)
        composable(route = BottomBarScreen.CustomerHome.route) {
            SelectRestaurantScreen(
                navController = navController,
                currentDestination = currentDestination,
                onRestaurantClick = {navController.navigate("${SelectFoodDestination.route}/${it.shopName}")},
                sellerRepository = sellerRepository
            )
        }
        composable(route = BottomBarScreen.CustomerOrderList.route) {
            //CustomerOrderListScreen(navController = navController, currentDestination = currentDestination)       //放customer的order list screen
        }
        composable(route = BottomBarScreen.CustomerProfile.route) {
            //CustomerProfileScreen()                                                                               //放customer的profile screen
        }

        //Customer module route
        //select restaurant screen
        composable(
            route = SelectRestaurantDestination.route
        ) {
            SelectRestaurantScreen(
                navController = navController,
                currentDestination = currentDestination,
                onRestaurantClick = { seller ->
                    // Navigate to food screen, passing restaurant name
                    navController.navigate("${SelectFoodDestination.route}/${seller.shopName}")
                },
                sellerRepository = sellerRepository
            )
        }
        // Food Selection Screen
        composable(
            route = SelectFoodDestination.routeWithArgs,
            arguments = listOf(navArgument(SelectFoodDestination.restaurantNameArg) { type = NavType.StringType })
        ) { backStackEntry ->
            val restaurantName = backStackEntry.arguments?.getString(SelectFoodDestination.restaurantNameArg)
            val sampleFoods = listOf(
                Food(
                    name = "Laksa",
                    description = "Delicious Malaysian Laksa.",
                    imageRes = R.drawable.pan_mee,
                    price = 9.9
                ),
                Food(
                    name = "Nasi Lemak",
                    description = "Famous Malaysian dish.",
                    imageRes = R.drawable.pan_mee,
                    price = 7.5
                )
            )

            // Render SelectFoodScreen and pass the restaurant name
            SelectFoodScreen(
                sampleFoods = sampleFoods,
                restaurantName = restaurantName,
                navController = navController
            )
        }

        //Seller module route
        //add food screen
        composable(
            route = AddFoodDestination.route
        ){
            AddFoodScreen(
                onSaveButtonClicked = {navController.navigate(BottomBarScreen.SellerHome.route)},
                onCancelButtonClicked = {navController.navigate(BottomBarScreen.SellerHome.route)},
                navigateBack = {navController.navigateUp()},
            )
        }

        //food details screen
        composable(
            route = FoodDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(FoodDetailsDestination.foodIdArg) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val foodId = backStackEntry.arguments?.getInt("foodId")
            val food = Datasource.foods.find { it.id == foodId }
            FoodDetailsScreen(
                food = food,
                onEditClick = {navController.navigate(EditFoodDestination.route)},
                navigateBack = {navController.navigateUp()},
            )
        }

        //edit food screen
        composable(
            route = EditFoodDestination.route,
        ) { backStackEntry ->
            val foodId = backStackEntry.arguments?.getInt("foodId")
            val food = Datasource.foods.find { it.id == foodId }
            EditFoodScreen(
                navigateBack = {navController.navigateUp()}
            )
        }

        composable(
            route = pickUpChoose.route,
        ){
            PickupOrDeliveryScreen(
                navController = navController,
                currentDestination = currentDestination,
            )
        }

        composable(
            route = reportSaleCheck.route,
            arguments = listOf(navArgument(reportSaleCheck.chart) { type = NavType.StringType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getString(reportSaleCheck.chart)
            SaleMonthlyScreen(
                navController = navController,
                currentDestination = navController.currentDestination,
                month = "2024-09",  // Provide a sample month
                sellerId = 1  // Provide a sample seller ID
            )
        }
    }
}