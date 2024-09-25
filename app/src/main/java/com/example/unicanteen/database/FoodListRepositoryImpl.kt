package com.example.unicanteen.database

class FoodListRepositoryImpl(private val foodListDao: FoodListDao) : FoodListRepository {

    override suspend fun insertFoodItem(foodItem: FoodList): Long{
        return foodListDao.insertFoodItem(foodItem)
    }
    override suspend fun updateFoodItem(foodItem: FoodList){
        return foodListDao.updateFoodItem(foodItem)
    }
    override suspend fun deleteFoodItem(foodItem: FoodList){
        return foodListDao.deleteFoodItem(foodItem)
    }
    override fun getFoodItemById(foodId: Long): FoodList?{
        return foodListDao.getFoodItemById(foodId)
    }
    override fun getFoodItemsBySellerId(sellerId: Long): List<FoodList>{
        return foodListDao.getFoodItemsBySellerId(sellerId)
    }
    override fun getAvailableFoodItems(): List<FoodList>{
        return foodListDao.getAvailableFoodItems()
    }
    override fun getFoodItemsByType(type: String): List<FoodList>{
        return foodListDao.getFoodItemsByType(type)
    }
    override fun getFoodItemsByPriceRange(minPrice: Double, maxPrice: Double): List<FoodList>{
        return foodListDao.getFoodItemsByPriceRange(minPrice, maxPrice)
    }
    override fun getFoodItemsWithHighRating(rating: Double): List<FoodList>{
        return foodListDao.getFoodItemsWithHighRating(rating)
    }
    override fun getAllFoodItems(): List<FoodList>{
        return foodListDao.getAllFoodItems()
    }
}