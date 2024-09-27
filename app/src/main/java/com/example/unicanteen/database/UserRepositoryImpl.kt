package com.example.unicanteen.database

import androidx.lifecycle.LiveData

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

    override fun getUserById(userId: Int): User? {
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
}