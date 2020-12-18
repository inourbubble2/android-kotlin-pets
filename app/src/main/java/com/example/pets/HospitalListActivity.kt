package com.example.pets

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.net.URI

class HospitalListActivity: AppCompatActivity() {
    lateinit var listViewHospitals : ListView
    lateinit var textViewLocation : TextView

    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hospital_list)

        dbHelper = DBHelper(this, "newdb.db", null, 1)
        database = dbHelper.writableDatabase

        listViewHospitals = findViewById<ListView>(R.id.listViewHospitals)
        textViewLocation = findViewById<TextView>(R.id.textViewLocation)

        val placeIds = intent.getStringArrayExtra("placeIds")
        val hospitalNames = intent.getStringArrayExtra("hospitalNames")
        val hospitalAddresses = intent.getStringArrayExtra("hospitalAddresses")
        val lats = intent.getDoubleArrayExtra("lats")
        val lngs = intent.getDoubleArrayExtra("lngs")
        val location = intent.getStringExtra("location")

        textViewLocation.text = location

        Log.d("here", hospitalNames[1])
        Log.d("here", hospitalAddresses[1])

        val adapter = MyListAdapter(this, placeIds, hospitalNames, hospitalAddresses, database)
        listViewHospitals.adapter = adapter
        listViewHospitals.setOnItemClickListener{ adapterView: AdapterView<*>, view: View, i: Int, l: Long ->
            Log.d("position", adapterView.getChildAt(i).toString())
            val outIntent = Intent(this, HospitalViewActivity::class.java)
            outIntent.putExtra("placeId", placeIds[i])
            outIntent.putExtra("hospitalName", hospitalNames[i])
            outIntent.putExtra("hospitalAddress", hospitalAddresses[i])
            outIntent.putExtra("lat", lats[i])
            outIntent.putExtra("lng", lngs[i])
            Log.d("lat: ", lats[i].toString())
            Log.d("lng: ", lngs[i].toString())
            startActivity(outIntent)
        }
    }

    class MyListAdapter(
        var context: Context,
        private val placeIds: Array<String>,
        private val hospitalNames: Array<String>,
        private val hospitalAddresses: Array<String>,
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