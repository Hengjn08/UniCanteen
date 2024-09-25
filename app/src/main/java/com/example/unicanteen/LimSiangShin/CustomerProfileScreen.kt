package com.example.unicanteen.LimSiangShin

import android.graphics.drawable.Icon
import android.text.BoringLayout
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.database.AppDatabase
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.database.SellerRepositoryImpl
import com.example.unicanteen.database.UserRepository
import com.example.unicanteen.database.UserRepositoryImpl
import com.example.unicanteen.model.User
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme

object CustomerProfileDestination : NavigationDestination {
    override val route = "User Profile"
    override val title = ""
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

@Composable
fun CustomerProfileScreen(
    userRepository: UserRepository,
    sellerRepository: SellerRepository,
    onSaveButtonClicked: () -> Unit = {},
    navController: NavController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
){
    val userViewModel: UserViewModel = viewModel(
        factory = AppViewModelProvider.Factory(userRepository = userRepository, sellerRepository = sellerRepository)
    )

    var userName by remember { mutableStateOf("123")}
    var email by remember { mutableStateOf("123") }
    var pw by remember { mutableStateOf("123") }
    var confirmPw by remember { mutableStateOf("") }

    var phoneNumber by remember { mutableStateOf("123") }

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UniCanteenTopBar()
        },
        bottomBar = {
            BottomNavigationBar(navController = navController,
                currentDestination = currentDestination,
                isSeller = false)
        }
    ){ innerPadding ->
        CustomerDetailBody(
            modifier = modifier.padding(innerPadding),
            userName = userName,
            email = email,
            pw = pw,
            confirmPw = confirmPw,
            phoneNumber = phoneNumber,
            onUserNameChange = { userName = it },
            onEmailChange = { email = it },
            onPwChange = { pw = it },
            onPhoneNumberChange = { phoneNumber = it },
            onConfirmPwChange = { confirmPw = it },
            onSaveButtonClicked = {
                onSaveButtonClicked()
                Toast.makeText(context, "Register successfully!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}



@Composable
fun CustomerDetailBody(
    modifier: Modifier = Modifier,
    userName: String,
    email: String,
    pw: String,
    phoneNumber: String,
//    navController: NavController,
    confirmPw: String,
    onUserNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPwChange: (String) -> Unit,
    onConfirmPwChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSaveButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
            ){
            Column(
                modifier = modifier.padding(0.dp)
                    .background(colorResource(R.color.orange_500))
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.Top
            ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        ) {
                        Text(
                            text = "Profile",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.white)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    CustomerDetailLabel(
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
                        icon = Icons.Default.Edit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                    CustomerDetailLabel(
                        value = email,
                        onValueChange = onEmailChange,
                        label = "Email Address",
                        placeholder = "123456@gmail.com",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        color = colorResource(R.color.white),
                        shape = RoundedCornerShape(8.dp),
                        icon = Icons.Default.Edit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    CustomerDetailLabel(
                        value = phoneNumber,
                        onValueChange = onPhoneNumberChange,
                        label = "Phone Number",
                        placeholder = "0123455678",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        color = colorResource(R.color.white),
                        shape = RoundedCornerShape(8.dp),
                        icon = Icons.Default.Edit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    CustomerDetailLabel(
                        value = pw,
                        onValueChange = onPwChange,
                        label = "Password",
                        placeholder = "123456",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        color = colorResource(R.color.white),
                        shape = RoundedCornerShape(8.dp),
                        isPassword = true,
                        icon = Icons.Default.Edit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
            }

         HorizontalDivider(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                thickness = 5.dp,
                color = colorResource(R.color.orange_500)
            )

        Button(onClick = onSaveButtonClicked,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.White),
            border = BorderStroke(0.dp,Color.White)
        ) {
            Text(text = "Order History",
                color = Color.Black)
        }

        HorizontalDivider(modifier = Modifier
            .fillMaxWidth(),
            thickness = 2.dp,
            color = colorResource(R.color.orange_500)
        )

            Button(
                onClick = onSaveButtonClicked,
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(Color.White),
                border = BorderStroke(0.dp, Color.White)
            ) {
                Text(
                    text = "Help Center",
                    color = Color.Black
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = colorResource(R.color.orange_500)
            )

            Button(
                onClick = onSaveButtonClicked,
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(Color.White),
                border = BorderStroke(0.dp, Color.White)
            ) {
                Text(
                    text = "Become Seller",
                    color = Color.Black
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = colorResource(R.color.orange_500)
            )

            Column (verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxHeight()){
                Button(
                    onClick = onSaveButtonClicked,
                    modifier = Modifier
                        .padding(20.dp)
                        .height(50.dp)
                        .fillMaxWidth(),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.orange_500)),
                    border = BorderStroke(3.dp, Color.Red)
                ) {
                    Text(
                        text = "Log Out",
                        color = Color.Red
                    )
                }
            }
        }
    }


@Composable
fun CustomerDetailLabel(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    color: Color,
    shape: RoundedCornerShape,
    isPassword: Boolean = false,
    icon: ImageVector,
    modifier: Modifier = Modifier
){
    var showPassword by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text(label)},
        singleLine = true,
        placeholder = {Text(placeholder)},
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(unfocusedContainerColor = color),
        shape = shape,
        readOnly = true,
        visualTransformation = if (isPassword){
            if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
        } else { VisualTransformation.None },
        trailingIcon = {
                Icon(
                    icon,
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                )
        },
        modifier = modifier
    )

    if(isPassword){
        Checkbox(
            checked = false,
            onCheckedChange ={showPassword = !showPassword} )
    }
}

@Composable
fun CustomerOptionButton(
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
        //isError = value.isNullOrEmpty(),                          <--- need to do back
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun CustomerProfilePreview() {

    UniCanteenTheme {


        //val food = Datasource.foods.get(0)
//        CustomerProfileScreen(
//
//        )
    }
}