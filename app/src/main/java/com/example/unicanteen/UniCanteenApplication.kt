package com.example.unicanteen

import android.app.Application
import com.example.unicanteen.database.AppContainer
import com.example.unicanteen.database.AppDataContainer

class UniCanteenApplication : Application() {
    val container: AppContainer by lazy {
        AppDataContainer(this)
    }

    override fun onCreate() {
        super.onCreate()
        // You don't need to manually initialize container anymore, lazy takes care of it
    }
}