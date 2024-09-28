package com.example.unicanteen.LimSiangShin

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.unicanteen.data.Datasource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.database.UserRepository
import com.example.unicanteen.model.User
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme

object SellerProdileDestination : NavigationDestination {
    override val route = "seller_profile"
    override val title = "Seller Profile"
   fun routeWithArgs(sellerId: Int): String{
       return "$route/$sellerId"
   }
}

@Composable
fun SellerProfileScreen(
    application: Application, // Pass application context
    userId: Int,
    userRepository: UserRepository,
    onHelpClicked: () -> Unit,
    onLogOutClicked:()->Unit,
    onManageShopClicked: () -> Unit,
    onReportClicked:()->Unit,
    navController: NavController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
){
    val userViewModel: UserViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application, userRepository = userRepository)
    )

    var originalUserName by remember { mutableStateOf("") }
    var originalEmail by remember { mutableStateOf("") }
    var originalPhoneNumber by remember { mutableStateOf("") }
    var originalPassword by remember { mutableStateOf("") }

    var userName by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var confirmPw by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { UniCanteenTopBar() },
        bottomBar = {
            BottomNavigationBar(navController = navController,
                currentDestination = currentDestination,
                isSeller = true)
        }
    ){ innerPadding ->

        SellerDetailBody(
            userId = userId,
            viewModel = userViewModel,
            modifier = modifier.padding(innerPadding),
            userName = userName,
            email = email,
            phoneNumber = phoneNumber,
            pw = pw,
            confirmPw = confirmPw,
            onUserNameChange = { userName = it },
            onEmailChange = { email = it },
            onPhoneNumberChange = {phoneNumber = it},
            onPwChange = { pw = it },
            onConfirmPwChange = { confirmPw = it },
            onManageShopClicked = {onManageShopClicked()},
            onReportClicked = {onReportClicked()},
            onLogOutClicked = {onLogOutClicked()},
            onHelpClicked = {onHelpClicked()},
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
            update = {
                userName = userViewModel.userName.value
                email = userViewModel.email.value
                phoneNumber = userViewModel.phoneNumber.value
                pw = userViewModel.password.value
            }
        )
    }



}


@Composable
fun SellerDetailBody(
    userId: Int,
    viewModel: UserViewModel,
    modifier: Modifier = Modifier,
    userName: String,
    email: String,
    phoneNumber: String,
    pw: String,
    confirmPw: String,
    onUserNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange:(String) ->Unit,
    onPwChange: (String) -> Unit,
    onConfirmPwChange: (String) -> Unit,
    onManageShopClicked: () -> Unit,
    onReportClicked: () -> Unit,
    onLogOutClicked: () -> Unit,
    onCancelUpdate: () -> Unit,
    onUpdate: () -> Unit,
    onHelpClicked:() -> Unit,
    update:() ->Unit
) {
    LaunchedEffect(userId) {
        viewModel.updateCurrentUserDetail(userId)
        update()
    }
    var changeAvailable by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top

    ) {


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
                    text = "Seller Profile",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.white),
                    textDecoration = TextDecoration.Underline
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            DetailLabel(
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
            DetailLabel(
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

            DetailLabel(
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

            DetailLabel(
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
                onEditClicked = {
                    changeAvailable = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier
            .padding(40.dp)
            .fillMaxWidth(),
        thickness = 5.dp,
        color = colorResource(R.color.orange_500)
    )

    Button(
        onClick = { onManageShopClicked() },
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(Color.White),
        border = BorderStroke(0.dp, Color.White)
    ) {
        Text(
            text = "Manage Shop",
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
        onClick = { onReportClicked() },
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(Color.White),
        border = BorderStroke(0.dp, Color.White)
    ) {
        Text(
            text = "Shop Profit",
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
        onClick = { onHelpClicked() },
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
        onClick = {},
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

    if (changeAvailable) {
        EditDialog(
            viewModel = viewModel,
            userName = userName,
            email = email,
            phoneNumber = phoneNumber,
            password = pw,
            confirmPassword = confirmPw,
            onConfirmPasswordChange = onConfirmPwChange,
            onUserNameChange = onUserNameChange,
            onEmailChange = onEmailChange,
            onPhoneNumberChange = onPhoneNumberChange,
            onPwChange = onPwChange,
            onUpdate = {
                if (pw == confirmPw) {
                    if (viewModel.register(userName, email, pw, phoneNumber)) {
                        viewModel.updateUserDetail(userId, userName, pw, email, phoneNumber)
                        onUpdate()
                        changeAvailable = false
                        Toast.makeText(context, "Detail Changed Successful!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            },
            onCancel = {
                // Invoke the onCancelUpdate callback to reset the values
                onCancelUpdate()
                changeAvailable = false
            })
        }
}


@Preview(showBackground = true)
@Composable
fun SellerProfilePreview() {
    UniCanteenTheme {
//        //val food = Datasource.foods.get(0)
//        SellerProfileScreen1(navigateBack = {})
    }
}