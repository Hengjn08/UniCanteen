package com.example.unicanteen.database

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.unicanteen.database.UserDao.OrderDetails

class UserRepositoryImpl(private val userDao: UserDao): UserRepository {

    override suspend fun getAll(): List<User>{
        return userDao.getAll()
    }

    override suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        return userDao.updateUser(user)
    }

    override suspend fun deleteUser(user: User) {
        return userDao.deleteUser(user)
    }

    override suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    override suspend fun getUserForLogin(userName: String, password: String): User? {
        return userDao.getUserForLogin(userName,password)
    }

    override suspend fun isEmailTaken(email: String): Int {
        return userDao.isEmailTaken(email)
    }

    override suspend fun isUserNameTaken(userName: String): Int {
        return userDao.isUserNameTaken(userName)
    }

    override suspend fun deleteAllUsers() {
        return userDao.deleteAllUsers()
    }

    override suspend fun checkUserIsSeller(userId: Int): Int? {
        return userDao.checkUserIsSeller(userId)
    }

    override suspend fun updateUserDetail(
        userName: String,
        email: String,
        phoneNumber: String,
        password: String,
        userId: Int
    ) {
        return  userDao.updateUserDetail(userName,email, phoneNumber, password, userId)
    }


    override suspend fun updateUserEmail(email: String, userId: Int) {
        return userDao.updateUserEmail(email,userId)
    }

    override suspend fun updateUserPhoneNumber(phoneNumber: String, userId: Int) {
        return userDao.updateUserPhoneNumber(phoneNumber,userId)
    }

    override suspend fun updateUserPassword(password: String, userId: Int) {
        return userDao.updateUserPassword(password,userId)
    }

    override suspend fun getUserName(userId: Int?): String {
        return userDao.getUserName(userId)
    }

    override suspend fun getEmail(userId: Int?) : String{
        return userDao.getEmail(userId)
    }

    override suspend fun getPhoneNumber(userId: Int?): String {
        return userDao.getPhoneNumber(userId)
    }

    override suspend fun getPassword(userId: Int?): String {
        return userDao.getPassword(userId)
    }

    override suspend fun checkUserEmail(email: String): Int {
        return userDao.checkUserEmail (email)
    }

    override suspend fun getOrderDetailsByUserId(userId: Int): List<OrderDetails>{
        return userDao.getOrderDetailsByUserId(userId)
    }

}