package com.justin.apps.wheretowatch.repository

import android.util.Log
import com.justin.apps.wheretowatch.model.Model
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

    /**
     *  Get response from the Utelly API
     *  @param country the country code (only 'us' and 'uk' currently supported)
     *  @param searchTerm the term used to search Utelly's API
     *  @return an Observable list of shows and movies
     */
    fun getFromApi(country: String, searchTerm: String): Maybe<List<Model.Media>> =
            apiService.search(country, searchTerm)
                .doOnError{
                    Log.d("MediaRepository", "Error: ${it.message}")
                }
                .map {
                    it.results
                }
}