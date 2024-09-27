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
import com.example.unicanteen.FoodDetailCustomerDestination
import com.example.unicanteen.FoodDetailsScreenCustomer
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
import com.example.unicanteen.LimSiangShin.AddUserDestination
import com.example.unicanteen.LimSiangShin.LoginDestination
import com.example.unicanteen.LimSiangShin.LoginScreen
import com.example.unicanteen.LimSiangShin.RegistrationScreen
import com.example.unicanteen.Pierre.FoodSalesDetailDestination
import com.example.unicanteen.Pierre.FoodSalesDetailScreen
import com.example.unicanteen.Pierre.InputTableNoDestination
import com.example.unicanteen.Pierre.OrderListStatusDestination
import com.example.unicanteen.Pierre.OrderListStatusScreen
import com.example.unicanteen.Pierre.PaymentSelectionScreen
import com.example.unicanteen.Pierre.PickupOrDeliveryScreen
import com.example.unicanteen.Pierre.SaleMonthlyScreen
import com.example.unicanteen.Pierre.TableNoScreen
import com.example.unicanteen.Pierre.choosePayment
import com.example.unicanteen.Pierre.pickUpChoose
import com.example.unicanteen.Pierre.reportSaleCheck
import com.example.unicanteen.R
import com.example.unicanteen.SelectFoodDestination
import com.example.unicanteen.SelectFoodScreen
import com.example.unicanteen.SelectRestaurantDestination
import com.example.unicanteen.SelectRestaurantScreen
import com.example.unicanteen.SelectRestaurantViewModel
import com.example.unicanteen.data.Datasource
import com.example.unicanteen.database.AddOnRepositoryImpl
import com.example.unicanteen.database.AppDatabase
import com.example.unicanteen.database.FoodListRepositoryImpl
import com.example.unicanteen.database.OrderListRepositoryImpl
import com.example.unicanteen.database.PierreAdminRepositoryImpl
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.database.SellerRepositoryImpl
import com.example.unicanteen.database.UserRepositoryImpl


@Composable
fun UniCanteenNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination


    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.SellerHome.route,      //应该最后要用login的,因为从那里开始,要test先放你们的第一页
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

        composable(route = BottomBarScreen.SellerOrderList.route) {
            OrderListScreen(
                navController = navController,
                currentDestination = currentDestination,
                sellerId = 1,
                orderListRepository = OrderListRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao())
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

        composable(route = LoginDestination.route){
            LoginScreen(
                userRepository = UserRepositoryImpl(AppDatabase.getDatabase(navController.context).userDao()),
                navController = navController,
                onSignUpTextClicked = {navController.navigate(AddUserDestination.route)},
                onSignInClicked = {navController.navigate(BottomBarScreen.SellerHome.route) }
            )
        }


        composable(route = AddUserDestination.route){
            RegistrationScreen(
                userRepository = UserRepositoryImpl(AppDatabase.getDatabase(navController.context).userDao()),
                navController = navController,
                onSaveButtonClicked = {navController.navigate(LoginDestination.route)}
            )
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

        //select restaurant screen
        composable(route = SelectRestaurantDestination.route) {
            SelectRestaurantScreen(
                navController = navController,
                currentDestination = navController.currentDestination,
                sellerRepository = SellerRepositoryImpl(AppDatabase.getDatabase(navController.context).sellerDao())
            )
        }

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
        composable(
            route = FoodDetailCustomerDestination.routeWithArgs,
            arguments = listOf(navArgument(FoodDetailCustomerDestination.foodIdArg) { type = NavType.IntType })
        ) { backStackEntry ->
            val foodId = backStackEntry.arguments?.getInt(FoodDetailCustomerDestination.foodIdArg)
            FoodDetailsScreenCustomer(
                foodListRepository = FoodListRepositoryImpl(AppDatabase.getDatabase(navController.context).foodListDao()),
                addOnRepository = AddOnRepositoryImpl(AppDatabase.getDatabase(navController.context).addOnDao()),
                foodId = foodId ?: return@composable,
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
                sellerId = 1,   //temporary
                foodListRepository = FoodListRepositoryImpl(AppDatabase.getDatabase(navController.context).foodListDao()),
                addOnRepository = AddOnRepositoryImpl(AppDatabase.getDatabase(navController.context).addOnDao()),
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
                sellerAdminRepository = PierreAdminRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                userId = 1,
                orderId = 1
            )
        }

        composable(
            route = InputTableNoDestination.route,  // Define route with placeholders for orderId and userId
            arguments = listOf(
                navArgument("orderId") { type = NavType.IntType },   // Add orderId as an Int argument
                navArgument("userId") { type = NavType.IntType }     // Add userId as an Int argument
            )
        ) { backStackEntry ->
            // Retrieve the orderId and userId from the backStackEntry arguments
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            // Call the TableNoScreen with the retrieved arguments
            TableNoScreen(
                navController = navController,
                currentDestination = navController.currentDestination,
                sellerAdminRepository = PierreAdminRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                userId = 1,  // Pass the retrieved userId to the screen
                orderId = 1  // Pass the retrieved orderId to the screen
            )
        }
        composable(
            route = choosePayment.route,  // Define route with placeholders for orderId and userId
            arguments = listOf(
                navArgument("orderId") { type = NavType.IntType },   // Add orderId as an Int argument
                navArgument("userId") { type = NavType.IntType }     // Add userId as an Int argument
            )
        ) { backStackEntry ->
            // Retrieve the orderId and userId from the backStackEntry arguments
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            // Call the TableNoScreen with the retrieved arguments
            PaymentSelectionScreen(
                navController = navController,
                currentDestination = navController.currentDestination,
                sellerAdminRepository = PierreAdminRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                userId = 1,  // Pass the retrieved userId to the screen
                orderId = 1  // Pass the retrieved orderId to the screen
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
                sellerAdminRepository = PierreAdminRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                sellerId = 1
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

        // Add the composable for OrderListStatusScreen
        composable(
            route = OrderListStatusDestination.route, // Define the route with placeholders for orderId and userId
            arguments = listOf(
                navArgument("orderId") { type = NavType.IntType },   // Add orderId as an Int argument
                navArgument("userId") { type = NavType.IntType }     // Add userId as an Int argument
            )
        ) { backStackEntry ->
            // Get the orderId and userId from the backStackEntry arguments
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            // Call the OrderListStatusScreen with the retrieved arguments
            OrderListStatusScreen(
                navController = navController,
                currentDestination = navController.currentDestination,
                sellerAdminRepository = PierreAdminRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                userId = 5,  // Pass userId to the screen
                orderId = 3 // Pass orderId to the screen
            )
        }



    }
}