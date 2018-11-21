package com.justin.apps.wheretowatch.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.squareup.moshi.Json

class Model {

    /**
     * Models a show or movie
     * @property picture the url for the media's picture
     * @property name the title of the media
     * @property locations a list of streaming services that have the media available
     * @property weight how close to the search term it is
     * @property id unique id
     */
    @Entity(tableName = "media")
    data class Media(
        val picture: String,
        val name: String,
        val locations: List<Location>,
        val weight: Int,
        @PrimaryKey val id: String
    )

    /**
     * Models location for an online streaming service (e.g. Netfix, Amazon)
     * @property displayName name used for displaying
     * @property name name of the service
     * @property url url for the media on the location
     * @property id unique id
     * @property icon url for icon image
     */
    data class Location(
        @field:Json(name = "display_name") val displayName: String,
        val name: String,
        val url: String,
        val id: String,
        val icon: String
    )

}