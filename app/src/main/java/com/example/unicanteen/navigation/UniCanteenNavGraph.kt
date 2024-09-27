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
import com.example.unicanteen.CartDestination
import com.example.unicanteen.CartScreen
import com.example.unicanteen.FoodDetailCustomerDestination
import com.example.unicanteen.FoodDetailsScreenCustomer
import com.example.unicanteen.HengJunEn.AddFoodDestination
import com.example.unicanteen.HengJunEn.AddFoodScreen
import com.example.unicanteen.HengJunEn.EditFoodDestination
import com.example.unicanteen.HengJunEn.EditFoodScreen
import com.example.unicanteen.HengJunEn.FoodDetailsDestination
import com.example.unicanteen.HengJunEn.FoodDetailsScreen
import com.example.unicanteen.HengJunEn.OrderListScreen
import com.example.unicanteen.HengJunEn.SellerHomeDestination
import com.example.unicanteen.HengJunEn.SellerHomeDestination.sellerIdArg
import com.example.unicanteen.HengJunEn.SellerHomeScreen
import com.example.unicanteen.HengJunEn.SellerOrderListDestination
import com.example.unicanteen.HengJunEn.SellerProfileScreen
import com.example.unicanteen.LimSiangShin.AddUserDestination
import com.example.unicanteen.LimSiangShin.CustomerProfileDestination
import com.example.unicanteen.LimSiangShin.CustomerProfileScreen
import com.example.unicanteen.LimSiangShin.LoginDestination
import com.example.unicanteen.LimSiangShin.LoginScreen
import com.example.unicanteen.LimSiangShin.RegistrationScreen
import com.example.unicanteen.LimSiangShin.SellerProdileDestination
import com.example.unicanteen.LimSiangShin.SellerProfileScreen1
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
import com.example.unicanteen.Pierre.paymentReceiptDestination
import com.example.unicanteen.Pierre.paymentReceiptScreen
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
import com.example.unicanteen.database.OrderRepositoryImpl
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
        startDestination = LoginDestination.route,      //应该最后要用login的,因为从那里开始,要test先放你们的第一页
        modifier = modifier
    ) {
        composable(
            route = SellerHomeDestination.route,
            arguments = listOf(navArgument("sellerId") { type = NavType.IntType
                defaultValue =1})
        ) {
            backStackEntry ->
            val sellerId = backStackEntry.arguments?.getInt("sellerId") ?: 0
            SellerHomeScreen(
                navController = navController,
                currentDestination = currentDestination,
                onFoodClick = { navController.navigate("${FoodDetailsDestination.route}/${it}") },
                sellerId = sellerId,
                foodListRepository = FoodListRepositoryImpl(AppDatabase.getDatabase(navController.context).foodListDao())
            )
        }

        composable(route = SellerOrderListDestination.route,
                arguments = listOf(navArgument("sellerId") { type = NavType.IntType
            defaultValue =1})
        ) {
            backStackEntry ->
            val sellerId = backStackEntry.arguments?.getInt("sellerId") ?: 0
            OrderListScreen(
                navController = navController,
                currentDestination = currentDestination,
                sellerId = sellerId,
                orderListRepository = OrderListRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao())
            )
        }
