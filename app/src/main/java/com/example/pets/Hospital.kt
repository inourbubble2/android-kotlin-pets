package com.example.pets

import android.os.Parcel
import android.os.Parcelable

class Hospital() : Parcelable {
//    var placeId: String, var name: String, var address: String, var lat: Double, var lng: Double
    var placeId: String ? = null
    var name: String ? = null
    var address: String ? = null
    var lat: Double ? = null
    var lng: Double ? = null

    constructor(parcel: Parcel) : this() {
        parcel.run {
            placeId = readString()
            name = readString()
            address = readString()
            lat = readDouble()
            lng = readDouble()
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel?.run {
            writeString(this@Hospital.placeId)
            writeString(this@Hospital.name)
            writeString(this@Hospital.address)
            this@Hospital.lat?.let { writeDouble(it) }
            this@Hospital.lng?.let { writeDouble(it) }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Hospital> {
        override fun createFromParcel(parcel: Parcel): Hospital {
            return Hospital(parcel)
        }

        override fun newArray(size: Int): Array<Hospital?> {
            return arrayOfNulls(size)
        }
    }
}