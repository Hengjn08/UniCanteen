package com.example.unicanteen.HengJunEn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                //canNavigateBack = true,
                //navigateUp = navigateBack,
            )
        }
    ) {
            innerPadding ->
        EditFoodBody(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun EditFoodBody(
    modifier: Modifier = Modifier
){
    Column (
        modifier = modifier
    ){
        Text(
            text = "Edit food"
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