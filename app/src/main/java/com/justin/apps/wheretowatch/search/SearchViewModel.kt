package com.justin.apps.wheretowatch.search

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.util.Log
import com.justin.apps.wheretowatch.model.Model
import com.justin.apps.wheretowatch.repository.MediaRepository
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

class SearchViewModel(val repo: MediaRepository) : ViewModel() {
    var mediaList: Maybe<List<Model.Media>> = Maybe.just(emptyList<Model.Media>())
    var loading = false
    var freshSearch = false

    // TODO: Implement the ViewModel
    fun searchMedia(country: String ,term: String)  {
        loading = true
        mediaList = repo.getFromApi(country, term).toMaybe()
    }
}

class SearchViewModelFactory(private val repo: MediaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel(repo) as T
    }

}
