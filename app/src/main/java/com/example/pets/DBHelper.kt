package com.example.pets

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {


    override fun onCreate(db: SQLiteDatabase) {
        var sql : String = "CREATE TABLE if not exists review (" +
                "placeId text primary key," +
                "hospitalName text," +
                "txt text);";
        db.execSQL(sql)

        sql = "CREATE TABLE if not exists star (" +
                "placeId text primary key," +
                "hospitalName text," +
                "hospitalAddress text);"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        var sql = "DROP TABLE if exists review"
        db.execSQL(sql)
        sql = "DROP TABLE if exists star"
        db.execSQL(sql)
        onCreate(db)
    }

    fun reset(db: SQLiteDatabase) {
        var sql = "DROP TABLE if exists review"
        db.execSQL(sql)
        sql = "DROP TABLE if exists star"
        db.execSQL(sql)
        onCreate(db)
    }

}