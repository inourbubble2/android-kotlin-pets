package com.example.pets

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewReviewActivity : AppCompatActivity() {
    lateinit var textViewReview : TextView
    lateinit var buttonReturn : Button
    lateinit var hospitalName4 : TextView

    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.review_view)

        dbHelper = DBHelper(this, "newdb.db", null, 1)
        database = dbHelper.writableDatabase

        val placeId = intent.getStringExtra("placeId")
        val hospitalName = intent.getStringExtra("hospitalName")

        textViewReview = findViewById<TextView>(R.id.textViewReview)
        buttonReturn = findViewById<Button>(R.id.buttonReturn)
        hospitalName4 = findViewById<TextView>(R.id.hospitalName4)
        hospitalName4.text = hospitalName

        var query = "SELECT * FROM review where placeId = '$placeId'"
        var c = database.rawQuery(query,null)

        while(c.moveToNext()) {
            var review = c.getString(c.getColumnIndex("txt"))
            textViewReview.text = review
        }

        buttonReturn.setOnClickListener {
            finish()
        }
    }
}