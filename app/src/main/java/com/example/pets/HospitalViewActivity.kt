package com.example.pets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class HospitalViewActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var hospitalName2 : TextView
    lateinit var hospitalAddress2 : TextView
    lateinit var buttonViewHistory : Button
    lateinit var buttonWriteHistory : Button
    lateinit var buttonShare : Button
    lateinit var hospitalOpenHours: TextView
    lateinit var hospitalPhone : TextView

    lateinit var hospitalName : String
    lateinit var hospitalAddress : String
    lateinit var placeId : String


    private lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hospital_view)

        hospitalName = intent.getStringExtra("hospitalName")
        hospitalAddress = intent.getStringExtra("hospitalAddress")
        placeId = intent.getStringExtra("placeId")

        hospitalName2 = findViewById<TextView>(R.id.hospitalName2)
        hospitalAddress2 = findViewById<TextView>(R.id.hospitalAddress2)
        buttonViewHistory = findViewById<Button>(R.id.buttonViewHistory)
        buttonWriteHistory = findViewById<Button>(R.id.buttonWriteHistory)
        buttonShare = findViewById<Button>(R.id.buttonShare)
        hospitalOpenHours = findViewById<TextView>(R.id.hospitalOpenHours)
        hospitalPhone = findViewById<TextView>(R.id.hospitalPhone)

        hospitalName2.text = hospitalName
        hospitalAddress2.text = hospitalAddress


        buttonShare.setOnClickListener {
            val newIntent = Intent(Intent.ACTION_SEND)
            newIntent.type = "text/plain"

            var message = "-----"
            message += hospitalName + "\n"
            message += hospitalAddress + "\n"
            message += hospitalOpenHours.text.toString() + "\n"
            message += hospitalPhone.text.toString() + "\n"

            newIntent.putExtra(Intent.EXTRA_TEXT, message)

            val shareIntent = Intent.createChooser(newIntent, "공유하기")
            startActivity(shareIntent)
        }

        buttonViewHistory.setOnClickListener {
            var newIntent = Intent(this, ViewReviewActivity::class.java)
            newIntent.putExtra("placeId", placeId)
            newIntent.putExtra("hospitalName", hospitalName)
            startActivity(newIntent)
        }

        buttonWriteHistory.setOnClickListener {
            var newIntent = Intent(this, WriteReviewActivity::class.java)
            newIntent.putExtra("placeId", placeId)
            newIntent.putExtra("hospitalName", hospitalName)
            startActivity(newIntent)
        }

        hospitalPhone.setOnClickListener{
            val phone = hospitalPhone.text.toString().replace("전화번호 : ", "")
            val uri = Uri.parse("tel:${phone}")
            val newIntent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(newIntent)
        }


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }



    override fun onMapReady(googleMap: GoogleMap) {
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lng = intent.getDoubleExtra("lng", 0.0)
        map = googleMap
        val marker = LatLng(lat, lng)
        map.addMarker(MarkerOptions().position(marker).title(hospitalName))
        map.moveCamera(CameraUpdateFactory.newLatLng(marker))
        map.setMinZoomPreference(15.0f)
        map.setMaxZoomPreference(20.0f)

    }
}