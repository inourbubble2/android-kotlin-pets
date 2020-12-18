package com.example.pets

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    lateinit var buttonSearch : Button
    lateinit var editTextSearch : EditText
    lateinit var buttonStarList : Button
    lateinit var buttonReviewList : Button

    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        dbHelper = DBHelper(this, "newdb.db", null, 1)
        database = dbHelper.writableDatabase

        buttonSearch= findViewById<Button>(R.id.buttonSearch)
        editTextSearch = findViewById<EditText>(R.id.editTextSearch)
        buttonStarList = findViewById<Button>(R.id.buttonStarList)
        buttonReviewList = findViewById<Button>(R.id.buttonReviewList)

        buttonSearch.setOnClickListener {
            var location = editTextSearch.text.toString()
            var url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + location + "동물병원" + "&key=YOUR_API_KEY"
            val results = MyNetworkTask().doInBackground(url)
            Log.d("url:", url)

            if (results.length() == 0) {
                Toast.makeText(this@MainActivity, "해당 지역에 동물 병원이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val placeIds = arrayOfNulls<String>(results.length())
                val hospitalNames = arrayOfNulls<String>(results.length())
                val hospitalAddresses = arrayOfNulls<String>(results.length())
                val lats = DoubleArray(results.length())
                val lngs = DoubleArray(results.length())

                for(i in 0 until results.length()) {
                    val result = results.getJSONObject(i)

                    val placeId = result.getString("place_id")
                    val hospitalName = result.getString("name")
                    val hospitalAddress = result.getString("formatted_address")
                    var lat = result.getJSONObject("geometry").getJSONObject("location").getString("lat").toDouble()
                    var lng = result.getJSONObject("geometry").getJSONObject("location").getString("lng").toDouble()

                    placeIds[i] = placeId
                    hospitalNames[i] = hospitalName
                    hospitalAddresses[i] =hospitalAddress
                    lats[i] = lat
                    lngs[i] = lng

                    // Log.d(hospitalNames[i], "$hospitalAddress / $lat / $lng")
                }

                val intent = Intent(this, HospitalListActivity::class.java)
                intent.putExtra("location", location)
                intent.putExtra("placeIds", placeIds)
                intent.putExtra("hospitalNames", hospitalNames)
                intent.putExtra("hospitalAddresses", hospitalAddresses)
                intent.putExtra("lats", lats)
                intent.putExtra("lngs", lngs)
                startActivity(intent)
            }
        }

        buttonStarList.setOnClickListener {
            val newIntent = Intent(this, StarListActivity::class.java)
            startActivity(newIntent)
        }

        buttonReviewList.setOnClickListener {
            val newIntent = Intent(this, ReviewListActivity::class.java)
            startActivity(newIntent)
        }

        val buttonReset = findViewById<Button>(R.id.buttonReset)
        buttonReset.setOnClickListener {
            dbHelper.reset(database)
        }
    }

    internal class MyNetworkTask :  AsyncTask<String, Void, Any?>() {
        public override fun doInBackground(vararg url: String?): JSONArray {
            var results : JSONArray
            try {
                val url = URL(url.get(0))
                Log.d("", url.toString())
                val connection = url.openConnection() as HttpURLConnection
                val data = connection.inputStream.bufferedReader().readText()
                // Log.d("", data.toString())

                results = JSONObject(data).getJSONArray("results")
                Log.d("",results.toString())

                return results

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return JSONArray()
        }

    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions()
//            .position(sydney)
//            .title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//    }
}