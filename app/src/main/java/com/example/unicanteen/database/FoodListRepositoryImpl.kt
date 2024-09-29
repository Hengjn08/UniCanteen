package com.example.unicanteen.database

import androidx.lifecycle.LiveData

import com.example.unicanteen.database.FoodListDao.UpdatedFoodDetails

class FoodListRepositoryImpl( private val foodListDao: FoodListDao): FoodListRepository{
    override suspend fun insertFood(foodList: FoodList): Long{
        return foodListDao.insertFoodItem(foodList)
    }
    override suspend fun updateFood(foodList: FoodList) {
        foodListDao.updateFoodItem(foodList)
    }
    override suspend fun deleteFood(foodList: FoodList) {
        foodListDao.deleteFoodItem(foodList)
    }
    override suspend fun getFoodById(foodIdList: Int): FoodList {
        return foodListDao.getFoodItemById(foodIdList) ?: throw Exception("Food item not found")
    }
    override suspend fun getFoodsBySellerId(sellerId: Int): List<FoodList> {
        return foodListDao.getFoodItemsBySellerId(sellerId)
    }
    override suspend fun getFoodsBySellerIdAndStatus(sellerId: Int, status: String): List<FoodList> {
        return foodListDao.getFoodItemsBySellerIdAndStatus(sellerId,status)
    }
    override suspend fun getAllFoods(): List<FoodList> {
        return foodListDao.getAllFoodItems()
    }
    override suspend fun searchFoodItemsByName(sellerId: Int, query: String): List<FoodList> {
        return foodListDao.searchFoodItemsByName(sellerId, query)
    }
    override suspend fun getFoodTypeBySellerId(sellerId: Int): List<String> {
        return foodListDao.getFoodTypeBySellerId(sellerId)
    }
    override suspend fun getShopNameBySellerId(sellerId: Int): String {
        return foodListDao.getShopNameBySellerId(sellerId)
    }
    override suspend fun getFoodDetailsWithAddOns(foodId: Int): List<FoodListDao.FoodDetailsWithAddOns> {
        return foodListDao.getFoodDetailsWithAddOns(foodId)
    }

    override suspend fun updateSellerFoodDetails(updatedFoodDetails: UpdatedFoodDetails){
        return foodListDao.updateSellerFoodDetails(updatedFoodDetails)
    }
}