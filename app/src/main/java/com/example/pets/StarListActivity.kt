package com.example.pets

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StarListActivity : AppCompatActivity() {
    lateinit var listViewStars : ListView

    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.star_list)

        dbHelper = DBHelper(this, "newdb.db", null, 1)
        database = dbHelper.writableDatabase

        var placeIds = ArrayList<String>()
        var hospitalNames = ArrayList<String>()
        var hospitalAddresses = ArrayList<String>()

        var query = "SELECT * FROM star"
        Log.d("query:", query)
        var c = database.rawQuery(query,null)
        while(c.moveToNext()) {
            val placeId = c.getString(c.getColumnIndex("placeId"))
            val hospitalName = c.getString(c.getColumnIndex("hospitalName"))
            val hospitalAddress = c.getString(c.getColumnIndex("hospitalAddress"))
            placeIds.add(placeId)
            hospitalNames.add(hospitalName)
            hospitalAddresses.add(hospitalAddress)
        }

        listViewStars = findViewById<ListView>(R.id.listViewStars)
        val adapter = StarListAdapter(
            this,
            placeIds,
            hospitalNames,
            hospitalAddresses,
            database
        )
        listViewStars.adapter = adapter

    }

    class StarListAdapter(
        var context: Context,
        private val placeIds: ArrayList<String>,
        private val hospitalNames: ArrayList<String>,
        private val hospitalAddresses: ArrayList<String>,
        private val database: SQLiteDatabase
    ) : BaseAdapter() {
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
            val view: View = LayoutInflater.from(context).inflate(R.layout.hospital_list_item, null)

            val hospitalName1 = view.findViewById<TextView>(R.id.hospitalName1)
            val hospitalAddress = view.findViewById<TextView>(R.id.hospitalAddress1)
            val favorite = view.findViewById<ImageView>(R.id.favorite)

            hospitalName1.text = hospitalNames[position]
            hospitalAddress.text = hospitalAddresses[position]
            val placeId = placeIds[position]

            var exist = false
            var query = "SELECT * FROM star where placeId = '$placeId'"
            var c = database.rawQuery(query,null)
            while(c.moveToNext()) {
                Log.d("exist: ", c.getString(c.getColumnIndex("placeId")))
                exist = true
            }
            if(exist) {
                favorite.setImageResource(R.drawable.star_on)
            } else {
                favorite.setImageResource(R.drawable.star_off)
            }

            favorite.setOnClickListener{
                if(exist) {
                    favorite.setImageResource(R.drawable.star_off)
                    query = "DELETE FROM star where placeId = '$placeId'"
                    Log.d("query: ", query)
                    database.execSQL(query)
                    exist = false
                } else {
                    favorite.setImageResource(R.drawable.star_on)
                    query = "INSERT INTO star('placeId', 'hospitalName', 'hospitalAddress') values('$placeId', '${hospitalNames[position]}', '${hospitalAddresses[position]}')"
                    Log.d("query: ", query)
                    database.execSQL(query)
                    exist = true
                }
            }

            return view
        }

    }
}