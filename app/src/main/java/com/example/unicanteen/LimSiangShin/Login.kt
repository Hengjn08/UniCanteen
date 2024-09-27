package com.example.unicanteen.LimSiangShin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unicanteen.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.unicanteen.BottomBarScreen
import com.example.unicanteen.SelectFoodDestination
import com.example.unicanteen.SelectRestaurantDestination
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.database.UserRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme

object LoginDestination : NavigationDestination {
    override val route = "Login"
    override val title = ""
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

@Composable
fun LoginScreen(
    onSignUpTextClicked:()->Unit = {},
    navController: NavController,
    userRepository: UserRepository,
//    sellerRepository: SellerRepository,
    modifier: Modifier = Modifier
){
    val userViewModel: UserViewModel = viewModel(
        factory = AppViewModelProvider.Factory(userRepository = userRepository)
    )

//    val sellerViewModel: SellerRepository = viewModel(
//        factory = AppViewModelProvider.Factory(sellerRepository = sellerRepository)
//    )
    val  loginResult by userViewModel.loginResult.collectAsState()
    val  currentUserId by userViewModel.currentUserId.collectAsState()
    val  isSeller by userViewModel.isSeller.collectAsState()

    // Track each login attempt to handle error message properly
    var loginAttempt by remember { mutableStateOf(0) }

    // Variable to track whether we should show the error message
    var showLoginFailed by remember { mutableStateOf(false) }

    var userName by remember { mutableStateOf("")}
    var pw by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(Int)}

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {}
    ){ innerPadding ->
        LoginBody(
            navController = navController,
            modifier = modifier.padding(innerPadding),
            userName = userName,
            pw = pw,
            onUserNameChange = { userName = it },
            onPwChange = { pw = it },
            onSignUpTextClicked = {onSignUpTextClicked() },
            onSignInClicked = {
                userViewModel.login(userName,pw)
                loginAttempt++
            }
        )

        // LaunchedEffect to handle login result and display the right message after each attempt
        LaunchedEffect(loginResult, loginAttempt) {
            if (loginResult) {
                // If login is successful, show welcome message and navigate to the next screen
                Toast.makeText(context, "Welcome Back, $userName!", Toast.LENGTH_SHORT).show()
                if (isSeller) {
                    navController.navigate(BottomBarScreen.SellerHome.route)
                } else {
                    navController.navigate(SelectRestaurantDestination.route)
                }
            } else if (loginAttempt > 0) {
                // Only show error message if login attempt has been made
                showLoginFailed = true
                Toast.makeText(context, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                // Clear the username and password for another try
                userName = ""
                pw = ""
            }
        }

        // Optionally, reset showLoginFailed after some delay to allow multiple error messages
        if (showLoginFailed) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000) // Example delay before hiding the error message
                showLoginFailed = false
            }
        }

    }



}

@Composable
fun LoginBody(
    navController: NavController,
    modifier: Modifier = Modifier,
    userName: String,
    pw: String,
    onUserNameChange: (String) -> Unit,
    onPwChange: (String) -> Unit,
    onSignUpTextClicked: () -> Unit,
    onSignInClicked: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center){

        Box(
            modifier = modifier
                .background(colorResource(R.color.orange_500), RoundedCornerShape(100.dp))
                .height(550.dp)
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row (modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 48.dp, top = 20.dp),
                    horizontalArrangement = Arrangement.Center){
                    Text(text = "UniCanteen",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.white))
                    IconButton(onClick = { /*TODO*/ },
                        colors = IconButtonDefaults
                            .iconButtonColors(colorResource(R.color.white)),
                        modifier = Modifier
                            .size(width = 50.dp, height = 40.dp)
                            .padding(start = 10.dp)
                    ) {

                    }
                }
                Row (modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center){
                    Text(text = "Sign Up",
                        color = colorResource(R.color.white))
                }

                Spacer(modifier = Modifier.height(16.dp))

                LoginUserName(
                    value = userName,
                    onValueChange = onUserNameChange,
                    label = "UserName",
                    placeholder = "shinx1",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    color = colorResource(R.color.white),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                PasswordField(
                    value = pw,
                    onValueChange = onPwChange,
                    label = "Password",
                    placeholder = "",
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    color = colorResource(R.color.white),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                LoginButton(
                    onSignInClicked = { onSignInClicked() },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxSize()
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = ButtonDefaults.buttonColors(Color.LightGray),
                    borderStroke = BorderStroke(1.dp,Color.Black),
                    value = "Sign In",
                    textColor = Color.Black
                )

                Row (modifier = Modifier) {
                    TextButton(onClick = {onSignUpTextClicked()}) {
                        Text(text = "Sign Up",
                            fontStyle = FontStyle.Italic,
                            textDecoration = TextDecoration.Underline)
                    }

                    TextButton(onClick = { /*TODO*/ },modifier = Modifier.padding(start = 145.dp)) {
                        Text(text = "Forgot Password?",
                            fontStyle = FontStyle.Italic,
                            textDecoration = TextDecoration.Underline)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(top = 20.dp),thickness = 2.dp, color = Color.White)
                Row (modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center){
                    Text(text = "Or Sign in with",
                        color = Color.White,
                        fontSize = 24.sp)
                }
                HorizontalDivider(modifier = Modifier.padding(start = 50.dp, end = 50.dp),thickness = 2.dp, color = Color.White)
            }
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color.LightGray),
            border = BorderStroke(1.dp,Color.Black)
        ){
            Image(painter = painterResource(R.drawable.baseline_upload_24),
                contentDescription = "Google"
            )
            Text(text="Sign In With Google",
                fontSize = 24.sp,
                color = Color.Black
            )
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color.Blue),
            border = BorderStroke(1.dp,Color.Blue)
        ){
            Image(painter = painterResource(R.drawable.baseline_upload_24),
                contentDescription = "FaceBook" )
            Text(text="Sign In With Google", fontSize = 24.sp, color = Color.White)
        }
    }
}

@Composable
fun LoginUserName(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    color: Color,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text(label)},
        singleLine = true,
        placeholder = {Text(placeholder)},
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(unfocusedContainerColor = color,
            focusedContainerColor = color,
            focusedLabelColor = color),
        shape = shape,
        modifier = modifier
    )
}

@Composable
fun LoginButton(
    onSignInClicked: () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape,
    color: ButtonColors,
    borderStroke: BorderStroke,
    value: String,
    textColor: Color
){
    Button(
        onClick = { onSignInClicked() },
        modifier = modifier,
        shape = shape,
        colors = color,
        border = borderStroke
    ) {
        Text(text = value,
            color = textColor,
            fontSize = 20.sp)
    }
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    color: Color,
    shape: RoundedCornerShape,
    visualTransformation: PasswordVisualTransformation,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text(label)},
        singleLine = true,
        placeholder = {Text(placeholder)},
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(unfocusedContainerColor = color,
            focusedContainerColor = color,
            focusedLabelColor = color),
        shape = shape,
        modifier = modifier
    )
}
@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    UniCanteenTheme {
        //val food = Datasource.foods.get(0)
//        LoginScreen(navController = )
    }
}