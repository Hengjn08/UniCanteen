package com.example.unicanteen.database

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
    override suspend fun getAllFoods(): List<FoodList> {
        return foodListDao.getAllFoodItems()
    }
    override suspend fun searchFoodItemsByName(sellerId: Int, query: String): List<FoodList> {
        return foodListDao.searchFoodItemsByName(sellerId, query)
    }


}