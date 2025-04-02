package com.example.contactapp.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper : SQLiteOpenHelper {

    constructor(context: Context) : super(context, DATABASE_NAME, null, DATABASE_VERSION)

    companion object {
        private const val DATABASE_NAME = "TLUContact"
        private const val DATABASE_VERSION = 1

        // Tên bảng và các cột
        private const val TABLE_PRODUCTS = "products"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_QUANTITY = "quantity"

        // Câu lệnh tạo bảng
        private const val CREATE_TABLE_PRODUCTS = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_PRICE REAL NOT NULL,
                $COLUMN_QUANTITY INTEGER NOT NULL DEFAULT 0
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PRODUCTS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }
}