package com.justin.apps.wheretowatch.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.justin.apps.wheretowatch.model.Model

/**
 *  Interface for Room database access object. Room creates the implementation.
 */
@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedia(media: Model.Media?)

    @Delete
    fun deleteMedia(media: Model.Media?)

    @Query("SELECT * FROM media")
    fun loadAllMedia(): LiveData<List<Model.Media>>
}