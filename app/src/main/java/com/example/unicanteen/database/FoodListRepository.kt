package com.example.unicanteen.database

import com.example.unicanteen.database.FoodListDao.FoodDetailsWithAddOns

interface FoodListRepository{
    suspend fun insertFood(foodList: FoodList): Long
    suspend fun updateFood(foodList: FoodList)
    suspend fun deleteFood(foodList: FoodList)
    suspend fun getFoodById(foodIdList: Int): FoodList
    suspend fun getFoodsBySellerId(sellerId: Int): List<FoodList>
    suspend fun getAllFoods(): List<FoodList>
    suspend fun searchFoodItemsByName(sellerId: Int, query: String): List<FoodList>
    suspend fun getFoodDetailsWithAddOns(foodId: Int): List<FoodDetailsWithAddOns>
    suspend fun updateSellerFoodDetails(updatedFoodDetails: FoodListDao.UpdatedFoodDetails)


}
