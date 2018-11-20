package com.justin.apps.wheretowatch.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.justin.apps.wheretowatch.adapter.App
import com.justin.apps.wheretowatch.db.MediaRoomDatabase
import com.justin.apps.wheretowatch.model.Model
import com.justin.apps.wheretowatch.model.Model.Media
import com.justin.apps.wheretowatch.network.MediaApi
import com.justin.apps.wheretowatch.network.MediaResponse
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * A repository for fetching and caching list of Media (shows and movies) from Utelly API or local database
 *  @property apiService the API service created by Retrofit for making requests to Utelly
 */
object MediaRepository {
    private val apiService by lazy { MediaApi.create() }
    private val db by lazy { MediaRoomDatabase.getInstance(App.appContext()) }

    /**
     *  Get response from the Utelly API
     *  @param country the country code (only 'us' and 'uk' currently supported)
     *  @param searchTerm the term used to search Utelly's API
     *  @return an Observable list of shows and movies
     */
    fun getFromApi(country: String, searchTerm: String): Maybe<List<Media>> =
            apiService.search(country, searchTerm)
                .doOnError{
                    Log.d("MediaRepository", "Error: ${it.message}")
                }
                .map {
                    it.results
                }

    fun loadFromDatabase(): LiveData<List<Media>> {
        return db?.roomDao()?.loadAllMedia() ?: MutableLiveData<List<Media>>()
    }

    suspend fun insert(media: Media?) {
        db?.roomDao()?.insertMedia(media)
    }

    fun remove(media: Media) {
        db?.roomDao()?.deleteMedia(media)
    }

}