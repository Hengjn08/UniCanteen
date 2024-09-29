package com.example.unicanteen.LimSiangShin

import android.app.Application
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.unicanteen.SelectRestaurantDestination
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.database.UserRepository
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme

object ForgotPasswordDestination : NavigationDestination {
    override val route = "FPW?userId={userId}"
    override val title = "FPW"
    fun routeWithArgs(userId: Int): String{
        return "${route}/$userId"
    }
}


@Composable
fun ChangePasswordScreen(
    application: Application, // Pass application context
    navController: NavController,
    userRepository: UserRepository,
    modifier: Modifier = Modifier
) {

    val userViewModel: UserViewModel = viewModel(
        factory = AppViewModelProvider.Factory(application = application,userRepository = userRepository)
    )

    var email by remember { mutableStateOf("") }
    var isEmailVerified by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UniCanteenTopBar()
        }
    ) { innerPadding ->
        var confirm by remember { mutableStateOf(false) }
        var showPasswordFields by remember { mutableStateOf(false) }
        var isEmailVerified by remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Big Title
            Text(
                text = "Change Password",
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email Field
            if (!isEmailVerified) {
                EmailField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Enter your email",
                    placeholder = "Email",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    )
                )
                Text(
                    text = "Enter Your Email To verify your Account",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp)
                        .wrapContentWidth(Alignment.Start)
                )



                Spacer(modifier = Modifier.height(16.dp))

                // Button to verify email
                Button(
                    onClick = {
                        if (userViewModel.validateEmail(email)) { // Basic email validation
//                            if (userViewModel.checkEmailExist(email)) {
                                isEmailVerified = true
                                showPasswordFields = true
                                Toast.makeText(context, "$email is verified!", Toast.LENGTH_SHORT)
                                    .show()
//                            } else {
//                                Toast.makeText(context, "Email Not Exist", Toast.LENGTH_SHORT)
//                                    .show()
//                            }
                        } else {
                            Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(text = "Verify Email")
                }
            }



            Spacer(modifier = Modifier.height(16.dp))

            // Password Fields (show after OTP is verified)
            if (showPasswordFields) {

                Text(text = "Email Verified: $email", color = Color.Green)

                ChangePassword(
                    value = password,
                    onValueChange = { password = it },
                    label = "New Password",
                    placeholder = "Enter New Password",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                ChangePassword(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password",
                    placeholder = "Re-enter Password",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Submit Button
                Button(
                    onClick = {
                        confirm = true
                    }
                ) {
                    Text(text = "Submit")
                }

                if (confirm) {
                    ConfirmDialog(
                        onConfirm = {
                            if (password == confirmPassword) {
                                if (userViewModel.validatePassword(password)) {
                                    userViewModel.updateNewPassword(email, password)
                                        Toast.makeText(
                                            context,
                                            "Password changed successfully!!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate(LoginDestination.route)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Invalid password format.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Passwords do not match.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            confirm = false
                        },
                        onCancel = {
                            confirm = false
                            password = ""
                            confirmPassword = ""
                            Toast.makeText(context, "Request cancelled.", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

            }
        }
        }
    }

@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
    )
{
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        modifier = modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = "Email" )
        }
    )
}

@Composable
fun ChangePassword(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        placeholder = { Text(placeholder) },
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,

        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (showPassword) "Hide password" else "Show password"
                    )
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun ConfirmDialog(onConfirm: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* Do nothing on outside touch */ },
        title = {
            Text(text = "Confirm Password Change")
        },
        text = {
            Text("Are you sure you want to change your password?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onCancel()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    UniCanteenTheme {
//        ChangePasswordScreen()
    }
}