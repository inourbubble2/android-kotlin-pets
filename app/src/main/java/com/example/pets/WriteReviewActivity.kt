package com.example.pets

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WriteReviewActivity :AppCompatActivity() {
    lateinit var buttonWrite : Button
    lateinit var editTextReview : EditText
    lateinit var hospitalName3 : TextView

    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.review_write)

        dbHelper = DBHelper(this, "newdb.db", null, 1)
        database = dbHelper.writableDatabase

        val placeId = intent.getStringExtra("placeId")
        val hospitalName = intent.getStringExtra("hospitalName")

        buttonWrite = findViewById<Button>(R.id.buttonWrite)
        editTextReview = findViewById<EditText>(R.id.editTextReview)
        hospitalName3 = findViewById<TextView>(R.id.hospitalName3)
        hospitalName3.text = hospitalName

        buttonWrite.setOnClickListener {
            var review = editTextReview.text.toString()
            var query = "INSERT INTO review('placeid', 'hospitalName', 'txt') values('$placeId','$hospitalName', '$review');"
            database.execSQL(query)

            Log.d("query : ", query)
            finish()
        }

    }
}