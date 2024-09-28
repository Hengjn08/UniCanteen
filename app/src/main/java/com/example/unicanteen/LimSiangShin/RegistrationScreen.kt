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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unicanteen.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.unicanteen.database.SellerRepository
import com.example.unicanteen.database.UserRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme

object AddUserDestination : NavigationDestination {
    override val route = "Registration"
    override val title = ""
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

@Composable
fun RegistrationScreen(
    application: Application, // Pass application context
    userRepository: UserRepository,
    navController: NavController,
//    sellerRepository: SellerRepository,
    onRegisterButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    val viewModel: UserViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,userRepository = userRepository))

    var userName by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var confirmPw by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {}
    ){ innerPadding ->
        AddUserDetailBody(
            viewModel = viewModel,
            modifier = modifier.padding(innerPadding),
            userName = userName,
            email = email,
            pw = pw,
            phoneNumber = phoneNumber,
            confirmPw = confirmPw,
            onUserNameChange = { userName = it },
            onEmailChange = { email = it },
            onPwChange = { pw = it },
            onConfirmPwChange = { confirmPw = it },
            onPhoneNumberChange = { phoneNumber = it},
            onRegisterButtonClicked = {
                if(viewModel.validateRegistrationForm(userName,email,phoneNumber,pw)){
                    if (pw == confirmPw) {
                            if (viewModel.register(userName, email, pw, phoneNumber)) {
                                Toast.makeText(context, "Register successfully!", Toast.LENGTH_SHORT).show()
                                // Registration successful, navigate to next screen
                                navController.navigate(LoginDestination.route)
                            } else {
                                // Show error message to the user
                                    Toast.makeText(context, "Email exist.", Toast.LENGTH_SHORT).show()
                                }
                    }else{
                        pw = ""
                        confirmPw = ""
                        Toast.makeText(context, "Password is Wrong", Toast.LENGTH_SHORT).show()
                    }
                }else{

                }
            }
        )
    }
}

@Composable
fun AddUserDetailBody(
    viewModel: UserViewModel,
    modifier: Modifier = Modifier,
    userName: String,
    email: String,
    pw: String,
    confirmPw: String,
    phoneNumber: String,
    onUserNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPwChange: (String) -> Unit,
    onConfirmPwChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onRegisterButtonClicked: () -> Unit,
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center){

        Column(
            modifier = modifier
                .background(colorResource(R.color.orange_500), RoundedCornerShape(70.dp))
                .padding(start = 20.dp, end = 20.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = modifier.verticalScroll(rememberScrollState()).padding(top = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row (modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 48.dp),
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


                EditTextField(
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
                        .padding(top = 16.dp),
                    isError = viewModel.userNameError.isNotEmpty(),
                    errorMessage = viewModel.userNameError
                )


                EditTextField(
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    isError = viewModel.emailError.isNotEmpty(),
                    errorMessage = viewModel.emailError
                )

                EditTextField(
                    value = phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    label = "phoneNumber",
                    placeholder = "Phone Number",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    color = colorResource(R.color.white),
                    shape = RoundedCornerShape(8.dp),
                    isError = viewModel.phoneNumberError.isNotEmpty(),
                    errorMessage = viewModel.phoneNumberError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                EditTextField(
                    value = pw,
                    onValueChange = onPwChange,
                    label = "Password",
                    placeholder = "",
                    isPassword = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    color = colorResource(R.color.white),
                    shape = RoundedCornerShape(8.dp),
                    isError = viewModel.passwordError.isNotEmpty(),
                    errorMessage = viewModel.passwordError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                EditTextField(
                    value = confirmPw,
                    onValueChange = onConfirmPwChange,
                    label = "Confirm Password",
                    placeholder = "",
                    isPassword = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    color = colorResource(R.color.white),
                    shape = RoundedCornerShape(8.dp),
                    isError = viewModel.passwordError.isNotEmpty(),
                    errorMessage = viewModel.passwordError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                Button(onClick = onRegisterButtonClicked,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxSize()
                        .height(70.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color.LightGray),
                    border = BorderStroke(1.dp,Color.Black)
                ) {
                    Text(text = "Sign Up",
                        color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun EditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    color: Color,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    isError: Boolean,
    errorMessage: String,
    isPassword: Boolean = false
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
        shape = shape,
        modifier = modifier,
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
    )
    if (isError){
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 2.dp),
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationPreview() {
    UniCanteenTheme {
        //val food = Datasource.foods.get(0)
//        RegistrationScreen(navigateBack = {})
    }
}