package com.justin.apps.wheretowatch.model

import com.squareup.moshi.Json

class Model {

    data class Media(
        val picture: String,
        val name: String,
        val locations: List<Locations>,
        val weight: Int,
        val id: String
    )

    data class Locations(
        @Json(name = "display_name") val displayName: String,
        val name: String,
        val url: String,
        val id: String,
        val icon: String
    )

}