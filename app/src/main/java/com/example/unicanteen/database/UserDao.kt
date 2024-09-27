package com.example.unicanteen.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
        @Query("SELECT * FROM user")
        suspend fun getAll(): List<User>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertUser(user: User): Long

        @Update
        suspend fun updateUser(user: User)

        @Delete
        suspend fun deleteUser(user: User)

        @Query("SELECT * FROM user WHERE userId = :userId")
        fun getUserById(userId: Int): User?

        @Query("SELECT * FROM user WHERE email = :email")
        suspend fun getUserByEmail(email: String): User?

        @Query("SELECT * FROM user WHERE userName = :userName AND password = :password")
        suspend fun getUserForLogin(userName: String, password: String): User?

        @Query("SELECT COUNT(*) FROM user WHERE email = :email")
        suspend fun isEmailTaken(email: String): Int

        @Query("SELECT COUNT(*) FROM user WHERE userName = :userName")
        suspend fun isUserNameTaken(userName: String): Int

        @Query("DELETE FROM user")
        suspend fun deleteAllUsers()

        @Query("SELECT sellerId FROM sellers WHERE userId = :userId")
        suspend fun checkUserIsSeller(userId: Int): Int?
}


