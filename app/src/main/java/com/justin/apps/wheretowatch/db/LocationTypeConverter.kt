package com.justin.apps.wheretowatch.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.justin.apps.wheretowatch.model.Model

class LocationTypeConverter {

    val gson: Gson = Gson()

    @TypeConverter
    fun locationListToString(locations: List<Model.Location>): String {
        return gson.toJson(locations)
    }

    @TypeConverter
    fun stringToLocationList(string: String?): List<Model.Location> {
        if (string == null) {
            return emptyList()
        }
        val objects = gson.fromJson(string, Array<Model.Location>::class.java) as Array<Model.Location>
        return objects.toList()
    }

}