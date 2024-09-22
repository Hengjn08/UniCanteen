package com.example.unicanteen.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.unicanteen.database.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User> // This will return a list of all users
    // Insert a new user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    // Update an existing user
    @Update
    suspend fun updateUser(user: User)

    // Delete a user
    @Delete
    suspend fun deleteUser(user: User)

    // Fetch a user by userId
    @Query("SELECT * FROM user WHERE userId = :userId")
    fun getUserById(userId: Long): User?

    // Fetch a user by email
    @Query("SELECT * FROM user WHERE email = :email")
    fun getUserByEmail(email: String): User?

    // Fetch a user by userName (for login purposes)
    @Query("SELECT * FROM user WHERE userName = :userName AND password = :password")
    fun getUserForLogin(userName: String, password: String): User?

    // Fetch all users (if needed)
    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>

    // Delete all users (useful for testing or cleanup)
    @Query("DELETE FROM user")
   fun deleteAllUsers()

    // Check if a user with a specific email exists
    @Query("SELECT COUNT(*) FROM user WHERE email = :email")
     fun isEmailTaken(email: String): Int

    // Check if a user with a specific userName exists
    @Query("SELECT COUNT(*) FROM user WHERE userName = :userName")
    fun isUserNameTaken(userName: String): Int


}