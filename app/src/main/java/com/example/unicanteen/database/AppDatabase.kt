package com.example.unicanteen.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        User::class,
        Seller::class,
        FoodList::class,
        Order::class,
        OrderList::class,
        Payment::class,
        PaymentDetails::class,
        AddOn::class
    ],
    version = 1 // Incremented version number
)
@TypeConverters(Converters::class) // Correct placement of TypeConverters annotation
abstract class AppDatabase : RoomDatabase() {

    // Abstract methods to access DAOs
    abstract fun userDao(): UserDao
    abstract fun orderListDao(): OrderListDao
    abstract fun sellerDao(): SellerDao
    abstract fun foodListDao(): FoodListDao
    abstract fun orderDao(): OrderDao
    abstract fun paymentDao(): PaymentDao
    abstract fun paymentDetailsDao(): PaymentDetailsDao
    abstract fun addOnDao(): AddOnDao

//    abstract fun foodListDao(): FoodListDao
//    abstract fun orderDao(): OrderDao
//    abstract fun orderListDao(): OrderListDao
//    abstract fun paymentDao(): PaymentDao
//    abstract fun paymentDetailsDao(): PaymentDetailsDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

//        // Migration strategy from version 1 to 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Perform necessary changes in the database schema.
                // For example, if you're adding a new column, you can run SQL here.
                // Example: database.execSQL("ALTER TABLE User ADD COLUMN age INTEGER DEFAULT 0")
                // Modify it as needed based on the actual schema changes.
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "testP2_database"
                )
                    .createFromAsset("Database/uniDatabase.db") // Load from assets

                    .addMigrations(MIGRATION_1_2) // Add migration to handle schema changes
                //    .fallbackToDestructiveMigration() // Use destructive migration during development

                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
