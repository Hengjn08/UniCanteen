package com.example.unicanteen.HengJunEn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unicanteen.UniCanteenTopBar
import com.example.unicanteen.navigation.NavigationDestination
import com.example.unicanteen.ui.theme.UniCanteenTheme

object EditFoodDestination : NavigationDestination {
    override val route = "edit_food"
    override val title = "Edit Food"
    const val foodIdArg = "foodId"
    val routeWithArgs = "$route/{$foodIdArg}"
}

@Composable
fun EditFoodScreen(

    navigateBack: () -> Unit,
){
    Scaffold (
        topBar = {
            UniCanteenTopBar(
            )
        }
    ) {
            innerPadding ->
        EditFoodBody(
            modifier = Modifier.padding(innerPadding),
            navigateBack = navigateBack,
        )
    }
}

@Composable
fun EditFoodBody(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
){
    Column (
        modifier = modifier.verticalScroll(rememberScrollState())
    ){
        NavigateBackIconWithTitle(
            title = EditFoodDestination.title ,
            navigateBack = navigateBack,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditFoodPreview() {
    UniCanteenTheme {
        EditFoodScreen(
            navigateBack = {}
        )
    }
}