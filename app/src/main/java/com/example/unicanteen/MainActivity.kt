package com.example.unicanteen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.unicanteen.database.AppDatabase
import com.example.unicanteen.database.User
import com.example.unicanteen.ui.theme.UniCanteenTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniCanteenTheme {
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
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)

            try {
                // Insert dummy data if the table is empty
                if (db.userDao().getAll().isEmpty()) {
                    val dummyUser = User(userName = "testuser", password = "password", name = "John Doe", email = "johndoe@example.com")
                    db.userDao().insertUser(dummyUser)
                }

                // Fetch all users
                val userList = db.userDao().getAll()
                userList.forEach {
                    Log.d("MainActivity", "User: ${it.userName}, Email: ${it.email}")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Database operation failed", e)
            }
        }

    }
}