package com.example.unicanteen.database

interface UserRepository {
    suspend fun getAll():List<User>
    suspend fun insertUser(user: User): Long
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
    fun getUserById(userId: Int): User?
    fun getUserByEmail(email: String): User?
    fun getUserForLogin(userName: String, password: String): User?
    suspend fun isEmailTaken(email: String): Int
    suspend fun isUserNameTaken(userName: String): Int
    suspend fun deleteAllUsers()
}