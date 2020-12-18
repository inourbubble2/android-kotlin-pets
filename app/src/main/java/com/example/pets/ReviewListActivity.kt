package com.example.pets

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReviewListActivity : AppCompatActivity() {
    lateinit var listViewReviews : ListView

    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.review_list)

        listViewReviews = findViewById<ListView>(R.id.listViewReviews)

        dbHelper = DBHelper(this, "newdb.db", null, 1)
        database = dbHelper.writableDatabase


        var query = "SELECT * FROM review"
        var c = database.rawQuery(query,null)

        var placeIds = ArrayList<String>()
        var hospitalNames = ArrayList<String>()
        var reviews = ArrayList<String>()
        while(c.moveToNext()) {
            var placeId = c.getString(c.getColumnIndex("placeId"))
            var hospitalName = c.getString(c.getColumnIndex("hospitalName"))
            var review = c.getString(c.getColumnIndex("txt"))

            placeIds.add(placeId)
            hospitalNames.add(hospitalName)
            reviews.add(review)
        }

        val adapter = ReviewListActivity.ReviewListAdapter(this, placeIds, hospitalNames, reviews)
        listViewReviews.adapter = adapter
    }

    internal class ReviewListAdapter(var context: Context,private val placeIds: ArrayList<String>, private val hospitalNames: ArrayList<String>, private val reviews: ArrayList<String>) : BaseAdapter() {
        override fun getCount(): Int {
            return hospitalNames.size
        }

        override fun getItem(position: Int): Any {
            return 0
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View = LayoutInflater.from(context).inflate(R.layout.review_list_item, null)

            val hospitalName = view.findViewById<TextView>(R.id.hospitalName)
            val hospitalAddress = view.findViewById<TextView>(R.id.hospitalAddress)

            hospitalName.text = hospitalNames[position]
            hospitalAddress.text = reviews[position]

            return view
        }

    }
}