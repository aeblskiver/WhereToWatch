package com.justin.apps.wheretowatch.network

import com.justin.apps.wheretowatch.model.Model

data class MediaResponse (
    val updated: String,
    val term: String,
    val results: List<Model.Media>
)