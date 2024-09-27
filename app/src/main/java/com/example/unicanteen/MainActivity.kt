package com.example.unicanteen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.unicanteen.database.AppDatabase
import com.example.unicanteen.ui.theme.UniCanteenTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        GlobalScope.launch {
            AppDatabase.getDatabase(applicationContext).userDao().getAll()
//            AppDatabase.getDatabase(applicationContext).sellerDao().getAllSellers()
//            AppDatabase.getDatabase(applicationContext).foodListDao().getAllFoodItems()
//            AppDatabase.getDatabase(applicationContext).orderDao().getAllOrders()
//            AppDatabase.getDatabase(applicationContext).orderListDao().getAllOrderListItems()
//            AppDatabase.getDatabase(applicationContext).paymentDao().getAllPayments()
//            AppDatabase.getDatabase(applicationContext).paymentDetailsDao().getAllPaymentDetails()

        }
        setContent {
            val darkTheme = isSystemInDarkTheme() // Detect system theme
            UniCanteenTheme(
                darkTheme = darkTheme
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ){
                    UniCanteenApp()
                }
            }
        }
        // Using lifecycleScope to access the database


    }
}