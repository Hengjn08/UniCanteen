package com.example.assgntmobile0921.database

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey


@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int,
    @ColumnInfo(name = "userName") val userName: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "phoneNumber") val phoneNumber: String? = null // Nullable field
)