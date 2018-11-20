package com.justin.apps.wheretowatch.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.justin.apps.wheretowatch.adapter.App
import com.justin.apps.wheretowatch.model.Model

@Database(version = 1, entities = [Model.Media::class])
@TypeConverters(LocationTypeConverter::class)
abstract class MediaRoomDatabase: RoomDatabase() {
    abstract fun roomDao(): RoomDao

    companion object {
        private var INSTANCE: MediaRoomDatabase? = null

        fun getInstance(context: Context): MediaRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(MediaRoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        App.appContext(),
                        MediaRoomDatabase::class.java,
                        "media.db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun clearInstance() {
            INSTANCE = null
        }
    }
}