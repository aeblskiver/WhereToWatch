package com.justin.apps.wheretowatch.base

import com.justin.apps.wheretowatch.model.Model

interface FavoriteClickListener {
    fun onClick(media: Model.Media?, favorite: Boolean)
}