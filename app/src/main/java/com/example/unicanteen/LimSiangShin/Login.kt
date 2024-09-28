package com.example.unicanteen.LimSiangShin


import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.unicanteen.BottomBarScreen
import com.example.unicanteen.Pierre.OrderListStatusDestination
import com.example.unicanteen.SelectRestaurantDestination
import com.example.unicanteen.database.UserRepository
import com.example.unicanteen.navigation.GlobalVariables
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme

object LoginDestination : NavigationDestination {
    override val route = "Login?userId={userId}"
    override val title = "Login"
    const val userIdArg = "userId"
    fun routeWithArgs(userId: Int): String{
        return "${route}/$userId"
    }
}


@Composable
fun LoginScreen(
    application: Application, // Pass application context
    onSignUpTextClicked:()->Unit = {},
    onHelpClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    navController: NavController,
    userRepository: UserRepository,
    modifier: Modifier = Modifier
){
    val userViewModel: UserViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application, userRepository = userRepository)
    )

    val loginResult by userViewModel.loginResult.collectAsState()
    val currentUserId by userViewModel.currentUserId.collectAsState()
    val sellerId by userViewModel.isSellerId.collectAsState()
    val isSeller by userViewModel.isSeller.collectAsState()

    var loginAttempt by remember { mutableStateOf(true) }
    var showLoginFailed by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(0) }

    val context = LocalContext.current

    // LaunchedEffect to update userId when currentUserId changes
    LaunchedEffect(currentUserId) {
        currentUserId?.let {
            userId = it // Update local userId when currentUserId is available
            Log.d("Login", "User logged in with ID: $userId")
            GlobalVariables.userId = userId // Set global userId
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {}
    ) { innerPadding ->
        LoginBody(
            userId = userId,
            navController = navController,
            modifier = modifier.padding(innerPadding),
            userName = userName,
            pw = pw,
            onUserNameChange = { userName = it },
            onPwChange = { pw = it },
            onSignUpTextClicked = { onSignUpTextClicked() },
            onHelpClicked = { onHelpClicked() },
            onForgotPasswordClicked = { onForgotPasswordClicked() },
            onSignInClicked = {
                userViewModel.login(userName, pw)
                loginAttempt = !loginAttempt
                Log.d("Login", "Login page User logged in with ID: $userId, Global: ${GlobalVariables.userId}, currId: $userId")
               // navController.navigate("${SelectRestaurantDestination.routeWithArgs(currentUserId!!)}")
            }
        )

        // Handle login result and display the appropriate message
        LaunchedEffect(loginResult) {
            if (loginResult) {
                // Successful login
                Toast.makeText(context, "Welcome Back, $userName!", Toast.LENGTH_SHORT).show()
                if (isSeller) {
                    navController.navigate(BottomBarScreen.SellerHome.route)
                } else {
                    navController.navigate(SelectRestaurantDestination.routeWithArgs(userId))
                }
            } else {
                showLoginFailed = true
                // Clear username and password for retry
                userName = ""
                pw = ""
            }

            // Show error message for failed login
            if (showLoginFailed) {
                Toast.makeText(context, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                kotlinx.coroutines.delay(2000) // Delay before hiding the error message
                showLoginFailed = false
            }
        }
    }
}

@Composable
fun LoginBody(
    userId: Int,
    navController: NavController,
    modifier: Modifier = Modifier,
    userName: String,
    pw: String,
    onUserNameChange: (String) -> Unit,
    onPwChange: (String) -> Unit,
    onHelpClicked:() -> Unit,
    onSignUpTextClicked: () -> Unit,
    onForgotPasswordClicked:()->Unit,
    onSignInClicked: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){

        Column(
            modifier = modifier
                .background(colorResource(R.color.orange_500), RoundedCornerShape(50.dp))
                .padding(vertical =  20.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    Column (){
                        Icon(imageVector = Icons.Default.QuestionMark,
                            contentDescription ="Help",
                            modifier = Modifier
                                .size(width = 50.dp, height = 40.dp)
                                .padding(start = 10.dp)
                                .background(colorResource(R.color.orange_500), CircleShape)
                                .border(2.dp, Color.White, CircleShape)

                                .clickable {
                                    onHelpClicked()
                                },
                            tint = Color.White
                        )
                    }
                }

                Row (modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center){
                    Text(text = "Sign In",
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
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    isPassword = true,
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

                    TextButton(onClick = { onForgotPasswordClicked() },modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End)) {
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
                HorizontalDivider(modifier = Modifier.padding(start = 20.dp, end = 20.dp),thickness = 2.dp, color = Color.White)


                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(vertical = 20.dp)

                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color.LightGray),
                    border = BorderStroke(1.dp,Color.Black)
                ){
                    Row(modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically){
                        Image(painter = painterResource(R.drawable.google_logo),
                            contentDescription = "Google",
                        )
                        Column (modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(text="Sign In With Google",
                                fontSize = 24.sp,
                                color = Color.Black,
                            )
                        }
                    }
                }

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color.Blue),
                    border = BorderStroke(1.dp,Color.Blue)
                ){
                    Row(modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically){
                        Image(painter = painterResource(R.drawable.facebook_logo),
                            contentDescription = "Facebook",
                        )
                        Column (modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(text="Sign In With Facebook",
                                fontSize = 24.sp,
                                color = Color.White,
                            )
                        }
                    }
                }
            }
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
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
){
    var showPassword by rememberSaveable { mutableStateOf(false) }

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
        trailingIcon = {
            if (isPassword){
                Icon(
                    if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (showPassword) "Show Password" else "Hide Password",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { showPassword = !showPassword }
                )
            }else {
                null
            }
        },
        visualTransformation = if (isPassword){
            if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
        } else { VisualTransformation.None },
        shape = shape,
        modifier = modifier
    )
}
@Preview(showBackground = true)
@Composable
fun LoginPreview() {

    UniCanteenTheme {
        //val food = Datasource.foods.get(0)
//        LoginScreen(onHelpClicked = {})
    }
}