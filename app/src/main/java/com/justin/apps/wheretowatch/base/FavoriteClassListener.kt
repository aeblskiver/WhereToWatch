package com.justin.apps.wheretowatch.base

import com.justin.apps.wheretowatch.model.Model

/**
 *  Interface for listening to click events on favorite icon
 */
interface FavoriteClickListener {
    fun onClick(media: Model.Media?, favorite: Boolean)
}