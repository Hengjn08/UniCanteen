package com.example.unicanteen.database

import androidx.lifecycle.LiveData

class AddOnRepositoryImpl(private val addOnDao: AddOnDao) : AddOnRepository {

    override suspend fun insert(addOn: AddOn) {
        addOnDao.insertAddOn(addOn)
    }

    override suspend fun getAddOnsForFood(foodId: Int): List<AddOn> {
        return addOnDao.getAddOnsForFood(foodId)
    }

    override suspend fun delete(addOn: AddOn) {
        addOnDao.deleteAddOn(addOn)
    }

    override fun getAddOnsForFoodLive(foodId: Int): LiveData<List<AddOn>> {
        return addOnDao.getAddOnsForFoodLive(foodId)
    }
}