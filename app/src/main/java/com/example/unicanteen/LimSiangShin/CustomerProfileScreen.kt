package com.example.unicanteen.LimSiangShin

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.unicanteen.BottomNavigationBar
import com.example.unicanteen.model.User
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.UniCanteenTheme

object CustomerProdileDestination : NavigationDestination {
    override val route = "Seller Profile"
    override val title = ""
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

@Composable
fun CustomerProfileScreen(
    user: User? = null,
//    onCancelButtonClicked: () -> Unit = {},
    onSaveButtonClicked: () -> Unit = {},
//    navController: NavController,
//    currentDestination: NavDestination?,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
){
    var userName by remember { mutableStateOf(user?.userName?:"")}
    var email by remember { mutableStateOf(user?.email?:"") }
    var pw by remember { mutableStateOf(user?.pw?:"") }
    var confirmPw by remember { mutableStateOf("") }
//    var imageUri by remember { mutableStateOf<Uri?>(null)}

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
//        testing(
//            modifier.padding(innerPadding)
//        )
        CustomerDetailBody(
            modifier = modifier.padding(innerPadding),
            userName = userName,
            email = email,
            pw = pw,
            confirmPw = confirmPw,
//            navController = navController,
//            imageUri = imageUri,
            onUserNameChange = { userName = it },
            onEmailChange = { email = it },
            onPwChange = { pw = it },
            onConfirmPwChange = { confirmPw = it },
            onImageClick = {
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
            },
//            onCancelButtonClicked = onCancelButtonClicked,
            onSaveButtonClicked = {
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
                onSaveButtonClicked()
                Toast.makeText(context, "Register successfully!", Toast.LENGTH_SHORT).show()
            }
        )

//        BottomNavigationBar(navController = navController,
//            currentDestination = currentDestination,
//            isSeller = false)
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
fun CustomerDetailBody(
    modifier: Modifier = Modifier,
    userName: String,
    email: String,
    pw: String,
//    navController: NavController,
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
        modifier = Modifier.fillMaxSize()){

        Box(
            modifier = modifier
                .background(colorResource(R.color.orange_500))
                .height(350.dp)
                .padding(start = 20.dp, end = 20.dp, bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row (modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),

                    ){
                    Text(text = "Profile",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.white))
                }
                Spacer(modifier = Modifier.height(16.dp))

                SellerDetailLabel(
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
                    icon = R.drawable.baseline_upload_24,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                SellerDetailLabel(
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
                    icon = R.drawable.baseline_upload_24,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                SellerDetailLabel(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Password",
                    placeholder = "123456",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    color = colorResource(R.color.white),
                    shape = RoundedCornerShape(8.dp),
                    icon = R.drawable.baseline_upload_24,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }
        HorizontalDivider(modifier = Modifier
            .padding(40.dp)
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

        Button(onClick = onSaveButtonClicked,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.White),
            border = BorderStroke(0.dp,Color.White)
        ) {
            Text(text = "Help Center",
                color = Color.Black)
        }

        HorizontalDivider(modifier = Modifier
            .fillMaxWidth(),
            thickness = 2.dp,
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
            Text(text = "Become Seller",
                color = Color.Black)
        }

        HorizontalDivider(modifier = Modifier
            .fillMaxWidth(),
            thickness = 2.dp,
            color = colorResource(R.color.orange_500)
        )

        Button(onClick = onSaveButtonClicked,
            modifier = Modifier
                .padding(20.dp)
                .height(50.dp)
                .fillMaxWidth(),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(colorResource(R.color.orange_500)),
            border = BorderStroke(3.dp,Color.Red)
        ) {
            Text(text = "Log Out",
                color = Color.Red)
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
fun CustomerDetailLabel(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    color: Color,
    shape: RoundedCornerShape,
    icon: Int,
    modifier: Modifier = Modifier
){
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
        trailingIcon = {Icon(painter = painterResource(icon), contentDescription = "")},
        //isError = value.isNullOrEmpty(),                          <--- need to do back
        modifier = modifier
    )
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
        CustomerProfileScreen(navigateBack = {})
    }
}