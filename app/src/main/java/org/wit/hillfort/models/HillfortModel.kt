package org.wit.hillfort.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@TypeConverters(Converters:: class)
data class HillfortModel(@PrimaryKey(autoGenerate = true) var id: Long=0,
                         var title: String = "",
                         var description: String = "",
                         var images: ArrayList<String> = ArrayList<String>(),
                         var visited: Boolean= false,
                         var date: String = "",
                         var notes: String ="",
                         var lat : Double = 0.0,
                         var lng: Double = 0.0,
                         var zoom: Float = 0f): Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable