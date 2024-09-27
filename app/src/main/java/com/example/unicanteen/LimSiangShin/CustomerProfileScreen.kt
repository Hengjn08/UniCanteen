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
import androidx.compose.material3.AlertDialog
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
import java.lang.Error

object CustomerProfileDestination : NavigationDestination {
    override val route = "User Profile"
    override val title = ""
    const val userIdArg = "userId"
    fun routeWithArgs(userId: Int): String{
        return "$route/$userId"
    }
}

@Composable
fun CustomerProfileScreen(
    userId: Int,
    userRepository: UserRepository,
//    sellerRepository: SellerRepository,
    onSaveButtonClicked: () -> Unit = {},
    navController: NavController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
){
    val userViewModel: UserViewModel = viewModel(
        factory = AppViewModelProvider.Factory(userRepository = userRepository)
    )

    val currentUserName by userViewModel.userName.collectAsState()
    val currentEmail by userViewModel.email.collectAsState()
    val currentPhoneNumber by userViewModel.phoneNumber.collectAsState()
    val currentPassword by userViewModel.password.collectAsState()

    // Keep a copy of the original values
    var originalUserName by remember { mutableStateOf(currentUserName) }
    var originalEmail by remember { mutableStateOf(currentEmail) }
    var originalPhoneNumber by remember { mutableStateOf(currentPhoneNumber) }
    var originalPassword by remember { mutableStateOf(currentPassword) }

    var userName by remember { mutableStateOf(currentUserName) }
    var email by remember { mutableStateOf(currentEmail) }
    var pw by remember { mutableStateOf(currentPassword) }
    var confirmPw by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf(currentPhoneNumber) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

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
            userId = userId,
            viewModel = userViewModel,
            modifier = modifier.padding(innerPadding),
            userName = userName,
            email = email,
            password = pw,
            phoneNumber = phoneNumber,
            confirmPassword = confirmPw,
            onUserNameChange = { userName = it },
            onEmailChange = { email = it },
            onPwChange = { pw = it },
            onPhoneNumberChange = { phoneNumber = it },
            onConfirmPwChange = { confirmPw = it },
            onSaveButtonClicked = {
                onSaveButtonClicked()
                Toast.makeText(context, "Register successfully!", Toast.LENGTH_SHORT).show()
            },
            onOrderHistoryClicked = {},
            onUpdate = {
                // Update the original values with the new ones on Save
                originalUserName = userName
                originalEmail = email
                originalPhoneNumber = phoneNumber
                originalPassword = pw
            },
            onCancelUpdate = {
                // Reset all values to their original state when the user cancels
                userName = originalUserName
                email = originalEmail
                phoneNumber = originalPhoneNumber
                pw = originalPassword
                confirmPw = ""
            },
            onEditClicked = {}
        )
    }
}



