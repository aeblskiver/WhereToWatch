package com.justin.apps.wheretowatch.network

import com.justin.apps.wheretowatch.model.Model


/**
 * Represents the response from Utelly
 * @property updated the date of the response
 * @property term the search term used
 * @property results a list of the movies and shows matching the search term
 */
data class MediaResponse (
    val updated: String,
    val term: String,
    val results: List<Model.Media>
)