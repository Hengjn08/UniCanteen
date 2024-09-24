package com.example.unicanteen.LimSiangShin

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.model.User
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.UniCanteenTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

object LoginDestination : NavigationDestination {
    override val route = "Login"
    override val title = ""
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

@Composable
fun LoginScreen(
//    onCancelButtonClicked: () -> Unit = {},
    onSignUpTextClicked:()->Unit = {},
    onSignInClicked: () -> Unit = {},
    navController: NavController,
    modifier: Modifier = Modifier
){
    var userName by remember { mutableStateOf("")}
    var pw by remember { mutableStateOf("") }

    val context = LocalContext.current
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        imageUri = uri
//    }

//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//            launchImagePicker(imagePickerLauncher)
//        } else {
//            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
//        }
//    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {}
    ){ innerPadding ->
        LoginBody(
            navController = navController,
            modifier = modifier.padding(innerPadding),
            userName = userName,
            pw = pw,
//            imageUri = imageUri,
            onUserNameChange = { userName = it },
            onPwChange = { pw = it },
            onSignUpTextClicked = {onSignUpTextClicked()
                navController.navigate(AddUserDestination.route)},
//            onImageClick = {
//                when (PackageManager.PERMISSION_GRANTED) {
//                    ContextCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    ) -> {
//                        launchImagePicker(imagePickerLauncher)
//                    }
//                    else -> {
//                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    }
//                }
//            },
//            onCancelButtonClicked = onCancelButtonClicked,
            onSignInClicked = {
//                val updatedUser = user?.copy(
//                    userName = userName,
//                    email = email,
//                    pw = pw,
//                ) ?: User(
//                    id = Datasource.foods.size + 1,
//                    userName = userName,
//                    email = email,
//                    pw = pw,
//                )
//                if (user == null) {
//                    Datasource.users.add(updatedUser)
//                }
                onSignInClicked()
                Toast.makeText(context, "Register successfully!", Toast.LENGTH_SHORT).show()
            }
        )
    }



}

@Composable
fun LoginBody(
    navController: NavController,
    modifier: Modifier = Modifier,
    userName: String,
    pw: String,
    onUserNameChange: (String) -> Unit,
//    onEmailChange: (String) -> Unit,
    onPwChange: (String) -> Unit,
//    onConfirmPwChange: (String) -> Unit,
//    onImageClick: () -> Unit,
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

                Button(onClick = onSignInClicked,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxSize()
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color.LightGray),
                    border = BorderStroke(1.dp,Color.Black)
                ) {
                    Text(text = "Sign In",
                        color = Color.Black,
                        fontSize = 20.sp)
                }

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
        //isError = value.isNullOrEmpty(),                          <--- need to do back
        modifier = modifier
    )
}

@Composable
fun LoginPassword(
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
fun LoginPreview() {
    UniCanteenTheme {
        //val food = Datasource.foods.get(0)
        LoginScreen(navController = rememberNavController())
    }
}