@Composable
fun CustomerDetailBody(
    userId: Int,
    viewModel: UserViewModel,
    modifier: Modifier = Modifier,
    userName: String,
    email: String,
    password: String,
    phoneNumber: String,
    confirmPassword: String,
//    navController: NavController,
    onUserNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPwChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSaveButtonClicked: () -> Unit,
    onOrderHistoryClicked: () -> Unit,
    onConfirmPwChange: (String) -> Unit,
    onCancelUpdate: () -> Unit,
    onUpdate: () -> Unit,
    onEditClicked: () -> Unit
) {
    LaunchedEffect(userId) {
        viewModel.updateCurrentUserDetail(userId)
    }


    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top

    ){
        var changeAvailable by rememberSaveable { mutableStateOf(false) }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .padding(0.dp)
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
                    color = colorResource(R.color.white),
                    textDecoration = TextDecoration.Underline
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
                onEditClicked = {
                    changeAvailable = true
                },
                modifier = Modifier
                    .fillMaxWidth()
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
                onEditClicked = {
                    changeAvailable = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
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
                onEditClicked = {
                    changeAvailable = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            CustomerDetailLabel(
                value = password,
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
                onEditClicked = {
                    changeAvailable = true
                                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
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
            onClick = onOrderHistoryClicked,
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
                    color = Color.Red,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }


        if(changeAvailable){
            EditDialog(
                viewModel = viewModel,
                userName = userName,
                email = email,
                phoneNumber = phoneNumber,
                password = password,
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = onConfirmPwChange,
                onUserNameChange = onUserNameChange,
                onEmailChange = onEmailChange,
                onPhoneNumberChange = onPhoneNumberChange,
                onPwChange = onPwChange,
                onUpdate = {
                    if(viewModel.validateRegistrationForm(userName, password, email, phoneNumber)){
                        viewModel.updateUserDetail(userId,userName, password, email, phoneNumber)
                        onUpdate()
                        changeAvailable = false
                        Toast.makeText(context, "Detail Changed Successful!", Toast.LENGTH_SHORT).show()
                    }
                },
                onCancel = {
                    // Invoke the onCancelUpdate callback to reset the values
                    onCancelUpdate()
                    changeAvailable = false
                })
        }
    }
}


@Composable
fun CustomerDetailLabel(
    value: String,
    onValueChange: (String) -> Unit,
    onEditClicked: () -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    color: Color,
    shape: RoundedCornerShape,
    isPassword: Boolean = false,
    icon: ImageVector,
    modifier: Modifier = Modifier,
){
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var check by rememberSaveable { mutableStateOf(false) }

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
                    .clickable {
                        onEditClicked()
                    }
            )
        },
        modifier = modifier
    )

    if(isPassword) {
        Row(verticalAlignment = Alignment.CenterVertically){
            Checkbox(
                checked = check,
                onCheckedChange = {
                    showPassword = !showPassword
                    check = !check
                }
            )
            Text(text = if (showPassword)"Hide Password"  else "Show Password")
        }
    }
}

@Composable
fun EditDialog(
    viewModel: UserViewModel,
    userName: String,
    email: String,
    phoneNumber: String,
    password: String,
    confirmPassword:String,
    onUserNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPwChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onConfirmPasswordChange:(String) -> Unit,
    onUpdate: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onCancel() },
        title = { Text("Edit Detail") },
        text = {
            Column {
                dialogText(
                    value = userName,
                    onValueChange = onUserNameChange,
                    modifier = Modifier.padding(top = 8.dp),
                    label = "User Name",
                    isError = viewModel.userNameError.isNotEmpty(),
                    errorMessage = viewModel.userNameError
                )

                dialogText(
                    value = email,
                    onValueChange = onEmailChange,
                    modifier = Modifier.padding(top = 8.dp),
                    label = "Email Address",
                    isError = viewModel.emailError.isNotEmpty(),
                    errorMessage = viewModel.emailError
                )

                dialogText(
                    value = phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    modifier = Modifier.padding(top = 8.dp),
                    label = "Phone Number",
                    isError = viewModel.phoneNumberError.isNotEmpty(),
                    errorMessage = viewModel.phoneNumberError
                )

                dialogText(
                    value = password,
                    onValueChange = onPwChange,
                    modifier = Modifier.padding(top = 8.dp),
                    label = "Password",
                    isError = viewModel.passwordError.isNotEmpty(),
                    errorMessage = viewModel.passwordError
                )

                dialogText(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    modifier = Modifier.padding(top = 8.dp),
                    label = "Confirm Password",
                    isError = viewModel.passwordError.isNotEmpty(),
                    errorMessage = viewModel.passwordError
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onUpdate()
            }) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(onClick = { onCancel() }) {
                Text("Cancel")
            }
        },
    )
}

@Composable
fun dialogText(
    errorMessage: String,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier,
    isError: Boolean
){
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) }
        )
    if(isError){
        Text(text = errorMessage,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 2.dp),
            textAlign = TextAlign.Start
        )
    }else{

    }
}



@Preview(showBackground = true)
@Composable
fun CustomerProfilePreview() {
    UniCanteenTheme {}
}