//package com.example.unicanteen.HengJunEn
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.wrapContentWidth
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Card
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Switch
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import androidx.navigation.NavDestination
//import com.example.unicanteen.BottomNavigationBar
//import com.example.unicanteen.R
//import com.example.unicanteen.SelectRestaurantViewModel
//import com.example.unicanteen.UniCanteenTopBar
//import com.example.unicanteen.data.Datasource
//import com.example.unicanteen.database.FoodList
//import com.example.unicanteen.database.FoodListRepository
//import com.example.unicanteen.database.SellerRepository
//import com.example.unicanteen.model.Food
//import com.example.unicanteen.ui.theme.AppViewModelProvider
//import com.example.unicanteen.ui.theme.UniCanteenTheme
//
//@Composable
//fun SellerHomeScreen(
//    onFoodClick: (Int) -> Unit,
//    navController: NavController,
//    modifier: Modifier = Modifier,
//    currentDestination: NavDestination?,
//    foodListRepository: FoodListRepository,
//    sellerId: Long = 1
//    //canNavigateBack: Boolean,
//    //onEditClick: () -> Unit,
//    //onRemoveClick: () -> Unit,
//){
//    val viewModel: FoodListRepository = viewModel(
//        factory = AppViewModelProvider.Factory()
//    )
//
//    val foodItems by viewModel.foodItems.collectAsState()
//
//    LaunchedEffect(sellerId) {
//        viewModel.loadFoodItemsBySellerId(sellerId)
//    }
//
//    var available by remember { mutableStateOf(false)}
//    var foodToRemove by remember { mutableStateOf<Food?>(null) }
//    //var showDialog by remember { mutableStateOf(false) }
//
//    Scaffold (
//        modifier = Modifier.fillMaxSize(),
//        topBar = {
//            UniCanteenTopBar()
//        },
//        bottomBar = {
//            BottomNavigationBar(
//                navController = navController,
//                currentDestination = currentDestination,
//                isSeller = true
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {navController.navigate(AddFoodDestination.route)},
//                shape = MaterialTheme.shapes.medium,
//                modifier = Modifier.padding(20.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "Add icon",
//                )
//            }
//        }
//    ){ innerPadding ->
//        SellerHomeBody(
//            foodItems = foodItems,
//            onFoodClick = onFoodClick,
//            modifier = modifier.padding(innerPadding)
//        )
//    }
//}
//
////@OptIn(ExperimentalMaterial3Api::class)
////@Composable
////fun FoodListAppbar(
////    currentRoute: String?,
////    canNavigateBack: Boolean,
////    navController: NavController,
////    onEditClick: () -> Unit,
////    onRemoveClick: () -> Unit,
////    modifier: Modifier = Modifier
////) {
////    TopAppBar(
////        title = {
////            Text(
////                text = getScreenTitle(currentRoute),
////                fontWeight = FontWeight.Bold,
////                fontSize = 32.sp,
////            )
////        },
////        modifier = modifier,
////        navigationIcon = {
////            if(canNavigateBack && (currentRoute == "addFood" || currentRoute == "foodDetails/{foodId}" || currentRoute == "editFood/{foodId}")){
////                IconButton(onClick = { navController.navigateUp() }) {
////                    Icon(
////                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
////                        contentDescription = "Back",
////                        modifier = Modifier.size(48.dp)
////                    )
////                }
////            }
////        },
////        actions = {
////            //only show these actions on the homepage screen
////            when (currentRoute){
////                BottomBarScreen.Home.route -> {
////                    IconButton(onClick = { navController.navigate("addFood") }) {
////                        Icon(
////                            painter = painterResource(R.drawable.baseline_add_24),
////                            contentDescription = "Add Icon",
////                            modifier = Modifier.size(48.dp)
////                        )
////                    }
////                }
////                "foodDetails/{foodId}" -> {
////                    IconButton(onClick = onEditClick) {
////                        Icon(
////                            painter = painterResource(R.drawable.edit_24dp),
////                            contentDescription = "Edit Icon",
////                            modifier = Modifier.size(48.dp)
////                        )
////                    }
////
////                    IconButton(onClick = onRemoveClick) {
////                        Icon(
////                            painter = painterResource(R.drawable.delete_24dp),
////                            contentDescription = "Delete Icon",
////                            modifier = Modifier.size(48.dp)
////                        )
////                    }
////                }
////            }
////        },
////    )
////}
//
////@Composable
////fun getScreenTitle(route: String?): String{
////    return when(route){
////        BottomBarScreen.Home.route -> "Food List"
////        BottomBarScreen.OrderList.route -> "Order List"
////        BottomBarScreen.Profile.route -> "Profile"
////        "addFood" -> "Add Food"
////        "foodDetails/{foodId}" -> "Food Details"
////        "editFood/{foodId}" -> "Edit Food"
////        else -> "Food List"
////    }
////}
//
////@Composable
////fun showDeleteConfirmationDialog(
////    onConfirm: () -> Unit,
////    onDismiss: () -> Unit
////) {
////    AlertDialog(
////        onDismissRequest = onDismiss,
////        title = { Text("Delete Confirmation") },
////        text = { Text("Are you sure you want to delete this food item?") },
////        confirmButton = {
////            TextButton(
////                onClick = onConfirm
////            ) {
////                Text("Confirm")
////            }
////        },
////        dismissButton = {
////            TextButton(
////                onClick = onDismiss
////            ) {
////                Text("Cancel")
////            }
////        }
////    )
////}
//
//@Composable
//fun SellerHomeBody(
//    //available: Boolean,
//    //onAvailableChanged: (Boolean) -> Unit,
//    foodItems: List<FoodList>,
//    onFoodClick: (Int) -> Unit,
//    modifier: Modifier = Modifier,
//){
//    FoodList(
//        //available = available,
//        //onAvailableChanged = {onAvailableChanged},
//        foodItems = foodItems,
//        onFoodClick = onFoodClick,
//        modifier = modifier,
//    )
//}
//
////@Composable
////fun Header(
////    sellerName: String,
////    modifier: Modifier = Modifier
////){
////    Box(
////        contentAlignment = Alignment.Center,
////        modifier = Modifier
////            .fillMaxWidth()
////            .statusBarsPadding()
////            .background(
////                color = colorResource(R.color.light_orange)
////            )
////    ){
////        Column(
////            horizontalAlignment = Alignment.CenterHorizontally,
////            modifier = Modifier.padding(20.dp)
////        ){
////            Text(
////                text = stringResource(R.string.appName),
////                color = Color.White,
////                fontWeight = FontWeight.Bold,
////                style = MaterialTheme.typography.displayMedium,
////                //modifier = Modifier.padding(top = 16.dp)
////            )
////            Text(
////                text = sellerName,
////                color = Color.White,
////                fontWeight = FontWeight.Bold,
////                style = MaterialTheme.typography.displaySmall,
////                modifier = Modifier.padding(top = 8.dp)
////            )
////        }
////    }
////}
//
////To display list of food cards
//@Composable
//fun FoodList(
//    //available: Boolean,
//    //onAvailableChanged: (Boolean) -> Unit,
//    //onFoodClick: (FoodList) -> Unit,
//    foodItems: List<FoodList>,
//    onFoodClick: (Int) -> Unit,
//    modifier: Modifier = Modifier
//){
//    //val data = Datasource.foods
//    LazyColumn(modifier = modifier){
//        itemsIndexed(foodItems) { index, food ->
//            FoodCard(
//                food = food,
//                modifier = Modifier.clickable{onFoodClick(food.foodId)}
//                //available = available,
//                //onAvailableChanged = onAvailableChanged,
//                //onClick = { onFoodClick(food.id)}
//            )
//            if (index < foodItems.size - 1) {
//                HorizontalDivider(
//                    modifier = Modifier.padding(
//                        vertical = 12.dp,
//                        horizontal = 8.dp
//                    ),
//                    thickness = 1.dp,
//                    color = Color.Gray
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun FoodCard(
//    food: FoodList,
//    //available: Boolean,
//    //onAvailableChanged: (Boolean) -> Unit,
//    //onClick: () -> Unit,
//    modifier: Modifier = Modifier
//){
//    Card(
//        modifier = modifier
//            //.fillMaxWidth()
//            .padding(8.dp)
//    ){
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//        ){
////            Image(
////                painter = painterResource(food.foodImage.toInt()),
////                contentDescription = null,
////                contentScale = ContentScale.FillBounds,
////                modifier = Modifier
////                    //.padding(8.dp)
////                    .size(80.dp)
////                    //.clickable(onClick = onClick)
////                    .clip(
////                        RoundedCornerShape(8.dp)
////                    )
////            )
//            Column {
//                Text(
//                    text = food.foodName,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 28.sp,
//                    color = Color.Black,
//                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
//                )
////                Text(
////                    text = stringResource(food.foodDesc)
////                )
//                Text(
//                    text = stringResource(R.string.rm, food.price),
//                    fontSize = 20.sp,
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
////            AvailableFood(
////                available = food.available,
////                onAvailableChanged = {newAvailable ->
////                    food.available = newAvailable},
////            )
////            if(food.available) {
////                Text(
////                    text = "Available"
////                )
////            }
//        }
//    }
//}
//
////Switch to turn on/off availability of food
//@Composable
//fun AvailableFood(
//    available: Boolean,
//    onAvailableChanged: (Boolean) -> Unit,
//    modifier: Modifier = Modifier
//){
//    Switch(
//        checked = available,
//        onCheckedChange = onAvailableChanged,
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentWidth(Alignment.End)
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun UniCanteen() {
//    UniCanteenTheme {
//        //SellerHomeScreen(onFoodClick = {}, navController = {}, currentDestination =  )
//        //BottomNavigationBar()
//    }
//}