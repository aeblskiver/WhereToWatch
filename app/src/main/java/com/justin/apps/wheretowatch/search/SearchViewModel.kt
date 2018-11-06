package com.justin.apps.wheretowatch.search

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.util.Log
import com.justin.apps.wheretowatch.model.Model
import com.justin.apps.wheretowatch.repository.MediaRepository
import io.reactivex.Single

class SearchViewModel(val repo: MediaRepository) : ViewModel() {
    lateinit var mediaList: Single<List<Model.Media>>

    val searchTerm: String? = null

    fun getMedia(): Single<List<Model.Media>>? {
        return mediaList
    }
    // TODO: Implement the ViewModel
    fun searchMedia(country: String ,term: String)  {
        Log.d("SearchViewModel", "Searching for $term")
        mediaList = repo.getFromApi(country, term)
        Log.d("SearchViewModel", "List: ${mediaList}")
    }


}

class SearchViewModelFactory(val repo: MediaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel(repo) as T
    }

}
