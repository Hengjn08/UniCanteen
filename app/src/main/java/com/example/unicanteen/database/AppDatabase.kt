package com.example.unicanteen.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = arrayOf(
        User::class,
        Seller::class,
        FoodList::class,
        Order::class,
        OrderList::class,
        Payment::class,
        PaymentDetails::class
    ),
    version = 1, // Increment version if needed
   // exportSchema = false
)
@TypeConverters(Converters::class) // Correct placement of TypeConverters annotation
abstract class AppDatabase : RoomDatabase() {

    // Abstract methods to access DAOs
    abstract fun userDao(): UserDao
    abstract fun sellerDao(): SellerDao
    abstract fun foodListDao(): FoodListDao
    abstract fun orderDao(): OrderDao
    abstract fun orderListDao(): OrderListDao
    abstract fun paymentDao(): PaymentDao
    abstract fun paymentDetailsDao(): PaymentDetailsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .createFromAsset("Database/uniDatabase.db") // Ensure this file is in the assets folder
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}