package com.example.cruduts.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "BookStore.db"
        private const val DATABASE_VERSION = 2

        private const val TABLE_BOOKS = "books"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_GENRE = "genre"
        private const val COLUMN_TYPE = "type" // New column for Fiction/Non-Fiction
        private const val COLUMN_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_BOOKS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_AUTHOR TEXT,
                $COLUMN_GENRE TEXT,
                $COLUMN_TYPE TEXT,
                $COLUMN_PRICE REAL
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_BOOKS ADD COLUMN $COLUMN_TYPE TEXT")
        }
    }

    // Insert a new book
    fun insertBook(title: String, author: String, genre: String, type: String, price: Double): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_AUTHOR, author)
            put(COLUMN_GENRE, genre)
            put(COLUMN_TYPE, type)
            put(COLUMN_PRICE, price)
        }
        return db.insert(TABLE_BOOKS, null, values)
    }

    // Update a book
    fun updateBook(id: Int, title: String, author: String, genre: String, type: String, price: Double): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_AUTHOR, author)
            put(COLUMN_GENRE, genre)
            put(COLUMN_TYPE, type)
            put(COLUMN_PRICE, price)
        }
        return db.update(TABLE_BOOKS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Delete a book
    fun deleteBook(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_BOOKS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Retrieve all books
    fun getAllBooks(): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BOOKS, null, null, null, null, null, "$COLUMN_TITLE ASC"
        )
        with(cursor) {
            while (moveToNext()) {
                books.add(
                    Book(
                        id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                        title = getString(getColumnIndexOrThrow(COLUMN_TITLE)),
                        author = getString(getColumnIndexOrThrow(COLUMN_AUTHOR)),
                        genre = getString(getColumnIndexOrThrow(COLUMN_GENRE)),
                        type = getString(getColumnIndexOrThrow(COLUMN_TYPE)),
                        price = getDouble(getColumnIndexOrThrow(COLUMN_PRICE))
                    )
                )
            }
            close()
        }
        return books
    }
}