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
import androidx.compose.ui.graphics.RectangleShape
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

object OrderHistoryDestination : NavigationDestination {
    override val route = "OrderHistory"
    override val title = ""
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

@Composable
fun OrderHistoryScreen(
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
        topBar = { UniCanteenTopBar()}
    ){ innerPadding ->
        OrderHistoryBody(
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
fun OrderHistoryBody(
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
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp)){
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(start = 150.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Text(text = "History",)

            IconButton(onClick = { /*TODO*/ },modifier = Modifier.padding(start = 100.dp)) {
                Icon(painter = painterResource(R.drawable.baseline_upload_24), contentDescription = "")
            }
        }

        OrderHistoryLabel(
            date = "2024 JUN 20",
            time = "12:44a.m.",
            totalAmount = 22.20

        )
    }
}

@Composable
fun OrderHistoryLabel(
    date: String,
    time: String,
    totalAmount: Double
){
    Column {
        Text(text = date)
        HorizontalDivider(thickness = 1.dp)
        OrderHistoryDetail(time,totalAmount)
    }
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = {Text(label)},
//        singleLine = true,
//        placeholder = {Text(placeholder)},
//        keyboardOptions = keyboardOptions,
//        colors = TextFieldDefaults.colors(unfocusedContainerColor = color,
//            focusedContainerColor = color,
//            focusedLabelColor = color),
//        shape = shape,
//        //isError = value.isNullOrEmpty(),                          <--- need to do back
//        modifier = modifier
//    )
}

@Composable
fun OrderHistoryDetail(
    time: String,
    totalAmount: Double
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: String,
//    placeholder: String,
//    keyboardOptions: KeyboardOptions,
//    color: Color,
//    shape: RoundedCornerShape,
//    visualTransformation: PasswordVisualTransformation,
//    modifier: Modifier = Modifier
){
    Button(onClick = { /*TODO*/ },
        shape = RectangleShape,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(colorResource(R.color.orange_500))) {
        Row (modifier = Modifier.fillMaxWidth()){
            Column (modifier = Modifier,
                horizontalAlignment = Alignment.Start) {
                Text(text = "Time : $time")
                Text(text = "Amount : RM $totalAmount")
            }
            Column (modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End){
                Icon(painter = painterResource(R.drawable.baseline_upload_24), contentDescription ="" )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderHistoryPreview() {
    UniCanteenTheme {
        //val food = Datasource.foods.get(0)
        OrderHistoryScreen(navController = rememberNavController())
    }
}