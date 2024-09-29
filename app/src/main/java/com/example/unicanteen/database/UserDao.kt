package com.example.unicanteen.database

import androidx.lifecycle.LiveData
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
        suspend fun getUserById(userId: Int): User?

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

        @Query("UPDATE user SET userName = :userName, email = :email,phoneNumber = :phoneNumber,password = :password WHERE userId = :userId")
        suspend fun updateUserDetail(userName: String,email: String, phoneNumber: String, password: String, userId: Int)

        @Query("UPDATE user SET email = :email WHERE userId = :userId")
        suspend fun updateUserEmail(email: String,userId: Int)

        @Query("UPDATE user SET phoneNumber = :phoneNumber WHERE userId = :userId")
        suspend fun updateUserPhoneNumber(phoneNumber: String,userId: Int)

        @Query("UPDATE user SET password = :password WHERE userId = :userId")
        suspend fun updateUserPassword(password: String,userId: Int)

        @Query("SELECT userName FROM user WHERE userId =:userId")
        suspend fun getUserName(userId: Int?) : String

        @Query("SELECT email FROM user WHERE userId =:userId")
        suspend fun getEmail(userId: Int?): String

        @Query("SELECT phoneNumber FROM user WHERE userId =:userId")
        suspend fun getPhoneNumber(userId: Int?): String

        @Query("SELECT password FROM user WHERE userId =:userId")
        suspend fun getPassword(userId: Int?): String

        @Query("""
        SELECT o.orderId AS orderId,
               o.createDate AS createDate,
               o.totalPrice AS totalAmount
        FROM orders o
        WHERE o.userId = :userId
        ORDER BY o.createDate DESC
    """) fun getOrderDetailsByOrderId(userId: Int): LiveData<List<OrderDetails>>

        data class OrderDetails(
                val orderId: Int,
                val createDate: String,
                val totalAmount: Double
        )
}


