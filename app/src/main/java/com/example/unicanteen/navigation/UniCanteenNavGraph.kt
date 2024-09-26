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
import com.example.unicanteen.HengJunEn.AddFoodDestination
import com.example.unicanteen.HengJunEn.AddFoodScreen
import com.example.unicanteen.HengJunEn.EditFoodDestination
import com.example.unicanteen.HengJunEn.EditFoodScreen
import com.example.unicanteen.HengJunEn.FoodDetailsDestination
import com.example.unicanteen.HengJunEn.FoodDetailsScreen
import com.example.unicanteen.HengJunEn.OrderListScreen
import com.example.unicanteen.HengJunEn.SellerHomeDestination.sellerIdArg
import com.example.unicanteen.HengJunEn.SellerHomeScreen
import com.example.unicanteen.HengJunEn.SellerProfileScreen
import com.example.unicanteen.Pierre.FoodSalesDetailDestination
import com.example.unicanteen.Pierre.FoodSalesDetailScreen
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
import com.example.unicanteen.database.FoodListRepositoryImpl
import com.example.unicanteen.database.PierreAdminRepositoryImpl
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
        startDestination = BottomBarScreen.SellerOrderList.route,      //应该最后要用login的,因为从那里开始,要test先放你们的第一页
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

        composable(
            route = BottomBarScreen.SellerHome.route,
            arguments = listOf(navArgument(sellerIdArg) { type = NavType.IntType })
        ) {
            SellerHomeScreen(
                navController = navController,
                currentDestination = currentDestination,
                onFoodClick = { navController.navigate("${FoodDetailsDestination.route}/${it}") },
                sellerId = 1,
                foodListRepository = FoodListRepositoryImpl(AppDatabase.getDatabase(navController.context).foodListDao())
            )
        }
//        composable(
//            route = SelectFoodDestination.routeWithArgs,
//            arguments = listOf(navArgument(SelectFoodDestination.sellerIdArg) { type = NavType.IntType }) // Ensure argument type matches
//        ) { backStackEntry ->
//            val sellerId = backStackEntry.arguments?.getInt(SelectFoodDestination.sellerIdArg)
//
//            // Set up the SelectFoodScreen here, using the sellerId to fetch the relevant foods
//            SelectFoodScreen(
//                foodListRepository = FoodListRepositoryImpl(AppDatabase.getDatabase(navController.context).foodListDao()),
//                sellerId = sellerId ?: return@composable, // Ensure sellerId is not null
//                navController = navController,
//                currentDestination = navController.currentDestination
//            )
//        }
        composable(route = BottomBarScreen.SellerOrderList.route) {
            OrderListScreen(navController = navController, currentDestination = currentDestination)
        }
        composable(
            route = BottomBarScreen.SellerReport.route,
            arguments = listOf(navArgument(reportSaleCheck.chart) { type = NavType.StringType })
        ) { backStackEntry ->
            val chartId = backStackEntry.arguments?.getString(reportSaleCheck.chart)
            SaleMonthlyScreen(
                navController = navController,
                currentDestination = navController.currentDestination,
                sellerAdminRepository = PierreAdminRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                sellerId = 1
            )
        }
        composable(route = BottomBarScreen.SellerProfile.route) {
            SellerProfileScreen()
        }

        // Customer-specific routes
//        composable(route = BottomBarScreen.CustomerHome.route) {
//            SelectRestaurantScreen(
//                navController = navController,
//                currentDestination = currentDestination,
//                onRestaurantClick = { seller ->
//                    // Navigate to food selection screen, passing the seller's ID
//                    navController.navigate("${SelectFoodDestination.route}/${seller.sellerId}")
//                },
//                sellerRepository = SellerRepositoryImpl(AppDatabase.getDatabase(context = navController.context).sellerDao())
//            )
//        }
        composable(route = BottomBarScreen.CustomerOrderList.route) {
            //CustomerOrderListScreen(navController = navController, currentDestination = currentDestination)       //放customer的order list screen
        }
        composable(route = BottomBarScreen.CustomerProfile.route) {
            //CustomerProfileScreen()                                                                               //放customer的profile screen
        }

        //Customer module route
        //select restaurant screen
//        composable(route = SelectRestaurantDestination.route) {
//            SelectRestaurantScreen(
//                navController = navController,
//                currentDestination = navController.currentDestination,
//                onRestaurantClick = { seller ->
//                    // Navigate to food selection screen, passing the seller's ID
//                    navController.navigate("${SelectFoodDestination.route}/${seller.sellerId}")
//                },
//                sellerRepository = SellerRepositoryImpl(AppDatabase.getDatabase(navController.context).sellerDao())
//            )
//        }

        composable(
            route = SelectFoodDestination.routeWithArgs,
            arguments = listOf(navArgument(SelectFoodDestination.sellerIdArg) { type = NavType.IntType }) // Ensure argument type matches
        ) { backStackEntry ->
            val sellerId = backStackEntry.arguments?.getInt(SelectFoodDestination.sellerIdArg)

            // Set up the SelectFoodScreen here, using the sellerId to fetch the relevant foods
            SelectFoodScreen(
                foodListRepository = FoodListRepositoryImpl(AppDatabase.getDatabase(navController.context).foodListDao()),
                sellerId = sellerId ?: return@composable, // Ensure sellerId is not null
                navController = navController,
                currentDestination = navController.currentDestination
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
                //foodListRepository = FoodListRepositoryImpl(AppDatabase.getDatabase(navController.context).foodListDao()),
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
            val foodId = backStackEntry.arguments?.getInt(FoodDetailsDestination.foodIdArg)
            //val food = Datasource.foods.find { it.id == foodId }
            FoodDetailsScreen(
                foodId = foodId ?: return@composable,
                foodListRepository = FoodListRepositoryImpl(AppDatabase.getDatabase(navController.context).foodListDao()),
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

        // Add the composable for FoodSalesDetailScreen
        composable(
            route = FoodSalesDetailDestination.route,
            arguments = listOf(
                navArgument("foodType") { type = NavType.StringType },
                navArgument("month") { type = NavType.StringType } // Add month as a string argument
            )
        ) { backStackEntry ->
            val foodType = backStackEntry.arguments?.getString("foodType") ?: ""
            val month = backStackEntry.arguments?.getString("month")

            // Call the FoodSalesDetailScreen
            FoodSalesDetailScreen(
                navController = navController,
                currentDestination = navController.currentDestination,
                foodType = foodType,
//                foodType = "Beverage",
                sellerAdminRepository = PierreAdminRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                sellerId = 1,
                month = month
            )
        }
    }
}