package com.justin.apps.wheretowatch.network

import android.util.JsonReader
import android.util.Log
import com.justin.apps.wheretowatch.model.Model
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream
import java.lang.Exception

class TestMediaApi() : MediaApi {

    private val TAG = "TestMediaApi"

    private val responsePath: String = "D:\\WheretoWatch\\app\\src\\test\\java\\com\\justin\\apps\\wheretowatch\\utils\\response.json"

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val response: JsonAdapter<MediaResponse>? = moshi.adapter<MediaResponse>(MediaResponse::class.java)

    override fun search(country: String, searchTerm: String): Single<MediaResponse> {
            try {
                val json = File(responsePath).inputStream().readBytes().toString(Charsets.UTF_8)
                val mediaResponse: MediaResponse? = response?.fromJson(json)
                mediaResponse?.let {
//                    System.out.println("Media Response: $mediaResponse")
                }
             return Single.just(mediaResponse)

            } catch (e: Exception) {
                System.out.println("Exception: $e")
        }

        return Single.just(MediaResponse("Error", "Error", listOf()))
    }
}