//        composable(route = SellerProdileDestination.route,
//            arguments = listOf(navArgument("sellerId") { type = NavType.IntType
//                defaultValue =1})
//        ) {
//                backStackEntry ->
//            val sellerId = backStackEntry.arguments?.getInt("sellerId") ?: 0
//            SellerProfileScreen1(
//                navController = navController,
//                currentDestination = currentDestination,
//                sellerId = sellerId,
//                orderListRepository = OrderListRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao())
//            )
//        }

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


        composable(route = LoginDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType
                defaultValue =1})
            ){
                backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            LoginScreen(
                userRepository = UserRepositoryImpl(AppDatabase.getDatabase(navController.context).userDao()),
                navController = navController,
                onSignUpTextClicked = {navController.navigate(AddUserDestination.route)},
            )
        }


        composable(route = AddUserDestination.route){
            RegistrationScreen(
                userRepository = UserRepositoryImpl(AppDatabase.getDatabase(navController.context).userDao()),
                navController = navController,
            )
        }

        composable(route = CustomerProfileDestination.route){
            CustomerProfileScreen(
                userRepository = UserRepositoryImpl(AppDatabase.getDatabase(navController.context).userDao()),
                navController = navController,
                currentDestination = currentDestination,
                userId = 1
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
        composable(route = SelectRestaurantDestination.route,
            arguments = listOf(
                // Add orderId as an Int argument
                navArgument("userId") { type = NavType.IntType
                    defaultValue = 1
                }
                )
            ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            SelectRestaurantScreen(
                navController = navController,
                currentDestination = navController.currentDestination,
                sellerRepository = SellerRepositoryImpl(AppDatabase.getDatabase(navController.context).sellerDao()),
                orderRepository = OrderRepositoryImpl(AppDatabase.getDatabase(navController.context).orderDao()),
                orderListRepository = OrderListRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                userId = userId,
            )
        }

        composable(
            route = SelectFoodDestination.routeWithArgs,
            arguments = listOf(navArgument(SelectFoodDestination.sellerIdArg) { type = NavType.IntType },
                ) // Ensure argument type matches
        ) { backStackEntry ->
            val sellerId = backStackEntry.arguments?.getInt(SelectFoodDestination.sellerIdArg)
            val userId=1
            // Set up the SelectFoodScreen here, using the sellerId to fetch the relevant foods
            SelectFoodScreen(
                userId = userId,
                orderRepository = OrderRepositoryImpl(AppDatabase.getDatabase(navController.context).orderDao()),
                orderListRepository = OrderListRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                foodListRepository = FoodListRepositoryImpl(AppDatabase.getDatabase(navController.context).foodListDao()),
                sellerId = sellerId ?: return@composable, // Ensure sellerId is not null
                navController = navController,
                currentDestination = navController.currentDestination
            )
        }
        composable(
            route = FoodDetailCustomerDestination.routeWithArgs,
            arguments = listOf(navArgument(FoodDetailCustomerDestination.foodIdArg) { type = NavType.IntType },
               )
        ) { backStackEntry ->
            val foodId = backStackEntry.arguments?.getInt(FoodDetailCustomerDestination.foodIdArg)
            val userId = 1//backStackEntry.arguments?.getInt(LoginDestination.userIdArg)
            FoodDetailsScreenCustomer(
                foodListRepository = FoodListRepositoryImpl(AppDatabase.getDatabase(navController.context).foodListDao()),
                addOnRepository = AddOnRepositoryImpl(AppDatabase.getDatabase(navController.context).addOnDao()),
                orderRepository = OrderRepositoryImpl(AppDatabase.getDatabase(navController.context).orderDao()),
                orderListRepository = OrderListRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                foodId = foodId ?: return@composable,
                userId = userId ?: return@composable,
                navController = navController

            )
        }
        composable(
            route = CartDestination.route
        ){
            val userId = 1//backStackEntry.arguments?.getInt(LoginDestination.userIdArg)
            CartScreen(
                userId = userId,
                orderRepository = OrderRepositoryImpl(AppDatabase.getDatabase(navController.context).orderDao()),
                orderListRepository = OrderListRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                navController = navController
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
            arguments = listOf(
                navArgument(FoodDetailsDestination.foodIdArg) {
                type = NavType.IntType },
                navArgument(LoginDestination.userIdArg){
                    type= NavType.IntType
                })
        ) { backStackEntry ->
            val foodId = backStackEntry.arguments?.getInt(FoodDetailsDestination.foodIdArg)
            val userId = backStackEntry.arguments?.getInt(LoginDestination.userIdArg)
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
            arguments = listOf(
                navArgument("orderId"){type = NavType.IntType},
                navArgument("userId") { type = NavType.IntType }
            )
        ){
            backStackEntry ->
            // Retrieve the orderId and userId from the backStackEntry arguments
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            PickupOrDeliveryScreen(
                navController = navController,
                currentDestination = currentDestination,
                sellerAdminRepository = PierreAdminRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                userId = userId,  // Pass the retrieved userId to the screen
                orderId = orderId  // Pass the retrieved orderId to the screen
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
                userId = userId,  // Pass the retrieved userId to the screen
                orderId = orderId  // Pass the retrieved orderId to the screen
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
                userId = userId,  // Pass the retrieved userId to the screen
                orderId = orderId  // Pass the retrieved orderId to the screen
            )
        }
        composable(
            route = paymentReceiptDestination.route,  // Define route with placeholders for orderId and userId
            arguments = listOf(
                navArgument("orderId") { type = NavType.IntType },   // Add orderId as an Int argument
                navArgument("userId") { type = NavType.IntType }     // Add userId as an Int argument
            )
        ) { backStackEntry ->
            // Retrieve the orderId and userId from the backStackEntry arguments
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            // Call the TableNoScreen with the retrieved arguments
            paymentReceiptScreen(
                navController = navController,
                currentDestination = navController.currentDestination,
                sellerAdminRepository = PierreAdminRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                userId = userId,  // Pass the retrieved userId to the screen
                orderId = orderId  // Pass the retrieved orderId to the screen
            )
        }

        composable(
            route = reportSaleCheck.route,
        ) { backStackEntry ->
            val sellerId = 1 // Use a constant or retrieve from ViewModel as needed

            // Call your SaleMonthlyScreen composable
            SaleMonthlyScreen(
                navController = navController,
                currentDestination = navController.currentDestination,
                sellerAdminRepository = PierreAdminRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                sellerId = sellerId,
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
                // Add orderId as an Int argument
                navArgument("userId") { type = NavType.IntType
                defaultValue = 1
                }     // Add userId as an Int argument
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            // Call the OrderListStatusScreen with the retrieved arguments
            OrderListStatusScreen(
                navController = navController,
                currentDestination = navController.currentDestination,
                sellerAdminRepository = PierreAdminRepositoryImpl(AppDatabase.getDatabase(navController.context).orderListDao()),
                userId = userId,  // Pass userId to the screen
            )
        }



    }
}