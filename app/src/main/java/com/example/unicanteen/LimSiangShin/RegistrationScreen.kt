package com.example.unicanteen.LimSiangShin

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.unicanteen.R
import com.example.unicanteen.data.Datasource
import com.example.unicanteen.model.Food
import coil.compose.AsyncImage
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.database.UserRepository
import com.example.unicanteen.model.User
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.AppViewModelProvider
import com.example.unicanteen.ui.theme.UniCanteenTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

object AddUserDestination : NavigationDestination {
    override val route = "Registration"
    override val title = ""
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

@Composable
fun RegistrationScreen(
    user: User? = null,
//    onCancelButtonClicked: () -> Unit = {},
    onSaveButtonClicked: () -> Unit = {},
    navController: NavController,
    userRepository: UserRepository,
    modifier: Modifier = Modifier
){
    val viewModel: UserViewModel = viewModel(
        factory = AppViewModelProvider.Factory(userRepository = userRepository)
    )
    var userName by remember { mutableStateOf(user?.userName?:"")}
    var email by remember { mutableStateOf(user?.email?:"") }
    var pw by remember { mutableStateOf(user?.pw?:"") }
    var confirmPw by remember { mutableStateOf("") }
//    var imageUri by remember { mutableStateOf<Uri?>(null)}

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {}
    ){ innerPadding ->
        AddUserDetailBody(
            modifier = modifier.padding(innerPadding),
            userName = userName,
            email = email,
            pw = pw,
            confirmPw = confirmPw,
//            imageUri = imageUri,
            onUserNameChange = { userName = it },
            onEmailChange = { email = it },
            onPwChange = { pw = it },
            onConfirmPwChange = { confirmPw = it },
            onImageClick = {},
//            onCancelButtonClicked = onCancelButtonClicked,
            onSaveButtonClicked = {
                if (pw == confirmPw) {

                    onSaveButtonClicked()
                    Toast.makeText(context, "Register successfully!", Toast.LENGTH_SHORT).show()
                }else{
                    pw = ""
                    confirmPw = ""
                    Toast.makeText(context, "Password is Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }



}

//@Composable
//fun testing(
//    modifier: Modifier = Modifier
//){
//    Column(
//        modifier = modifier
//    ) {
//        Text(text = "testing")
//    }
//}

@Composable
fun AddUserDetailBody(
    modifier: Modifier = Modifier,
    userName: String,
    email: String,
    pw: String,
    confirmPw: String,
//    imageUri: Uri?,
    onUserNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPwChange: (String) -> Unit,
    onConfirmPwChange: (String) -> Unit,
    onImageClick: () -> Unit,
//    onCancelButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center){

        Box(
            modifier = modifier
                .background(colorResource(R.color.orange_500), RoundedCornerShape(70.dp))
                .height(640.dp)
                .padding(start = 20.dp, end = 20.dp, bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier.verticalScroll(rememberScrollState()),
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
                        .padding(bottom = 16.dp)
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
                        .padding(bottom = 16.dp)
                )

                PasswordField(
                    value = confirmPw,
                    onValueChange = onConfirmPwChange,
                    label = "Confirm Password",
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
                        .padding(bottom = 16.dp)
                )

                Button(onClick = onSaveButtonClicked,
                    modifier = Modifier
                        .padding(top = 40.dp)
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

//fun launchImagePicker(launcher: ActivityResultLauncher<String>) {
//    launcher.launch("image/*")
//}

//@Composable
//fun ImageUploadBox(
//    imageUri: Uri?,
//    onImageClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .height(200.dp)
//            .fillMaxWidth()
//            .background(Color.LightGray, RoundedCornerShape(8.dp))
//            .clickable(onClick = onImageClick),
//        contentAlignment = Alignment.Center
//    ) {
//        if (imageUri != null) {
//            AsyncImage(
//                model = imageUri,
//                contentDescription = "Selected food image",
//                modifier = Modifier
//                    .fillMaxSize()
//                    .clip(RoundedCornerShape(8.dp)),
//                contentScale = ContentScale.Crop
//            )
//        } else {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.baseline_upload_24),
//                    contentDescription = "Upload icon",
//                    modifier = Modifier.size(50.dp),
//                    tint = Color.Gray
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = "Upload picture",
//                    color = Color.Gray
//                )
//            }
//        }
//    }
//}
@Composable
fun EditTextField(
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
        //isError = value.isNullOrEmpty(),                          <--- need to do back
        modifier = modifier
    )
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
        //isError = value.isNullOrEmpty(),                          <--- need to do back
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun RegistrationPreview() {
    UniCanteenTheme {
        //val food = Datasource.foods.get(0)
//        RegistrationScreen(navigateBack = {})
    }
}