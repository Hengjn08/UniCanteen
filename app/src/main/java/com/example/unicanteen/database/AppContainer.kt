package com.example.unicanteen.database

import android.content.Context

interface AppContainer {
    val foodRepository: FoodListRepository
    val sellerRepository: SellerRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val foodRepository: FoodListRepository by lazy {
        FoodListRepositoryImpl(AppDatabase.getDatabase(context).foodListDao())
    }

    override val sellerRepository: SellerRepository by lazy{
        SellerRepositoryImpl(AppDatabase.getDatabase(context).sellerDao())
    }
}