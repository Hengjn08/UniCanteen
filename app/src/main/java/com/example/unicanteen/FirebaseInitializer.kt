package com.example.unicanteen
import android.app.Application
import com.google.firebase.FirebaseApp

class FirebaseInitializer : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}