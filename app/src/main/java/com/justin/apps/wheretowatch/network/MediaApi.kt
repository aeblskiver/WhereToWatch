package com.justin.apps.wheretowatch.network

import com.justin.apps.wheretowatch.util.constants.MASHAPE_APIKEY
import com.justin.apps.wheretowatch.util.constants.UTELLY_BASE_URL
import io.reactivex.Maybe
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Interface for Retrofit to create API implementation
 */
interface MediaApi {

    /**
     * Searches the Utelly API
     * @param country country code to search (only 'us' and 'uk' currently supported)
     * @param searchTerm the term used to search Utelly's API
     * @return the Utelly response
     */
    @Headers("X-Mashape-Key: $MASHAPE_APIKEY")
    @GET("lookup")
    fun search(
        @Query("country") country: String,
        @Query("term") searchTerm: String) : Maybe<MediaResponse>

    //Api service companion object
    companion object Factory{
        fun create(): MediaApi {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(UTELLY_BASE_URL)
                .build()

            return retrofit.create(MediaApi::class.java)
        }
    }
}