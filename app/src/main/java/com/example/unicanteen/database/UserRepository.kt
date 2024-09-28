package com.example.unicanteen.database

import androidx.room.Query

interface UserRepository {
    suspend fun getAll():List<User>
    suspend fun insertUser(user: User): Long
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun getUserById(userId: Int): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserForLogin(userName: String, password: String): User?
    suspend fun isEmailTaken(email: String): Int
    suspend fun isUserNameTaken(userName: String): Int
    suspend fun deleteAllUsers()
    suspend fun checkUserIsSeller(userId: Int): Int?
    suspend fun updateUserDetail(userName: String,email: String, phoneNumber: String, password: String, userId: Int)    suspend fun updateUserEmail(email: String,userId: Int)
    suspend fun updateUserPhoneNumber(phoneNumber: String,userId: Int)
    suspend fun updateUserPassword(password: String,userId: Int)
}