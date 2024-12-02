package com.example.cruduts.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        writableDatabase
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME (" +
                "$COL_1 INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_2 TEXT, " +
                "$COL_3 TEXT, " +
                "$COL_4 INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(name: String, surname: String, marks: String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_2, name)
            put(COL_3, surname)
            put(COL_4, marks)
        }
        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }

    fun getAllData(): Cursor {
        val db = writableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun updateData(id: String, name: String, surname: String, marks: String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_1, id)
            put(COL_2, name)
            put(COL_3, surname)
            put(COL_4, marks)
        }
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    fun deleteData(id: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "ID = ?", arrayOf(id))
    }

    companion object {
        const val DATABASE_NAME = "Student.db"
        const val TABLE_NAME = "student_table"
        private const val DATABASE_VERSION = 1
        const val COL_1 = "ID"
        const val COL_2 = "NAME"
        const val COL_3 = "SURNAME"
        const val COL_4 = "MARKS"
    }
}
